package com.example.projectempty

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class pf : AppCompatActivity() {
    var logout: Button? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser

        logout?.setOnClickListener {
            mAuth!!.signOut()
            Toast.makeText(this, "Signed out!", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Signed out!")
            startActivity(
                Intent(this@pf, MainLogin::class.java)
            )
            finish()
        }

    }
    init{
        logout = findViewById(R.id.profile_button_logout)
    }
}