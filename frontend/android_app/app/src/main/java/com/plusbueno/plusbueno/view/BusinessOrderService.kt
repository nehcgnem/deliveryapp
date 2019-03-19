package com.plusbueno.plusbueno.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.plusbueno.plusbueno.data.ServerOrder
import com.plusbueno.plusbueno.parser.UniversalParser

/**
 * Created by LZMA on 2018/11/25.
 */
class BusinessOrderService : Service() {
    private var latest : Long = -1
    companion object {
        const val CHANNEL_ID = "BusinessChannel"
        const val NEW_ORDER_NOTIFICATION_ID = 1
        const val INTERVAL_MS = 10000
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, "Business Notification", IMPORTANCE_DEFAULT)
            mChannel.description = "Show notifications on new orders."
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

        }
        val task = LoadOrdersTask(this)
        task.execute()

    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(CHANNEL_ID)
    }

    private fun showNotification(message : String) {
        val intent = Intent(this, OrderListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_more)
                .setContentTitle("+Bueno")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NEW_ORDER_NOTIFICATION_ID, mBuilder.build())
        }

    }

    fun onLoad(orders : Array<ServerOrder>?) {
        if (orders != null && !orders.isEmpty()) {
            val last = orders.last().id
            if (latest.compareTo(-1) != 0) {  // latest != -1
                if (latest != last) {  // TODO who can come up with a better solution?
                    latest = last
                    showNotification("New order received.")
                }
            } else {  // latest == -1
                latest = last
            }
        }

        val task = LoadOrdersDelayTask(this)  // this task sleeps before querying server
        task.execute()
    }


    private class LoadOrdersTask(val service : BusinessOrderService) : AsyncTask<String, String, Array<ServerOrder>>() {
        override fun onPostExecute(result: Array<ServerOrder>?) {
            super.onPostExecute(result)
            service.onLoad(result)

        }

        override fun doInBackground(vararg params: String?): Array<ServerOrder>? {
            Log.v("BUSSERVICE", "Loading new orders")
            try {
                return UniversalParser.get(UniversalParser.BASE_URL_RESTFUL, Array<ServerOrder>::class.java)
            } catch (e: Exception) {
                Log.e("BUSSERVICE", e.toString() + e.message)
            }
            return null
        }

    }
    private class LoadOrdersDelayTask(val service : BusinessOrderService) : AsyncTask<String, String, Array<ServerOrder>>() {
        override fun onPostExecute(result: Array<ServerOrder>?) {
            super.onPostExecute(result)
            service.onLoad(result)

        }

        override fun doInBackground(vararg params: String?): Array<ServerOrder>? {
            SystemClock.sleep(INTERVAL_MS.toLong())
            Log.v("BUSSERVICE", "Loading new orders")
            try{
                return UniversalParser.get(UniversalParser.BASE_URL_RESTFUL, Array<ServerOrder>::class.java)
            } catch (e : Exception) {
                Log.e("BUSSERVICE", e.toString() + e.message)
            }
            return null
        }
    }

}