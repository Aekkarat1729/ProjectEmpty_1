package com.example.projectempty.ui.hot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.MphotoModel
import com.example.projectempty.R
import com.example.projectempty.homeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HotFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.

    lateinit var databaseReferenceheart: DatabaseReference
    private var homeAdapter: homeAdapter? = null
    lateinit var RecyclerViewheart: RecyclerView
    lateinit var responseheart:MutableList<MphotoModel>
    lateinit var database: FirebaseDatabase
    lateinit var hot_text_wellcome: TextView
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_hot, container, false)
        RecyclerViewheart = view.findViewById(R.id.RecyclerView_hot)
        hot_text_wellcome = view.findViewById(R.id.hot_text_wellcome)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        val myref = Firebase.database.reference
        val tempMailUser:String = user?.email.toString().replace(".", "") // ทำการลบจุดออก

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
                val value = dataSnapshot.getValue(String::class.java)
                Log.d(HotFragment.TAG, "Value is: $value")

                hot_text_wellcome?.setText("Hello "+value+".")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(HotFragment.TAG, "Failed to read value.", databaseError.toException())
            }
        }
        // ดึงข้อมูลแบบ Realtime
        myref.child("Account")
            .child(tempMailUser)
            .child("User name").addValueEventListener(postListener)

        database = FirebaseDatabase.getInstance()

        RecyclerViewheart.layoutManager = GridLayoutManager(context,2)
        databaseReferenceheart = database.getReference("Hot")
        responseheart = mutableListOf()
        homeAdapter = homeAdapter(responseheart as ArrayList<MphotoModel>)
        RecyclerViewheart.adapter = homeAdapter

        onBindingFirebase()
        return view
    }
    private fun onBindingFirebase() {
        databaseReferenceheart.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@HotFragment.responseheart.add(snapshot.getValue(MphotoModel::class.java)!!)
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

//        val textView: TextView = binding.textHot
//        hotViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
    }
    companion object {
        private const val TAG = "HotFragment"
    }

}