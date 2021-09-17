package com.clonecoding.clouldmessagealarm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    /**
     * Result text view
     */
    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    /**
     * Firebase text view
     */
    private val firebaseTextview: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initFirebase()
        this.updateResult()
    }

    /**
     * Firebase 초기화
     */
    private fun initFirebase() {

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    this.firebaseTextview.text = it.result
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {

        this.resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if (isNewIntent) {
                    "(으)로 갱신했습니다."
                } else {
                    "(으)로 실행했습니다."
                }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        this.updateResult(true)
    }
}