package com.clonecoding.clouldmessagealarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

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
    }
}