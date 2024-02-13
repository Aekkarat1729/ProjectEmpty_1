package com.example.projectempty


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainStart : AppCompatActivity() {
    var main_button_login: Button? = null
    var main_button_create: Button? = null
    var mAuth: FirebaseAuth? = null
    private val TAG: String = "Main Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_start)
        init()

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null) {
            Log.d(TAG, "Continue with: " + mAuth!!.currentUser!!.email)
            // เป็นการสังให้ทําการ start activity ส่วนของหน้า MainActivity
            startActivity(
                Intent(
                    this@MainStart,MainActivity::class.java
                )
            )
            finish()
        }
        main_button_login?.setOnClickListener {
            var intent = Intent(this, MainLogin::class.java)
            startActivity(intent)
        }
        main_button_create?.setOnClickListener {
            var intent = Intent(this, MainRegister::class.java)
            startActivity(intent)
        }
    }
    fun init(){
        main_button_login = findViewById(R.id.main_button_login)
        main_button_create = findViewById(R.id.main_button_create)
    }
}