package com.example.projectempty


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainStart : AppCompatActivity() {
    var main_button_login: Button? = null
    var main_button_create: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_start)
        init()
        main_button_login?.setOnClickListener {
            var intent = Intent(this,MainLogin::class.java)
            startActivity(intent)
        }
        main_button_create?.setOnClickListener {
            var intent = Intent(this,MainRegister::class.java)
            startActivity(intent)
        }
    }
    fun init(){
        main_button_login = findViewById(R.id.main_button_login)
        main_button_create = findViewById(R.id.main_button_create)
    }
}