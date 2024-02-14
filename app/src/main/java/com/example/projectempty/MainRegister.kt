package com.example.projectempty

import android.app.AlertDialog
import android.app.ListActivity
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainRegister : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    private val TAG: String = "Register Activity"

    var register_button_back:ImageButton? = null
    var register_button_account:Button? = null
    var register_button_login:Button? = null
    var register_edit_user:EditText? = null
    var register_edit_name:EditText? = null
    var register_edit_email:EditText? = null
    var register_edit_password:EditText? = null
    var register_edit_repassword:EditText? = null
    var register_text:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_register)
        init()
        mAuth = FirebaseAuth.getInstance()

        register_button_account?.setOnClickListener {
            val user:String = register_edit_user!!.text.toString().trim()
            val pass:String = register_edit_password!!.text.toString().trim()
            val name:String = register_edit_name!!.text.toString().trim()
            val email:String = register_edit_email!!.text.toString().trim()
            val database = Firebase.database

//            if(user.isEmpty()){
//                register_edit_user?.setError("Please Enter Your User")
//            }
//            if(pass.isEmpty()){
//                register_edit_user?.setError("Please Enter Your Password")
//            }
//            if(email.isEmpty()){
//                register_edit_user?.setError("Please enter Your Email")
//            }
//            if(name.isEmpty()){
//                register_edit_user?.setError("Please enter Your Name")
//            }

            validateUser()
            validateEmail()
            validatePassword()
            validateCpassword()
            validateName()
            if(validateUser() == true && validateEmail() == true && validatePassword() == true && validateCpassword() == true && validateName() == true){
                var tempMail:String = email
                val mark = '.'
                var newMail = ""
                for (char in tempMail){
                    if(char != mark){
                        newMail += char
                    }
                }
//                register_text?.setText(newMail)

                val databaseReference =  database.reference.child("Account").child(newMail)
                databaseReference.child("User name").setValue(user)
                databaseReference.child("Password").setValue(pass)
                databaseReference.child("Email").setValue(email)
                databaseReference.child("Full name").setValue(name)
                Toast.makeText(this,"Create account successfully!",Toast.LENGTH_LONG).show()
                Log.d(TAG, "Create account successfully!")
                if (mAuth!!.currentUser != null) {
                    startActivity(Intent(this@MainRegister,
                        MainActivity::class.java))
                    finish()
                }
            }

            if(validateEmail() == true && validatePassword() == true){
                createEmail()
            }
//            val email = register_edit_email?.text.toString().trim { it <= ' ' }
//            val password = register_edit_password?.text.toString().trim { it <= ' ' }
//
//            //ทําการตรวจสอบก่อนว่ามีข้อมูลหรือไม่
//            if (email.isEmpty()) {
//                Toast.makeText(this,"Please enter your email address.", Toast.LENGTH_LONG).show()
//                Log.d(TAG, "Email was empty!")
//                return@setOnClickListener
//            }
//            if (password.isEmpty()) {
//                Toast.makeText(this,"Please enter your password.",Toast.LENGTH_LONG).show()
//                Log.d(TAG, "Password was empty!")
//                return@setOnClickListener
//            }
//            //กรณีทีข้อมูลจะทําการตรวจสอบเงือนไขอืน ๆ ก่อนทําการ create user
//            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    if (password.length < 6) { // ตรวจสอบความยาวของ password
//                        Toast.makeText(this,"Password too short! Please enter minimum 6 characters.",Toast.LENGTH_LONG).show()
//                                Log.d(TAG, "Enter password less than 6 characters.")
//                    } else {
//                        Toast.makeText(this,"Authentication Failed: " +
//                                task.exception!!.message,Toast.LENGTH_LONG).show()
//                        Log.d(TAG, "Authentication Failed: " +
//                                task.exception!!.message)
//                    }
//                } else {
//                    Toast.makeText(this,"Create account successfully!",Toast.LENGTH_LONG).show()
//                            Log.d(TAG, "Create account successfully!")
//                    startActivity(Intent(this@MainRegister, MainActivity::class.java))
//                    finish()
//                }
//            }
        }

        register_button_back?.setOnClickListener { onBackPressed() }


        register_button_login?.setOnClickListener {
            var intent = Intent(this,MainLogin::class.java)
            startActivity(intent)
        }

   // override fun OnStart(){
   //     super.onStart()
   //     val currentUser = mAuth.currentUser
   //     updateUi(currentUser)
   // }

    }

    private fun updateUi(user: FirebaseUser?){
        if (user != null){
            val uid = user.uid
            val email = user.email
            Toast.makeText(
                this@MainRegister,
                "Welcome: $email, Your ID is: $uid",
                Toast.LENGTH_SHORT
            ).show()
            val intentSession = Intent(this, ListActivity::class.java)
            startActivity(intentSession)
        }
    }

    private fun createEmail():Boolean{
        val pass:String = register_edit_password!!.text.toString().trim()
        val email:String = register_edit_email!!.text.toString().trim()

        mAuth!!.createUserWithEmailAndPassword(email,
            pass)
        return true
    }
    private fun validateName():Boolean{
        var user = register_edit_name?.text.toString().trim()
        if(user.length > 6){
            return true
        }else{
            register_edit_name?.setError("กรอกให้มากกว่า 6 ตัวอักษร")
            return false
        }
    }
    private fun validateUser():Boolean{
        var user = register_edit_user?.text.toString().trim()
        if(user.length >= 5 && user.length <= 20){
            return true
        }else{
            register_edit_user?.setError("กรอกให้มากกว่า 5 ตัวอักษรแต่ไม่เกิน 20 ตัวอักษร")
            return false
        }
    }
    private fun validateCpassword():Boolean{
        val uPass = register_edit_password?.text.toString().trim()
        val cPass = register_edit_repassword?.text.toString().trim()
        if(uPass != cPass){
            register_edit_repassword?.setError("รหัสไม่ตรงกัน")
            return false
        }else if(cPass.isEmpty()){
            register_edit_repassword?.setError("field can not be empty")
            return false
        }else{
            return true
        }
    }
    private  fun validatePassword():Boolean{
        val uPass = register_edit_password!!.text.toString().trim()
        return if(uPass.isEmpty()){
            register_edit_password?.setError("field can not be empty")
            return false
        }else if(uPass.length < 6){
            register_edit_password?.setError("ความยาวต้องมากกว่า 6 ตัว")
            return false
        }else{
            register_edit_password?.setError(null)
            return true
        }
    }
    private fun validateEmail():Boolean{
        val dialogBuilder = AlertDialog.Builder(this)
        val userEmail = register_edit_email?.text.toString().trim()
        val checkEmail1 = "[a-zA-Z0-9._-]+@gmail.com"
        val checkEmail2 = "[a-zA-Z0-9._-]+@hotmail.th"
        val checkEmail3 = "[a-zA-Z0-9._-]+@kkumail.com"
        if(userEmail.isEmpty()){
            register_edit_email?.setError("Field can not be empty")
            return false
        }else if(!userEmail.matches(checkEmail1.toRegex()) && !userEmail.matches(checkEmail2.toRegex()) && !userEmail.matches(checkEmail3.toRegex())){
            register_edit_email?.setError("Invalid Email!")
            return false
        }else{
            return true
        }
    }
    fun init(){
        register_button_account = findViewById(R.id.register_button_account)
        register_button_back = findViewById(R.id.register_button_back)
        register_button_login = findViewById(R.id.register_button_login)
        register_edit_user = findViewById(R.id.register_edit_user)
        register_edit_name = findViewById(R.id.register_edit_name)
        register_edit_email = findViewById(R.id.register_edit_email)
        register_edit_password = findViewById(R.id.register_edit_password)
        register_edit_repassword = findViewById(R.id.register_edit_repassword)
        register_text = findViewById(R.id.register_text)
    }
}