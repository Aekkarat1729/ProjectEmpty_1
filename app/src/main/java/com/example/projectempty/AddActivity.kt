package com.example.projectempty

import android.app.ListActivity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddActivity : AppCompatActivity() {
    lateinit var buttonUpload:Button
    lateinit var buttonSubmit:Button
    lateinit var addTitle:TextView
    lateinit var addDetail:TextView
    lateinit var previewimage:ImageView
    lateinit var storageReference: StorageReference
    lateinit var checkBox_Sad:CheckBox
    lateinit var checkBox_Love:CheckBox
    lateinit var checkBox_Happy:CheckBox

    var mAuth: FirebaseAuth? = null

    var PICK_IMAGE_REQUEST = 111
    var filePath: Uri? = null
    var progressDialog:ProgressDialog? = null
    private var imageName:String? =null

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add)

        buttonUpload = findViewById(R.id.buttonUpload)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        addTitle = findViewById(R.id.addTitle)
        addDetail = findViewById(R.id.addDetail)
        previewimage = findViewById(R.id.previewimage)
        checkBox_Sad = findViewById(R.id.checkBox_sad)
        checkBox_Love = findViewById(R.id.checkBox_love)
        checkBox_Happy = findViewById(R.id.checkBox_happy)
        mAuth = FirebaseAuth.getInstance()


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReferenceFromUrl("gs://emptyproject-52591.appspot.com/")


        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading....")
        buttonUpload!!.setOnClickListener{
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent,"Select Image"),
                PICK_IMAGE_REQUEST
            )
        }

        buttonSubmit.setOnClickListener(object : DialogInterface.OnClickListener,
            View.OnClickListener{
            override fun onClick(v: View?) {
                if(filePath != null){
                    progressDialog!!.show()
                    imageName = "${UUID.randomUUID()}.jpg"
                    val childRef: StorageReference = firebaseStorage.reference.child(imageName.toString())
                    val uploadTask = childRef.putFile(filePath!!)
                    uploadTask.addOnSuccessListener {
                        progressDialog!!.dismiss()
                        submitData()
                    }.addOnFailureListener{e->
                        progressDialog!!.dismiss()
                        Toast.makeText(this@AddActivity,"Upload Fail -> $e",Toast.LENGTH_SHORT).show()


                    }
                }
            }

            override fun onClick(dialog: DialogInterface?, which: Int) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun submitData() {
        if (TextUtils.isEmpty(addTitle.text)) {
            Toast.makeText(this@AddActivity, "Please Enter your Title..", Toast.LENGTH_SHORT).show()
        } else {
            // ข้อมูลไม่ว่าง
            val user = mAuth!!.currentUser
            val tempMail: String = user?.email.toString().replace(".", "")
            val databaseReference = firebaseDatabase.reference.child("Account").child(tempMail).child("Posts").push()
            val tempreference:String = databaseReference.key.toString().trim()
            val databaseReferenceHome = firebaseDatabase.reference.child("home").child(tempreference)

            if (checkBox_Sad.isChecked) {
                val sadRef = firebaseDatabase.reference.child("Sad").child(tempreference)
                sadRef.child("key").setValue(tempreference)
                sadRef.child("title").setValue(addTitle.text.toString())
                sadRef.child("detail").setValue(addDetail.text.toString())
                sadRef.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/$imageName?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
                sadRef.child("email").setValue(tempMail)
            }

            if (checkBox_Happy.isChecked) {
                val happyRef = firebaseDatabase.reference.child("Happy").child(tempreference)
                happyRef.child("key").setValue(tempreference)
                happyRef.child("title").setValue(addTitle.text.toString())
                happyRef.child("detail").setValue(addDetail.text.toString())
                happyRef.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/$imageName?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
                happyRef.child("email").setValue(tempMail)
            }

            if (checkBox_Love.isChecked) {
                val loveRef = firebaseDatabase.reference.child("Love").child(tempreference)
                loveRef.child("key").setValue(tempreference)
                loveRef.child("title").setValue(addTitle.text.toString())
                loveRef.child("detail").setValue(addDetail.text.toString())
                loveRef.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/$imageName?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
                loveRef.child("email").setValue(tempMail)
            }

            databaseReference.child("key").setValue(databaseReference.key)
            databaseReference.child("title").setValue(addTitle.text.toString())
            databaseReference.child("detail").setValue(addDetail.text.toString())
            databaseReference.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/$imageName?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
            databaseReference.child("email").setValue(tempMail)

            databaseReferenceHome.child("key").setValue(tempreference)
            databaseReferenceHome.child("title").setValue(addTitle.text.toString())
            databaseReferenceHome.child("detail").setValue(addDetail.text.toString())
            databaseReferenceHome.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/$imageName?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
            databaseReferenceHome.child("email").setValue(tempMail)

            Toast.makeText(this@AddActivity, "Upload Finish", Toast.LENGTH_SHORT).show()
            val intentMain = Intent(this, ListActivity::class.java)
            startActivity(intentMain)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null)
            filePath = data.data
        try{
            val bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,filePath
            )
            previewimage!!.setImageBitmap(bitmap)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }


}
