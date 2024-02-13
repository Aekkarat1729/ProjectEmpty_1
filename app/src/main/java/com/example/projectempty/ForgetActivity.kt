package com.example.projectempty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {
    lateinit var textEmailForget:EditText
    lateinit var buttonReset:Button
    lateinit var forgetbuttonback:ImageButton
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_forget)


        mAuth = FirebaseAuth.getInstance()
        textEmailForget = findViewById(R.id.textEmailForget)
        buttonReset = findViewById(R.id.buttonReset)
        forgetbuttonback = findViewById(R.id.forgetbuttonback)



        buttonReset!!.setOnClickListener{
            val email = textEmailForget.text.toString()
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please Enter your Email!!",Toast.LENGTH_SHORT).show()
            }else{
                mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener{
                    task -> if(task.isSuccessful){
                    Toast.makeText(this@ForgetActivity,"Please Check your Email",Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this@ForgetActivity,"Fail to send reset email password",Toast.LENGTH_SHORT).show()

                }
                }
            }
        }

        forgetbuttonback!!.setOnClickListener{
            val gotoForget = Intent(this,MainLogin::class.java)
            startActivity(gotoForget)
        }

    }
}