package com.example.projectempty

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PphotoAdapter (val MphotoList:List<MphotoModel>): RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.itemvertical, parent ,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = MphotoList[position]
        holder.textTitleItem.text = dataModel.title
        Picasso.get().load(dataModel.Image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)


        holder.cardView.setOnClickListener(View.OnClickListener { view ->
            val readActivity = Intent(view.context,ContentActivity::class.java)
            view.context.startActivity(readActivity)
            Log.d("M planner",dataModel.title.toString())
        })

    }

    override fun getItemCount(): Int {
        return MphotoList.size
    }


}