package com.example.projectempty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {
    var reset_edittext_password:EditText? = null
    var reset_edittext_newpassword:EditText? = null
    var mAuth: FirebaseAuth? = null
    var texttest:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        init()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        val password = reset_edittext_password?.text.toString().trim()

        mAuth?.createUserWithEmailAndPassword(user.toString(),password)
        if (mAuth!!.currentUser != null) {
            texttest?.setText(user?.email.toString())
        }
    }
    fun init(){
        reset_edittext_newpassword = findViewById(R.id.reset_edittext_newpassword)
        reset_edittext_password = findViewById(R.id.reset_edittext_password)
        texttest = findViewById(R.id.texttest)
    }
}