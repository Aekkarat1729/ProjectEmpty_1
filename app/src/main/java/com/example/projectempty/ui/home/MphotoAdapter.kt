package com.example.projectempty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MphotoAdapter (val MphotoList:List<MphotoModel>):RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.itemhorizontal, parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = MphotoList[position]
        holder.textTitleItem.text = dataModel.title
        Picasso.get().load(dataModel.Image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return MphotoList.size
    }


}
