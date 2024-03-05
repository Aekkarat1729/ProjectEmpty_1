package com.example.projectempty.ui.profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectempty.MainStart
import com.example.projectempty.R
import com.example.projectempty.homefeed
import com.example.projectempty.language
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private val TAG = "ProfileFragment"

    private var PICK_IMAGE_REQUEST = 111
    private var filePath: Uri? = null
    private var progressDialog: ProgressDialog? = null
    private var imageName: String? = null
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var profile_button_ct:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var profile_button_homefeed: ImageView? = view.findViewById(R.id.profile_button_homefeed)
        var profile_button_information: Button? = view.findViewById<Button>(R.id.profile_button_information)
        var profile_button_ct: Button? = view.findViewById<Button>(R.id.profile_button_ct)

        profile_button_homefeed?.setOnClickListener {
            val intent = Intent(context, homefeed::class.java)
            startActivity(intent)
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Uploading....")

        //ดึงข้อมูล user
        mAuth = FirebaseAuth.getInstance()
        val myref = Firebase.database.reference
        val user = mAuth!!.currentUser
        val database = FirebaseDatabase.getInstance()
        var profile_text_name: TextView? = view.findViewById<TextView>(R.id.profile_text_name)
        var profile_text_email: TextView? = view.findViewById<TextView>(R.id.profile_text_email)

        //name
        val postListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
                profile_text_name?.text = value
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }

        //ลบจุด
        var tempMail1: String = user?.email.toString()
        val mark1 = '.'
        var newMail1 = ""
        for (char in tempMail1) {
            if (char != mark1) {
                newMail1 += char
            }
        }

        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(newMail1)
            .child("Full name").addValueEventListener(postListener1)

        // สร้าง DatabaseReference ของ Firebase Realtime Database
        val tempMail: String = user?.email.toString().replace(".", "") // ทำการลบจุดออก
        val databaseReference = database.reference.child("Account").child(tempMail).child("Profile")

        // เพิ่ม ValueEventListener เพื่อดึง URL ของรูปภาพจาก Realtime Database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.getValue(String::class.java)
                if (!imageUrl.isNullOrEmpty()) {
                    // โหลดรูปภาพจาก URL ด้วย Glide หรือ Picasso หรือวิธีอื่นๆ
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .into(profile_button_homefeed!!)
                } else {
                    // ถ้าไม่มี URL ของรูปภาพ ให้ทำการกำหนดรูปภาพเริ่มต้นหรือตัวแทน
                    profile_button_homefeed?.setImageResource(R.drawable.profile) // เปลี่ยน placeholder_image เป็นรูปภาพที่คุณต้องการให้แสดงเมื่อไม่มีรูปใน Realtime Database
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // กรณีเกิดข้อผิดพลาดในการอ่านค่าจาก Realtime Database
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        //email
        val postListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $value")
                profile_text_email?.text = value
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }

        //ลบจุด
        var tempMail2: String = user?.email.toString()
        val mark2 = '.'
        var newMail2 = ""
        for (char in tempMail2) {
            if (char != mark2) {
                newMail2 += char
            }
        }

        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(newMail2)
            .child("Email").addValueEventListener(postListener2)

        val signoutButton = view.findViewById<Button>(R.id.profile_button_logout)
        signoutButton.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(), "Signed out!", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Signed out!")
            val intent = Intent(requireActivity(), MainStart::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        profile_button_ct?.setOnClickListener {
            val intent = Intent(requireContext(), language::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }
}
