package com.example.projectempty

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.database.FirebaseDatabase

class MainRegister : AppCompatActivity() {
    var register_button_back:ImageButton? = null
    var register_button_account:Button? = null
    var register_button_login:Button? = null
    var register_edit_user:EditText? = null
    var register_edit_name:EditText? = null
    var register_edit_email:EditText? = null
    var register_edit_password:EditText? = null
    var register_edit_repassword:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        init()
        register_button_back?.setOnClickListener {
            var intent = Intent(this,MainStart::class.java)
            startActivity(intent)
        }
        register_button_login?.setOnClickListener {
            var intent = Intent(this,MainLogin::class.java)
            startActivity(intent)
        }
        register_button_account?.setOnClickListener{
//            var user = register_edit_user.toString().trim()
//            var name = register_edit_name.toString().trim()
//            var email = register_edit_email.toString().trim()
//            var pass = register_edit_password.toString().trim()
//            var repass = register_edit_repassword.toString().trim()

        }
    }
    fun init(){
        register_button_account = findViewById(R.id.register_button_account)
        register_button_back = findViewById(R.id.register_button_back)
        register_button_login = findViewById(R.id.register_button_login)
        register_edit_user = findViewById(R.id.reregister_edit_user)
        register_edit_name = findViewById(R.id.register_edit_name)
        register_edit_email = findViewById(R.id.register_edit_email)
        register_edit_password = findViewById(R.id.register_edit_password)
        register_edit_repassword = findViewById(R.id.register_edit_repassword)
    }
}