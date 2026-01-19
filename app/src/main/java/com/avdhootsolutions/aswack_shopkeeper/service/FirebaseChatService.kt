package com.avdhootsolutions.aswack_shopkeeper.service


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.avdhootsolutions.aswack_shopkeeper.enums.FileTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.*
import java.util.*


class FirebaseChatService() {

    val db = FirebaseFirestore.getInstance()

    lateinit var chatServiceListener: ChatServiceListener

    lateinit var userServiceListener : UserServiceListener

    lateinit var sellerRFQServiceListener : SellerRFQServiceListener


    //Firebase
    var storage: FirebaseStorage? = FirebaseStorage.getInstance()
    var storageReference: StorageReference? = storage?.getReference();


    fun setChatListner(chatServiceListener: ChatServiceListener, userServiceListener: UserServiceListener) {
        this.chatServiceListener = chatServiceListener
        this.userServiceListener = userServiceListener
    }

    fun setSellerRFQListner(sellerRFQServiceListener: SellerRFQServiceListener) {
        this.sellerRFQServiceListener = sellerRFQServiceListener

    }


    fun addChat(chat: String, userName: String, productId: String, userId: String, sellerId : String, context: Context): Boolean {

        var isAdded: Boolean = false
        // Create a reference
        val dbChat: CollectionReference = db.collection("ChatData_$productId" + "_"  + userId + "_" + sellerId)
        val chatMessage = ChatMessage()
        chatMessage.text = chat
        chatMessage.user = userName

        // below method is use to add data to Firebase Firestore.
        dbChat.add(chatMessage)
            .addOnSuccessListener(OnSuccessListener<DocumentReference?> { // after the data addition is successful
                // we are displaying a success toast message.

                chatMessage.id = it.id

                createNotify(context)

//                    filePath?.let {
//                        uploadImage(it,category.imageId,context, 0,category)
//                    }

                isAdded = true
            })
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                isAdded = false
            })


        return isAdded
    }

    fun getUserDataFromSellerId(sellerId: String) {

        Log.e("get selllers " , "yess " + sellerId)

        db.collection("Sellers").whereEqualTo("sellerId", sellerId).get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {

                        var isUserAvail = false

                        for (document in task.result!!) {
                            val userData: SellerDataChat = document.toObject(SellerDataChat::class.java)
                            userData.id = document.id
                            userServiceListener.getUserDetails(userData)
                            isUserAvail = true
                        }

                        if (!isUserAvail){
                            userServiceListener.getUserDetails(null)
                        }
                    }
                })
    }


    /**
     * Get seller data from firestore database
     * If seller found from the data base, Then see that RFQ is available or not in that seller data
     * If RFQ is not available then add
     * And if RFQ is available then return
     */
    fun getSellerAndAddUpdateRFQ(userId: String, rfqList: RFQList) {
        db.collection("Sellers").whereEqualTo("sellerId", userId).get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {

                        var isUserAvail = false

                        for (document in task.result!!) {
                            val sellerData: SellerDataChat = document.toObject(SellerDataChat::class.java)
                            sellerData.id = document.id

                            var isRFQAdded = false

                            for (j in sellerData.productDataList.indices){
                                if (sellerData.productDataList[j].onlyRFQDetails && sellerData.productDataList[j].rfqId == rfqList.id){
                                    isRFQAdded = true

                                    sellerRFQServiceListener.getSellerDetailsAndAddedProduct(sellerData, sellerData.productDataList[j])
                                    break
                                }
                            }

                            if (!isRFQAdded){
                                val productDataForSeller = ProductDataForSeller()
                                productDataForSeller.rfqId = rfqList.id!!
                                productDataForSeller.rfqCode = rfqList.rfq_code!!

                                productDataForSeller.categoryId = rfqList.master_category_id!!

                                productDataForSeller.userId = rfqList.user_id!!
                                productDataForSeller.userName = rfqList.user_name!!

                                productDataForSeller.companyId = rfqList.seller_company_id!!

                                productDataForSeller.onlyRFQDetails = true

                                sellerData.productDataList.add(productDataForSeller)

                                // Here chat is initiated by seller
                                sellerData.sellerName = rfqList.seller_name?:""
                                updateSellerData(sellerData)

                                sellerRFQServiceListener.getSellerDetailsAndAddedProduct(sellerData, productDataForSeller)
                            }


                            isUserAvail = true
                        }

                        if (!isUserAvail){
                            sellerRFQServiceListener.getSellerDetailsAndAddedProduct(null, null)
                        }
                    }
                })
    }


    /**
     * Get user data from firestore database
     * If user found from the data base, Then see that RFQ is available or not in that user data
     * If RFQ is not available then add
     * And if RFQ is available then return that data
     */
    fun getUserDataAndAddUpdateRFQ(userId: String, productData: ProductData) {
        db.collection("Users").whereEqualTo("userId", userId).get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {

                        var isUserAvail = false

                        for (document in task.result!!) {
                            val userData: UserDataChat = document.toObject(UserDataChat::class.java)

                            Log.e("comes from ", " Get seller data")

                            userData.id = document.id

                            var isRFQAdded = false

                            for (j in userData.productDataList.indices){
                                if (userData.productDataList[j].onlyRFQDetails
                                    && userData.productDataList[j].rfqId == productData.rfqId
                                    && productData.onlyRFQDetails){
                                    isRFQAdded = true
                                    Log.e("comes from ", " product added already")
                                    sellerRFQServiceListener.getUserDetailsAndAddedProduct(userId,userData, userData.productDataList[j])
                                    break
                                }
                            }

                            // Here if RFQ is not found then it will be add
                            if (!isRFQAdded){
                                Log.e("comes from ", " Add offer in seller data")
                                userData.productDataList.add(productData)
                                updateUserData(userData)

                                sellerRFQServiceListener.getUserDetailsAndAddedProduct(userId,userData, productData)
                            }


                            isUserAvail = true
                        }

                        if (!isUserAvail){
                            sellerRFQServiceListener.getUserDetailsAndAddedProduct(userId,null, productData)
                        }
                    }
                })
    }


    fun getUserDataAndAddQuotationDetails(productDataForSeller: ProductDataForSeller) {
        db.collection("Users").whereEqualTo("userId", productDataForSeller.userId).get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {

                        var isUserAvail = false

                        for (document in task.result!!) {
                            val userData: UserDataChat = document.toObject(UserDataChat::class.java)
                            userData.id = document.id

                            for (j in userData.productDataList.indices){

                                if (userData.productDataList[j].onlyOfferDetails){
                                    if (userData.productDataList[j].offerId == productDataForSeller.offerId){
                                        userData.productDataList[j].quotationId = productDataForSeller.quotationId
                                        userData.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }

                                if (userData.productDataList[j].onlyRFQDetails){
                                    if (userData.productDataList[j].rfqCode == productDataForSeller.rfqCode){
                                        userData.productDataList[j].quotationId = productDataForSeller.quotationId
                                        userData.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }

                                if (!userData.productDataList[j].onlyOfferDetails
                                    && !userData.productDataList[j].onlyCompanyDetail
                                    && !userData.productDataList[j].onlyRFQDetails){
                                    if (userData.productDataList[j].productId == productDataForSeller.productId){
                                        userData.productDataList[j].quotationId = productDataForSeller.quotationId
                                        userData.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }
                            }

                            updateUserData(userData)
                            userServiceListener.isQuotationAddedInUser(true, productDataForSeller)

                        }

                    }
                })
    }

    fun getSellerDataAndAddQuotationDetails(sellerId: String, productDataForSeller: ProductDataForSeller) {
        db.collection("Sellers").whereEqualTo("sellerId", sellerId).get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {

                        var isUserAvail = false

                        for (document in task.result!!) {
                            val sellerDataChat: SellerDataChat = document.toObject(SellerDataChat::class.java)
                            sellerDataChat.id = document.id
                            for (j in sellerDataChat.productDataList.indices){

                                if (sellerDataChat.productDataList[j].onlyOfferDetails){
                                    if (sellerDataChat.productDataList[j].offerId == productDataForSeller.offerId){
                                        sellerDataChat.productDataList[j].quotationId = productDataForSeller.quotationId
                                        sellerDataChat.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }

                                if (sellerDataChat.productDataList[j].onlyRFQDetails){
                                    if (sellerDataChat.productDataList[j].rfqCode == productDataForSeller.rfqCode){
                                        sellerDataChat.productDataList[j].quotationId = productDataForSeller.quotationId
                                        sellerDataChat.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }

                                if (!sellerDataChat.productDataList[j].onlyOfferDetails
                                    && !sellerDataChat.productDataList[j].onlyCompanyDetail
                                    && !sellerDataChat.productDataList[j].onlyRFQDetails){
                                    if (sellerDataChat.productDataList[j].productId == productDataForSeller.productId){
                                        sellerDataChat.productDataList[j].quotationId = productDataForSeller.quotationId
                                        sellerDataChat.productDataList[j].quotationRequest = productDataForSeller.quotationRequest
                                    }
                                }


                            }

                            updateSellerData(sellerDataChat)
                            userServiceListener.isQuotationAddedInSellers(true, sellerId)

                        }

                    }
                })
    }


    fun addSellerForChat(sellerDataChat: SellerDataChat): Boolean {

        var isAdded: Boolean = false
        // Create a reference
        val dbChat: CollectionReference = db.collection("Sellers")

        // below method is use to add data to Firebase Firestore.
        dbChat.add(sellerDataChat)
            .addOnSuccessListener(OnSuccessListener<DocumentReference?> { // after the data addition is successful
                // we are displaying a success toast message.

                sellerDataChat.id = it.id

                val userDataChat = UserDataChat()
                userDataChat.userId = sellerDataChat.productDataList[0].userId
                userDataChat.userName = sellerDataChat.productDataList[0].userName

                val poductData = ProductData()
                poductData.rfqId = sellerDataChat.productDataList[0].rfqId
                poductData.rfqCode= sellerDataChat.productDataList[0].rfqCode

                poductData.categoryId = sellerDataChat.productDataList[0].categoryId

                poductData.companyId = sellerDataChat.productDataList[0].companyId

                poductData.sellerId = sellerDataChat.sellerId
                poductData.sellerName = sellerDataChat.sellerName

                poductData.onlyRFQDetails = sellerDataChat.productDataList[0].onlyRFQDetails

                userDataChat.productDataList.add(poductData)

                addUserForChat(userDataChat)
                isAdded = true
            })
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.

                sellerRFQServiceListener.addSellerdata(false)

                isAdded = false
            })


        return isAdded
    }


    fun addUserForChat(userDataChat: UserDataChat): Boolean {

        var isAdded: Boolean = false
        // Create a reference
        val dbChat: CollectionReference = db.collection("Users")

        // below method is use to add data to Firebase Firestore.
        dbChat.add(userDataChat)
            .addOnSuccessListener(OnSuccessListener<DocumentReference?> { // after the data addition is successful
                // we are displaying a success toast message.

                userDataChat.id = it.id

                sellerRFQServiceListener.addSellerdata(true)
                isAdded = true
            })
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.

                sellerRFQServiceListener.addSellerdata(false)

                isAdded = false
            })


        return isAdded
    }


    fun addSessionDetailInChat(chat: String, userName: String, groupId : String): Boolean {

        var isAdded: Boolean = false
        // Create a reference
        val dbChat: CollectionReference = db.collection("ChatData_$groupId")
        val chatMessage = ChatMessage()
        chatMessage.text = chat
        chatMessage.user = userName


        // below method is use to add data to Firebase Firestore.
        dbChat.add(chatMessage)
            .addOnSuccessListener(OnSuccessListener<DocumentReference?> { // after the data addition is successful
                // we are displaying a success toast message.

                chatMessage.id = it.id

//                    filePath?.let {
//                        uploadImage(it,category.imageId,context, 0,category)
//                    }

                isAdded = true
            })
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                isAdded = false
            })


        return isAdded
    }



    fun uploadImageOrVideo1(
        filePath: Uri?,
        randomId: String,
        context: Context,
        chatMessage: ChatMessage,
        fileType: Int,
        chatServiceListener: ChatServiceListener,
        groupId: String,
    ) {
        var message = ""
        if (filePath != null) {
            var folderName = ""
            when (fileType) {
                FileTypeEnum.IMAGE.ordinal -> {
                    folderName = "Image"
                }

                FileTypeEnum.VIDEO.ordinal -> {
                    folderName = "Video"
                }
            }




            val ref: StorageReference? =
                storageReference?.child("$folderName/$randomId")
            ref?.putFile(filePath)
                ?.addOnSuccessListener {

                    it.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        val uri = task.result!!.toString()
                        Log.e("downloaded uri ",uri)
                        when (fileType) {
                            FileTypeEnum.IMAGE.ordinal -> {
                                db.collection("ChatData_$groupId").document(chatMessage.id).update("imageUrl", uri)
                            }

                            FileTypeEnum.VIDEO.ordinal -> {
                                db.collection("ChatData_$groupId").document(chatMessage.id).update("videoUrl", uri)
                            }
                        }


                    }


                    chatServiceListener.imageUploaded(true)
                    message = "Uploaded"
                    //    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


                }
                ?.addOnFailureListener { e ->
                    chatServiceListener.imageUploaded(false)
//                    progressDialog.dismiss()
                    //  bannerServiceListener.bannerAdded(true)
                    message = "Failed " + e.message


                    db.collection("ChatData_$groupId").document(chatMessage.id).delete()
                    //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                }
                ?.addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }

    }

    fun uploadImageOrVideo(
        chat: String, userName: String, filePath: Uri?,
        randomId: String, context: Context,
        fileType: Int,
        chatServiceListener: ChatServiceListener,
        productId: String,
        userId: String,
        sellerId: String
    ) {
        var message = ""
        if (filePath != null) {
            var folderName = ""
            when (fileType) {
                FileTypeEnum.IMAGE.ordinal -> {
                    folderName = "Image"
                }

                FileTypeEnum.VIDEO.ordinal -> {
                    folderName = "Video"
                }
            }

            val  progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()


            val ref: StorageReference? =
                storageReference?.child("$folderName/$randomId")
            ref?.putFile(filePath)
                ?.addOnSuccessListener {

                    it.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        val uri = task.result!!.toString()
                        Log.e("downloaded uri ",uri)

                        addImageWithChat(chat, userName, filePath,
                            randomId,
                            fileType, uri,
                            chatServiceListener,productId, userId, sellerId)

                    }


                    chatServiceListener.imageUploaded(true)
                    message = "Uploaded"

                    progressDialog.dismiss()
                    //    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


                }
                ?.addOnFailureListener { e ->
                    chatServiceListener.imageUploaded(false)
                    progressDialog.dismiss()
                    //  bannerServiceListener.bannerAdded(true)
                    message = "Failed " + e.message
                    //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                }
                ?.addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }

    }

    fun addImageWithChat(
        chat: String, userName: String, filePath: Uri?,
        randomId: String,
        fileType: Int, imageUrl: String,
        chatServiceListener: ChatServiceListener,
        productId: String,
        userId: String,
        sellerId: String
    ): Boolean {

        var isAdded: Boolean = false
        // Create a reference
        val dbChat: CollectionReference = db.collection("ChatData_$productId" + "_"  + userId + "_" + sellerId)
        val chatMessage = ChatMessage()
        chatMessage.text = chat
        chatMessage.user = userName

        when (fileType) {
            FileTypeEnum.IMAGE.ordinal -> {
                chatMessage.imageId = randomId
                chatMessage.imageUrl = imageUrl
            }

            FileTypeEnum.VIDEO.ordinal -> {
                chatMessage.videoId = randomId
                chatMessage.videoUrl = imageUrl
            }
        }

        when(fileType){
            FileTypeEnum.IMAGE.ordinal->{
                chatMessage.imageId = randomId
            }
            FileTypeEnum.VIDEO.ordinal->{
                chatMessage.videoId = randomId
            }
        }

        // below method is use to add data to Firebase Firestore.
        dbChat.add(chatMessage)
            .addOnSuccessListener(OnSuccessListener<DocumentReference?> { // after the data addition is successful
                // we are displaying a success toast message.

                chatMessage.id = it.id
                isAdded = true
            })
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                isAdded = false
            })


        return isAdded
    }

    // get realtime updates from firebase regarding saved addresses
    fun getAllChatDataLive(productId: String, userId: String, sellerId : String) {
        db.collection("ChatData_$productId" + "_"  + userId + "_" + sellerId )
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    //    Log.w(TAG, "Listen failed.", e)
                    //  savedAddresses.value = null

                }

                var ChatDataList: MutableList<ChatMessage> = mutableListOf()
                for (doc in value!!) {
                    var ChatData = doc.toObject(ChatMessage::class.java)
                    ChatData.id = doc.id


                    ChatDataList.add(ChatData)
                    ChatDataList.sortByDescending { it.timestamp }

                }


                chatServiceListener.fetchChatList(ChatDataList)
            })


