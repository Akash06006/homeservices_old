package com.uniongoods.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.viewmodels.ratingreviews.RatingReviewsViewModel
import com.example.services.views.home.DashboardActivity
import com.example.services.views.home.LandingMainActivity
import com.example.services.views.ratingreviews.AddRatingReviewsListActivity
import com.example.services.views.subcategories.ServicesListActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    //*
    //* Called when message is received.
    //*
    //* @param remoteMessage Object representing the message received from Firebase Cloud Messaging.

    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        remoteMessage.notification
        Log.d("Notification--", "Received")
        sendNotification(
            data.get("body").toString(),
            data.get("status").toString(),
            data.get("message").toString()
        )
//notificationType
        //data.get("Id").toString()
    }


    override fun onNewToken(token: String) {
        SharedPrefClass()!!.putObject(
            applicationContext,
            GlobalConstants.NOTIFICATION_TOKEN,
            token
        )
        Log.d("token", token + "")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    //* @param messageBody FCM message body received.
    private fun sendNotification(messageBody: String, notificationType: String, message: String) {

        val intent = Intent(this, LandingMainActivity::class.java)
        // intent.putExtra("from", "notification")
        /* if (notificationType.equals("Job")) {
             intent = Intent(this, DashboardActivity::class.java)
         } else {
             intent = Intent(this, ServicesListActivity::class.java)
         }*/

        GlobalConstants.ORDERID = notificationType
        intent.putExtra("from", "notification")
        intent.putExtra(
            "orderId"/*"status"*/,
            "6ad3c62c-17dd-4bb1-97c5-e757cd2f4ff3"/*notificationType*/
        )

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        val pendingIntent = PendingIntent.getActivity(
            this, 0/*Request code*/, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        /*  val notificationBuilder = NotificationCompat.Builder(this, channelId)
              .setSmallIcon(R.drawable.ic_app)
              .setLargeIcon(
                  BitmapFactory.decodeResource(
                      this.getResources(),
                      R.drawable.ic_app
                  )
              )
              .setContentTitle(getString(R.string.app_name))
              .setContentText(message)
              .setAutoCancel(true)
              .setSound(defaultSoundUri)
              .setContentIntent(pendingIntent)*/

        val notificationBuilder: NotificationCompat.Builder?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.fm_hut)
                .setContentTitle(getString(R.string.app_name))
                .setChannelId(channelId)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.FLAG_HIGH_PRIORITY)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )

            /*val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    packageName,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }*/
        } else {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.fm_hut)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /*ID of notification*/, notificationBuilder.build())
    }
}

//* @param messageBody FCM message body received.
/*private fun sendNotification(messageBody: String) {
    val intent = Intent(this, DashboardActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(this, 0Request code, intent,
            PendingIntent.FLAG_ONE_SHOT)

    val channelId = getString(R.string.app_name)
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }
    notificationManager.notify(0 ID of notification, notificationBuilder.build())
}*/



