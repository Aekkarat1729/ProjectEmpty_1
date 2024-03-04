package com.example.projectempty.ui.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectempty.MphotoModel
import com.example.projectempty.R
import com.example.projectempty.databinding.FragmentHotBinding
import com.example.projectempty.homeAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HeartFragment : Fragment() {
    //heart เดิม
//    private var _binding: FragmentHomeBinding? = null
//    var mAuth: FirebaseAuth? = null
//    var home_text_wellcome: TextView? = null
//    val myref = Firebase.database.reference
//    lateinit var RecyclerViewMphoto:RecyclerView
//    lateinit var RecyclerViewPphoto:RecyclerView
//    lateinit var database: FirebaseDatabase
//    lateinit var databaseReferenceMphoto: DatabaseReference
//    lateinit var databaseReferencePphoto: DatabaseReference
//    lateinit var responseMphoto:MutableList<MphotoModel>
//    lateinit var responsePphoto:MutableList<MphotoModel>
//    private var MphotoAdapter: MphotoAdapter? = null
//    private var PphotoAdapter: PphotoAdapter? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        val view = inflater.inflate(R.layout.fragment_heart, container, false)
//        mAuth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//
//
//        val user = mAuth!!.currentUser
//        home_text_wellcome = view.findViewById<TextView>(R.id.home_text_wellcome)
//
//
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // ข้อมูลที่ดึงมาอยู่ใน dataSnapshot
//                val value = dataSnapshot.getValue(String::class.java)
//                Log.d(TAG, "Value is: $value")
//
//                home_text_wellcome?.setText("Hello "+value+".")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException())
//            }
//        }
//        var tempMail:String = user?.email.toString()
//        val mark = '.'
//        var newMail = ""
//        for (char in tempMail){
//            if(char != mark){
//                newMail += char
//            }
//        }
//
//        // ดึงข้อมูลแบบ Realtime
//        myref.child("Account")
//            .child(newMail)
//            .child("User name").addValueEventListener(postListener)
//
//
//        RecyclerViewMphoto = view.findViewById(R.id.RecyclerView_Mphoto)
//        RecyclerViewPphoto = view.findViewById(R.id.RecyclerView_Pphoto)
//
//
//
//        //Mphoto นะครับเธอ
//        RecyclerViewMphoto.layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL, false)
//        databaseReferenceMphoto = database.getReference("Mphoto")
//        responseMphoto = mutableListOf()
//        MphotoAdapter = MphotoAdapter(responseMphoto as ArrayList<MphotoModel>)
//        RecyclerViewMphoto.adapter = MphotoAdapter
//
//
//
//        //Pphoto ค้าบสุดหล่อ
//        RecyclerViewPphoto.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL, false)
//        databaseReferencePphoto = database.getReference("Pphoto")
//        responsePphoto = mutableListOf()
//        PphotoAdapter = PphotoAdapter(responsePphoto as ArrayList<MphotoModel>)
//        RecyclerViewPphoto.adapter = PphotoAdapter
//
//        onBindingFirebase()
//
//        return view
//    }
//
//    private fun onBindingFirebase() {
//        databaseReferenceMphoto.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                this@HeartFragment.responseMphoto.add(snapshot.getValue(MphotoModel::class.java)!!)
//                MphotoAdapter!!.notifyDataSetChanged()
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//        databaseReferencePphoto.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                this@HeartFragment.responsePphoto.add(snapshot.getValue(MphotoModel::class.java)!!)
//                PphotoAdapter!!.notifyDataSetChanged()
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//    }
//
    private var _binding: FragmentHotBinding? = null
    lateinit var databaseReferenceheart: DatabaseReference
    private var homeAdapter: homeAdapter? = null
    lateinit var RecyclerViewheart:RecyclerView
    lateinit var responseheart:MutableList<MphotoModel>
    lateinit var database: FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val view = inflater.inflate(R.layout.fragment_heart, container, false)
        RecyclerViewheart = view.findViewById(R.id.RecyclerView_heart)

        database = FirebaseDatabase.getInstance()

        RecyclerViewheart.layoutManager = GridLayoutManager(context,2)
        databaseReferenceheart = database.getReference("heart")
        responseheart = mutableListOf()
        homeAdapter = homeAdapter(responseheart as ArrayList<MphotoModel>)
        RecyclerViewheart.adapter = homeAdapter

        onBindingFirebase()
        return view
    }
    private fun onBindingFirebase() {
        databaseReferenceheart.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@HeartFragment.responseheart.add(snapshot.getValue(MphotoModel::class.java)!!)
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}