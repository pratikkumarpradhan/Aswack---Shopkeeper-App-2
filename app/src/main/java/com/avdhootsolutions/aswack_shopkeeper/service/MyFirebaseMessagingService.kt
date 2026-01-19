package com.avdhootsolutions.aswack_shopkeeper.service

import android.annotation.SuppressLint
import android.app.Notification
import android.util.Log

import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.content.ComponentName

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.net.Uri

import android.os.Build
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.activities.BookingListActivity
import com.avdhootsolutions.aswack_shopkeeper.activities.ChatListActivity
import com.avdhootsolutions.aswack_shopkeeper.activities.HomeActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseMessagingService"

    private val CHANNEL_ID = "Bestmarts"
    private val CHANNEL_NAME = "Bestmarts"
    private lateinit var resultIntent : Intent

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FirebaseMessagingService", "Dikirim dari: ${remoteMessage.data.toString()}")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendNotification1(remoteMessage);
        }else{
            sendNotification(remoteMessage);

          //  showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }

    }

    private fun showNotification(title: String?, body: String?) {

        Log.e("titlehihihhi ", title.toString())
        Log.e("bodyhihih ", body.toString())

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = notificationBuilder.build()
        startForeground(0, notification )
        notificationManager.notify(0, notificationBuilder.build())
    }


    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo: List<ActivityManager.RunningTaskInfo> = am.getRunningTasks(1)
            val componentInfo: ComponentName? = taskInfo[0].topActivity
            if (componentInfo?.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }


    @SuppressLint("LongLogTag")
    private fun sendNotification(remoteMessage: RemoteMessage) {
        if (!isAppIsInBackground(applicationContext)) {
            //foreground app
            Log.e("remoteMessage foreground", remoteMessage.data.toString())
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val resultIntent = Intent(applicationContext, HomeActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                applicationContext, CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(10)
                .setTicker("Bestmarts")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
            notificationManager.notify(1, notificationBuilder.build())
        } else {
            Log.e("remoteMessage background", remoteMessage.data.toString())
            val data: Map<*, *> = remoteMessage.data
            val title: String? = data.get("title") as String?
            val body: String? = data.get("body") as String?
            val resultIntent = Intent(applicationContext, HomeActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                applicationContext, CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(10)
                .setTicker("Bestmarts")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    @SuppressLint("NewApi")
    private fun sendNotification1(remoteMessage: RemoteMessage) {
        if (!isAppIsInBackground(applicationContext)) {
            //foreground app
            Log.e("remoteMessage", remoteMessage.data.toString())
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body


            for (key in remoteMessage.data) {
                if (key.key == "screen_type"){
                    when(key.value){
                        "SELLER_GENERAL" ->{
                            resultIntent = Intent(applicationContext, HomeActivity::class.java)
                            break
                        }

                        "SELLER_BOOKING" ->{
                            resultIntent = Intent(applicationContext, BookingListActivity::class.java)
                            break
                        }

                        "SELLER_COMPANY_APPROVAL" ->{
                               resultIntent = Intent(applicationContext, HomeActivity::class.java)
                            break
                        }

                        "SELLER_PACKAGE_EXPIRE" ->{
                            //    resultIntent = Intent(applicationContext, AddNotificationActivity::class.java)
                            break
                        }

                        "CHAT_LIST" -> {

                            Log.e("chatlistttt " , "hiii");

                            resultIntent =
                                Intent(applicationContext, ChatListActivity::class.java)
                            break
                        }

                        else ->{
                            resultIntent =
                                Intent(applicationContext, HomeActivity::class.java)
                            break
                        }


                    }
                }


            }
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val defaultsound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val oreoNotification = OreoNotification(this)
            val builder: Notification.Builder = oreoNotification.getOreoNotification(title,
                body,
                pendingIntent,
                defaultsound,
                java.lang.String.valueOf(R.drawable.ic_launcher_foreground))
            val i = 0
            oreoNotification.manager?.notify(i, builder.build())
        } else {
            Log.e("remoteMessage", remoteMessage.data.toString())
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val resultIntent = Intent(applicationContext, HomeActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val defaultsound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val oreoNotification = OreoNotification(this)
            val builder: Notification.Builder = oreoNotification.getOreoNotification(title,
                body,
                pendingIntent,
                defaultsound,
                java.lang.String.valueOf(R.drawable.ic_launcher_foreground))
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val i = 0
            oreoNotification.manager?.notify(i, builder.build())
        }
    }


}
