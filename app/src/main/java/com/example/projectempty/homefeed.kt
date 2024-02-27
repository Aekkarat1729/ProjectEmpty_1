package com.example.projectempty

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class homefeed : AppCompatActivity() {

    private var homefeed_button_editprofile: Button? = null
    private lateinit var RecyclerViewhomefeed: RecyclerView
    private lateinit var databaseReferencehomefeed: DatabaseReference
    private lateinit var responsehome: MutableList<MphotoModel>
    private var homeAdapter: homeAdapter? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var profile: ImageView

    private val PICK_IMAGE_REQUEST = 111
    private var filePath: Uri? = null
    private var progressDialog: ProgressDialog? = null
    private var imageName: String? = null
    private lateinit var firebaseStorage: FirebaseStorage
    lateinit var fab: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homefeed)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        initViews()

        //+
        fab.setOnClickListener{
            val intent = Intent(this@homefeed,AddActivity::class.java)
            startActivity(intent)
        }

        // สร้าง DatabaseReference ของ Firebase Realtime Database
        val user = mAuth!!.currentUser
        val tempMail:String = user?.email.toString().replace(".", "") // ทำการลบจุดออก
        val databaseReference = database.reference.child("Account").child(tempMail).child("Profile")

        // เพิ่ม ValueEventListener เพื่อดึง URL ของรูปภาพจาก Realtime Database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.getValue(String::class.java)
                if (!imageUrl.isNullOrEmpty()) {
                    // โหลดรูปภาพจาก URL ด้วย Glide หรือ Picasso หรือวิธีอื่นๆ
                    Glide.with(this@homefeed)
                        .load(imageUrl)
                        .into(profile)
                } else {
                    // ถ้าไม่มี URL ของรูปภาพ ให้ทำการกำหนดรูปภาพเริ่มต้นหรือตัวแทน
                    profile.setImageResource(R.drawable.profile) // เปลี่ยน placeholder_image เป็นรูปภาพที่คุณต้องการให้แสดงเมื่อไม่มีรูปใน Realtime Database
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // กรณีเกิดข้อผิดพลาดในการอ่านค่าจาก Realtime Database
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })


        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading....")

        homefeed_button_editprofile!!.setOnClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

        val user1 = mAuth!!.currentUser
        var tempMail1:String = user1?.email.toString()
        val mark = '.'
        var newMail = ""
        for (char in tempMail1){
            if(char != mark){
                newMail += char
            }
        }

        RecyclerViewhomefeed.layoutManager = GridLayoutManager(this@homefeed, 2)
        databaseReferencehomefeed = database.getReference("Account").child(newMail).child("Posts")
        responsehome = mutableListOf()
        homeAdapter = homeAdapter(responsehome as ArrayList<MphotoModel>)
        RecyclerViewhomefeed.adapter = homeAdapter

        onBindingFirebase()
    }

    private fun initViews() {
        homefeed_button_editprofile = findViewById(R.id.homefeed_button_editprofile)
        RecyclerViewhomefeed = findViewById(R.id.RecyclerView_homefeed)
        profile = findViewById(R.id.homefeed_image_profile)
        fab = findViewById(R.id.fab)
    }

    private fun onBindingFirebase() {
        databaseReferencehomefeed.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                responsehome.add(snapshot.getValue(MphotoModel::class.java)!!)
                homeAdapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profile.setImageBitmap(bitmap)

                uploadImageToFirebaseStorage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImageToFirebaseStorage() {
        progressDialog!!.show()
        imageName = "${UUID.randomUUID()}.jpg"
        val storageRef: StorageReference = firebaseStorage.reference.child("images/$imageName")
        val uploadTask = storageRef.putFile(filePath!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
                progressDialog!!.dismiss()
                Toast.makeText(this@homefeed, "Upload Success", Toast.LENGTH_SHORT).show()

                // Call submitData() to add data to Firebase Realtime Database
                submitData(downloadUrl.toString())
            } else {
                progressDialog!!.dismiss()
                Toast.makeText(this@homefeed, "Upload Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitData(downloadUrl: String) {
        val user = mAuth!!.currentUser
        var tempMail:String = user?.email.toString()
        val mark = '.'
        var newMail = ""
        for (char in tempMail){
            if(char != mark){
                newMail += char
            }
        }
        val databaseReference = database.reference.child("Account").child(newMail)

        // Add image URL to Firebase Realtime Database
        databaseReference.child("Profile").setValue(downloadUrl)
            .addOnSuccessListener {
                Toast.makeText(this@homefeed, "Data added to Realtime Database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@homefeed, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }
}