//        return savedAddresses
    }




    /**
     * Delete Chat
     */
    fun deleteChat(chatMessage: ChatMessage, productId: String, userId: String, sellerId : String){
        db.collection("ChatData_$productId" + "_"  + userId + "_" + sellerId ).document(chatMessage.id)
            .delete()
            .addOnSuccessListener {

                if (chatMessage.imageId.isNotEmpty() || chatMessage.videoId.isNotEmpty()){
                    deleteImage(chatMessage)
                }

            }
            .addOnFailureListener { }
    }


    fun updateUserData(userDataChat: UserDataChat){
        db.collection("Users").document(userDataChat.id).set(userDataChat)
    }

    fun updateSellerData(sellerDataChat: SellerDataChat){
        db.collection("Sellers").document(sellerDataChat.id).set(sellerDataChat)
    }

    private fun deleteImage(chatMessage: ChatMessage) {

        var folderName = ""
        var fileName = ""

        if (chatMessage.imageId.isNotEmpty()){
            folderName = "Image"
            fileName = chatMessage.imageId
        }else if (chatMessage.videoId.isNotEmpty()){
            folderName = "Video"
            fileName = chatMessage.videoId
        }

        storageReference?.child(folderName)?.child(fileName)?.delete()
            ?.addOnSuccessListener(OnSuccessListener<Void?> { // File deleted successfully
                Log.d("Firebase MenuService", "onSuccess: deleted file")
            })?.addOnFailureListener(OnFailureListener { // Uh-oh, an error occurred!
                Log.d("Firebase MenuService", "onFailure: did not delete file")
            })
    }


    private fun createNotify(context: Context) {

        //AlarmService
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager?
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 5)
        val intent = Intent("my.wedee.com.inilahapps.AlarmReceiver.DISPLAY_NOTIFICATION")
        val broadcast =
            PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast)
    }


    interface ChatServiceListener {
        fun fetchChatList(categoryList: List<ChatMessage>)
        fun imageUploaded(isSuccess: Boolean)
    }


    interface UserServiceListener {
        fun getUserDetails(sellerDataChat: SellerDataChat?)
        fun `isQuotationAddedInSellers`(isAdd: Boolean,userId: String)
        fun isQuotationAddedInUser(isAdd: Boolean,productDataForSeller : ProductDataForSeller)
    }


    interface SellerRFQServiceListener {
        fun addSellerdata(isSuccess: Boolean)
        fun getUserDetailsAndAddedProduct( userId: String,userDataChat: UserDataChat?, productDataF: ProductData)
        fun getSellerDetailsAndAddedProduct(sellerDataChat: SellerDataChat?, productDataForSeller: ProductDataForSeller?)

    }

}
