package com.avdhootsolutions.aswack_shopkeeper.interfaces


import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.squareup.okhttp.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import okhttp3.MultipartBody

import retrofit2.http.Multipart

import retrofit2.http.Url






interface API {

    @Headers("Content-Type: application/json")
    @POST("registration.php")
    fun apiRegister(@Body register: Register): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("login.php")
    fun apiLogin(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("forgot_password.php")
    fun apiForgotPassword(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("country_list.php")
    fun getCountryList(): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("state_list.php")
    fun getStateList(@Body register: Register): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("city_list.php")
    fun getCityList(@Body register: Register): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("category_list.php")
    fun getCategoryList(): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("seller_profile_update.php")
    fun apiUserProfileUpdate(@Body register: Register): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("seller_profile.php")
    fun apiGetProfile(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("page_detail.php")
    fun apiGetPageDetails(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("vehicle_brand.php")
    fun apiGetVehicleBrandsTypeModels(@Body sellVehicle: SellVehicle): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("seller_booking_list.php")
    fun apiGetBookingList(@Body Login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("update_seller_booking.php")
    fun apiChangeBookingStatus(@Body bookingStatusRequest: BookingStatusRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("vehicle_sell.php")
    fun apiSellVehicles(@Body sellVehicle: SellVehicle): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("update_sell_vehicle.php")
    fun apiUpdateSellVehicles(@Body updateSellVehicleRequest: UpdateSellVehicleRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("vehicle_year.php")
    fun getYearList(): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("vehicle_fuel.php")
    fun getFuelList(): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("offer_list.php")
    fun apiGetOfferList(@Body offer: Offer): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("user/get_offer_detail.php")
    fun apiGetSearchVehicleOfferList(@Body offer: Offer): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("order_list.php")
    fun apiGetOrderList(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("package_list.php")
    fun apiGetPackageList(@Body packageRequest: PackageRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("custom_package_list.php")
    fun apiGetCustomPackageList(@Body packageRequest: PackageRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("package_list.php")
    fun apiGetSellerPackageList(@Body packageRequest: PackageRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("seller_package_list.php")
    fun apiGetPurchasedPackageList(@Body login: Login): Call<Datas>


    @Headers("Content-Type: application/json")
    @POST("vehicle_category.php")
    fun getVehicleList(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("main_slider.php")
    fun getSliders(): Call<Datas>

    @Headers("Content-Type: application/json")
    @GET("main_cover.php")
    fun getMainPageImage(): Call<Datas>


    @Headers("Content-Type: application/json")
    @POST("company_status.php")
    fun apiGetCompanyRegisterStatusForCat(@Body Login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("company_package_status.php")
    fun apiGetPackageStatusForCat(@Body Login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("logout.php")
    fun apiLogout(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("vehicle_sell_list.php")
    fun apiGetVehicleSellList(@Body Login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("user/vehicle_sell_list.php")
    fun apiGetVehicleSellListFromUserApp(@Body Login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("sub_category.php")
    fun apiGetSubcategoryList(@Body categories: Categories): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("usage_product_offer.php")
    fun apiGetUsageProductOfferNotification(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("vehicle_buy_list.php")
    fun apiBuyVehicles(@Body buyVehicle: BuyVehicle): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("verify_seller_category.php")
    fun apiCheckCategoryIsRegistered(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("user/get_company_detail.php")
    fun apiGetCompanyDetail(@Body productList: ProductList): Call<Datas>

//    @Headers("Content-Type: application/json")
//    @POST("update_company_detail.php")
//    fun apiUpdateCompanyDetail(@Body companyRegister: CompanyRegister): Call<Datas>


    @Multipart
    @POST("update_image.php")
    fun apiUpdateImage(@Part image: MultipartBody.Part?,
                               @Part("id") id: okhttp3.RequestBody,
                               @Part("no") no: okhttp3.RequestBody,
                               @Part("type") type: okhttp3.RequestBody
    ): Call<Datas>

    @Multipart
    @POST("update_company_detail.php")
    fun apiUpdateCompanyDetail(@Part file: MultipartBody.Part?,
                                    @Part("company_id") company_id: okhttp3.RequestBody,
                                    @Part("ownername") ownername: okhttp3.RequestBody,
                                    @Part("phone") phone: okhttp3.RequestBody,
                                    @Part("mobile") mobile: okhttp3.RequestBody,
                                    @Part("amobile") amobile: okhttp3.RequestBody,
                                    @Part("email") email: okhttp3.RequestBody,
                                    @Part("website") website: okhttp3.RequestBody,
                                    @Part("houseno") houseno: okhttp3.RequestBody,
                                    @Part("streetno") streetno: okhttp3.RequestBody,
                                    @Part("landmark") landmark: okhttp3.RequestBody,
                                    @Part("pincode") pincode: okhttp3.RequestBody,
                                    @Part("detail") detail: okhttp3.RequestBody

    ): Call<Datas>

    @Multipart
    @POST("update_company_detail.php")
    fun apiUpdateCompanyDetailWithoutFile(
                               @Part("company_id") company_id: okhttp3.RequestBody,
                               @Part("ownername") ownername: okhttp3.RequestBody,
                               @Part("phone") phone: okhttp3.RequestBody,
                               @Part("mobile") mobile: okhttp3.RequestBody,
                               @Part("amobile") amobile: okhttp3.RequestBody,
                               @Part("email") email: okhttp3.RequestBody,
                               @Part("website") website: okhttp3.RequestBody,
                               @Part("houseno") houseno: okhttp3.RequestBody,
                               @Part("streetno") streetno: okhttp3.RequestBody,
                               @Part("landmark") landmark: okhttp3.RequestBody,
                               @Part("pincode") pincode: okhttp3.RequestBody,
                               @Part("detail") detail: okhttp3.RequestBody

    ): Call<Datas>



    @Multipart
    @POST("company_registration.php")
    fun apiCompanyRegisterWithImage(@Part file: MultipartBody.Part,
                                    @Part("seller_id") seller_id: okhttp3.RequestBody,
                                    @Part("name") name: okhttp3.RequestBody,
                                    @Part("ownername") ownername: okhttp3.RequestBody,
                                    @Part("phone") phone: okhttp3.RequestBody,
                                    @Part("mobile") mobile: okhttp3.RequestBody,
                                    @Part("amobile") amobile: okhttp3.RequestBody,
                                    @Part("email") email: okhttp3.RequestBody,
                                    @Part("website") website: okhttp3.RequestBody,
                                    @Part("country") country: okhttp3.RequestBody,
                                    @Part("state") state: okhttp3.RequestBody,
                                    @Part("city") city: okhttp3.RequestBody,
                                    @Part("houseno") houseno: okhttp3.RequestBody,
                                    @Part("streetno") streetno: okhttp3.RequestBody,
                                    @Part("landmark") landmark: okhttp3.RequestBody,
                                    @Part("pincode") pincode: okhttp3.RequestBody,
                                    @Part("detail") detail: okhttp3.RequestBody,
                                    @Part("officetime") officetime: okhttp3.RequestBody,
                                    @Part("package_id") package_id: okhttp3.RequestBody,
                                    @Part("custom_request") custom_request: okhttp3.RequestBody,
                                    @Part("offer") offer: okhttp3.RequestBody,
                                    @Part("post") post: okhttp3.RequestBody,
                                    @Part("notification") notification: okhttp3.RequestBody,
                                    @Part("duration") duration: okhttp3.RequestBody,
                                    @Query("type[]") type:ArrayList<String>,
                                    @PartMap times: HashMap<String, String>,
//                                    @Part("times[]") times: MutableList<OfficeTimings>,
                                    @Query("garage_subcategory[]") garage_subcategory:ArrayList<String>,
                                    @Query("emergency_subcategory[]") emergency_subcategory:ArrayList<String>,
                                    @Query("breakdown_subcategory[]") breakdown_subcategory:ArrayList<String>,
                                    @Query("insurance_subcategory[]") insurance_subcategory:ArrayList<String>,
                                    @Part("lat") lat: okhttp3.RequestBody,
                                    @Part("long") long: okhttp3.RequestBody,
                                    @Part("company_type") company_type: okhttp3.RequestBody,
                                    @Part("specialize_in") specialize_in: okhttp3.RequestBody,

    ): Call<Datas>


    @Multipart
    @POST("company_registration.php")
    fun apiCompanyRegisterWithoutImage(@Part("seller_id") seller_id: okhttp3.RequestBody,
                                    @Part("name") name: okhttp3.RequestBody,
                                    @Part("ownername") ownername: okhttp3.RequestBody,
                                    @Part("phone") phone: okhttp3.RequestBody,
                                    @Part("mobile") mobile: okhttp3.RequestBody,
                                    @Part("amobile") amobile: okhttp3.RequestBody,
                                    @Part("email") email: okhttp3.RequestBody,
                                    @Part("website") website: okhttp3.RequestBody,
                                    @Part("country") country: okhttp3.RequestBody,
                                    @Part("state") state: okhttp3.RequestBody,
                                    @Part("city") city: okhttp3.RequestBody,
                                    @Part("houseno") houseno: okhttp3.RequestBody,
                                    @Part("streetno") streetno: okhttp3.RequestBody,
                                    @Part("landmark") landmark: okhttp3.RequestBody,
                                    @Part("pincode") pincode: okhttp3.RequestBody,
                                    @Part("detail") detail: okhttp3.RequestBody,
                                    @Part("officetime") officetime: okhttp3.RequestBody,
                                    @Part("package_id") package_id: okhttp3.RequestBody,
                                    @Part("custom_request") custom_request: okhttp3.RequestBody,
                                    @Part("offer") offer: okhttp3.RequestBody,
                                    @Part("post") post: okhttp3.RequestBody,
                                    @Part("notification") notification: okhttp3.RequestBody,
                                    @Part("duration") duration: okhttp3.RequestBody,
                                    @Query("type[]") type:ArrayList<String>,
                                    @PartMap times: HashMap<String, String>,
//                                    @Part("times[]") times: MutableList<OfficeTimings>,
                                    @Query("garage_subcategory[]") garage_subcategory:ArrayList<String>,
                                    @Query("emergency_subcategory[]") emergency_subcategory:ArrayList<String>,
                                    @Query("breakdown_subcategory[]") breakdown_subcategory:ArrayList<String>,
                                    @Query("insurance_subcategory[]") insurance_subcategory:ArrayList<String>,
                                    @Part("lat") lat: okhttp3.RequestBody,
                                    @Part("long") long: okhttp3.RequestBody,
                                       @Part("company_type") company_type: okhttp3.RequestBody,
                                       @Part("specialize_in") specialize_in: okhttp3.RequestBody,

    ): Call<Datas>


//    @Multipart
//    @POST("users/{id}/user_photos")
//    fun uploadPhoto(
//        @Path("id") userId: Int,
//        @PartMap params: Map<String?, RequestBody?>?
//    ): Call<models.UploadResponse?>?

    @Multipart
    @POST("vehicle_sell.php")
    fun apiSellVehicleWithImage(@Part file1: MultipartBody.Part?,
                                @Part file2: MultipartBody.Part?,
                                @Part file3: MultipartBody.Part?,
                                @Part file4: MultipartBody.Part?,
                                @Part file5: MultipartBody.Part?,
                                @Part file6: MultipartBody.Part?,
                                @Part file7: MultipartBody.Part?,
                                    @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
                                    @Part("user_id") user_id: okhttp3.RequestBody,
                                    @Part("user_type") user_type: okhttp3.RequestBody,
                                    @Part("vehicle_cat") vehicle_cat: okhttp3.RequestBody,
                                    @Part("vehicle_brand") vehicle_brand: okhttp3.RequestBody,
                                    @Part("vehicle_type") vehicle_type: okhttp3.RequestBody,
                                    @Part("vehicle_model") vehicle_model: okhttp3.RequestBody,
                                    @Part("vehicle_year") vehicle_year: okhttp3.RequestBody,
                                    @Part("vehicle_fuel") vehicle_fuel: okhttp3.RequestBody,
                                    @Part("transmission") transmission: okhttp3.RequestBody,
                                    @Part("driven_km") driven_km: okhttp3.RequestBody,
                                    @Part("title") title: okhttp3.RequestBody,
                                    @Part("owners") owners: okhttp3.RequestBody,
                                    @Part("location_longitude") location_longitude: okhttp3.RequestBody,
                                    @Part("contact_number") contact_number: okhttp3.RequestBody,
                                    @Part("location_latitude") location_latitude: okhttp3.RequestBody,
                                    @Part("price") price: okhttp3.RequestBody,
                                    @Part("description") description: okhttp3.RequestBody,
                                @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody


    ): Call<Datas>


    @Multipart
    @POST("create_offer.php")
    fun apiCreateOfferWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part file7: MultipartBody.Part?,
        @Part("title") title: okhttp3.RequestBody,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("name") name: okhttp3.RequestBody,
        @Part("product_id") product_id: okhttp3.RequestBody,
        @Part("offer_price") offer_price: okhttp3.RequestBody,
        @Part("original_price") original_price: okhttp3.RequestBody,
        @Part("start_date") start_date: okhttp3.RequestBody,
        @Part("end_date") end_date: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("seller_category_id") seller_category_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,

        ): Call<Datas>


    @Headers("Content-Type: application/json")
    @POST("update_offer.php")
    fun apiUpdateOffer(@Body offer: Offer): Call<Datas>


    @Multipart
    @POST("insert_product.php")
    fun apiAddSparePartsWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("vehicle_category") vehicle_category: okhttp3.RequestBody,
        @Part("vehicle_company") vehicle_company: okhttp3.RequestBody,
        @Part("vehicle_model_type") vehicle_model_type: okhttp3.RequestBody,
        @Part("vehicle_model_name") vehicle_model_name: okhttp3.RequestBody,
        @Part("product_name") product_name: okhttp3.RequestBody,
        @Part("serial_number") serial_number: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("product_condition") product_condition: okhttp3.RequestBody,

        ): Call<Datas>


    @Multipart
    @POST("insert_product.php")
    fun apiInsuranceProductWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("vehicle_category") vehicle_category: okhttp3.RequestBody,
        @Part("vehicle_company") vehicle_company: okhttp3.RequestBody,
        @Part("vehicle_model_type") vehicle_model_type: okhttp3.RequestBody,
        @Part("vehicle_model_name") vehicle_model_name: okhttp3.RequestBody,
        @Part("vehicle_year") vehicle_year: okhttp3.RequestBody,
        @Part("product_name") product_name: okhttp3.RequestBody,
        @Part("serial_number") serial_number: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("product_condition") product_condition: okhttp3.RequestBody,

        ): Call<Datas>


    @Multipart
    @POST("insert_product.php")
    fun apiAddGarageServiceWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("vehicle_category") vehicle_category: okhttp3.RequestBody,
        @Part("vehicle_company") vehicle_company: okhttp3.RequestBody,
        @Part("product_name") product_name: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("specialize_in") specialize_in: okhttp3.RequestBody,

        ): Call<Datas>


    @Multipart
    @POST("insert_product.php")
    fun apiAddCourierProductWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("product_name") product_name: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody
        ): Call<Datas>



    @Headers("Content-Type: application/json")
    @POST("product_list.php")
    fun apiGetProductList(@Body spareParts: SpareParts): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("rfq_list.php")
    fun apiRFQList(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("rating_list.php")
    fun apiGetRatings(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("update_rating.php")
    fun apiSubmitRatingReply(@Body ratings: Ratings): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("push_notification.php")
    fun apiSendNotificationToUser(@Body notificationSendData: NotificationSendData): Call<Datas>


    @Headers("Content-Type: application/json")
    @POST("insert_rfq_response.php")
    fun apiSendNotificationForRFQResponse(@Body notificationSendData: NotificationSendData): Call<Datas>


    @Multipart
    @POST("notification_send.php")
    fun apiAddNotificationWithFile(
        @Part file: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("message") message: okhttp3.RequestBody,

        ): Call<Datas>


    @Multipart
    @POST("notification_send.php")
    fun apiAddNotificationWithoutFile(
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("message") message: okhttp3.RequestBody,
        ): Call<Datas>


    @Headers("Content-Type: application/json")
    @POST("notification_list.php")
    fun apiGetCategoryNotification(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("notification_admin.php")
    fun apiGetAdminNotification(@Body login: Login): Call<Datas>

    @Multipart
    @POST("insert_product.php")
    fun apiAddTyreServiceWithImage(
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?,
        @Part file6: MultipartBody.Part?,
        @Part("seller_id") seller_id: okhttp3.RequestBody,
        @Part("seller_company_id") seller_company_id: okhttp3.RequestBody,
        @Part("package_purchased_id") package_purchased_id: okhttp3.RequestBody,
        @Part("master_category_id") master_category_id: okhttp3.RequestBody,
        @Part("vehicle_category") vehicle_category: okhttp3.RequestBody,
        @Part("vehicle_company") vehicle_company: okhttp3.RequestBody,
        @Part("vehicle_model_type") vehicle_model_type: okhttp3.RequestBody,
        @Part("vehicle_model_name") vehicle_model_name: okhttp3.RequestBody,
        @Part("product_name") product_name: okhttp3.RequestBody,
        @Part("serial_number") serial_number: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("product_condition") product_condition: okhttp3.RequestBody,
        @Part("tyre_width") tyre_width: okhttp3.RequestBody,
        @Part("rim_diameter") rim_diameter: okhttp3.RequestBody,
        @Part("aspect_ratio") aspect_ratio: okhttp3.RequestBody,
        @Part("load_index") load_index: okhttp3.RequestBody,
        @Part("speed_index") speed_index: okhttp3.RequestBody,
        @Part("tyre_size") tyre_size: okhttp3.RequestBody,
        @Part("is_tubeless") is_tubeless: okhttp3.RequestBody,

        ): Call<Datas>




    @Headers("Content-Type: application/json")
    @POST("company_registration.php")
    fun apiCompanyRegister(@Body companyRegister: CompanyRegister): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("insert_company_package.php")
    fun apiCompanyPackage(@Body companyRegister: CompanyRegister): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("package_subscription.php")
    fun apiSelectPackageSubscription(@Body packageSubscriptionRequest: PackageSubscriptionRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("insert_custom_company_package.php")
    fun apiSelectCustomPackageSubscription(@Body packageSubscriptionRequest: PackageSubscriptionRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("insert_quotation.php")
    fun apiAddQuotation(@Body addQuotation: QuotationRequest): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("quotation_list.php")
    fun apiQuotationListOrDetail(@Body login: Login): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("update_product.php")
    fun apiUpdateProduct(@Body spareParts: SpareParts): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("delete_product_offer_vehicle.php")
    fun apiDeleteProductOffer(@Body productList: ProductList): Call<Datas>

    @Headers("Content-Type: application/json")
    @POST("courier_list.php")
    fun apiGetCourierList(@Body productList: ProductList): Call<Datas>

    @Multipart
    @POST("Service/saveFile")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("seller_id") seller_id: RequestBody?,
        @Part("BackupType") BackupType: RequestBody?,
        @Part("ProductName") ProductName: RequestBody?,
        @Part("AppVersionAppVersion") AppVersionAppVersion: RequestBody?,
        @Part("AppType") AppType: RequestBody?,
        @Part("IMEINumber") IMEINumber: RequestBody?,
        @Part("DeviceInfo") DeviceInfo: RequestBody?,
        @Part("Remark") Remark: RequestBody?,
        @Part("FileName") FileName: RequestBody?,
        @Part("Extentsion") Extentsion: RequestBody?,
    ): Call<Datas>




}
