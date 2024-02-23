package com.example.projectempty

import android.annotation.SuppressLint
import android.app.ListActivity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class homefeed : AppCompatActivity() {

    var homefeed_button_editprofile: Button? = null
    val myref = Firebase.database.reference
    lateinit var RecyclerViewhomefeed: RecyclerView
    lateinit var databaseReferencehomefeed: DatabaseReference
    lateinit var responsehome:MutableList<MphotoModel>
    private var homeAdapter: homeAdapter? = null
    lateinit var fab: FloatingActionButton
    var mAuth: FirebaseAuth? = null
    lateinit var database: FirebaseDatabase
    lateinit var profile: ImageView

    var PICK_IMAGE_REQUEST = 111
    var filePath: Uri? = null
    var progressDialog: ProgressDialog? = null
    private var imageName:String? =null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseStorage: FirebaseStorage

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homefeed)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        RecyclerViewhomefeed = findViewById<RecyclerView>(R.id.RecyclerView_homefeed)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        init()

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading....")
        homefeed_button_editprofile!!.setOnClickListener{
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(
                Intent.createChooser(intent,"Select Image"),
                PICK_IMAGE_REQUEST
            )

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
                        Toast.makeText(this@homefeed,"Upload Fail -> $e", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        //home photo database นะครับเตง
        //RecyclerViewhome.layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL, false)
        RecyclerViewhomefeed.layoutManager = GridLayoutManager(this@homefeed,2)
        databaseReferencehomefeed = database.getReference("home")
        responsehome = mutableListOf()
        homeAdapter = homeAdapter(responsehome as ArrayList<MphotoModel>)
        RecyclerViewhomefeed.adapter = homeAdapter

        onBindingFirebase()
    }

    private fun onBindingFirebase() {
        databaseReferencehomefeed.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@homefeed.responsehome.add(snapshot.getValue(MphotoModel::class.java)!!)
                homeAdapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun init(){
        homefeed_button_editprofile = findViewById(R.id.homefeed_button_editprofile)
        profile = findViewById(R.id.homefeed_image_profile)
    }

    private fun submitData() {
        val databaseReference = firebaseDatabase.reference.child("home").push()
        databaseReference.child("key").setValue(databaseReference.key)
        // เพิ่ม URL ของรูปภาพลงใน Firebase Realtime Database
        databaseReference.child("Image").setValue("https://firebasestorage.googleapis.com/v0/b/emptyproject-52591.appspot.com/o/"+imageName+"?alt=media&token=6225469d-2231-4898-a986-a2e2d6a1cc96")
        Toast.makeText(this@homefeed,"Upload Finish",Toast.LENGTH_SHORT).show()
        val intentMain = Intent(this, ListActivity::class.java)
        startActivity(intentMain)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null)
            filePath = data.data
        try{
            val bitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,filePath
            )
            profile!!.setImageBitmap(bitmap)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

}