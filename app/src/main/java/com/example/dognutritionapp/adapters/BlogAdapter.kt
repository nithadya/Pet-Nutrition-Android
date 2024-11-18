package com.example.dognutritionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.data.EducationalContent
import com.example.dognutritionapp.data.PetFoods

class BlogAdapter(private val blogList :List<EducationalContent>): RecyclerView.Adapter<BlogAdapter.BlogViewHolder>(){


    inner class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private  var blogTitle: TextView =itemView.findViewById(R.id.blogTitle)
        private  var blogImage: ImageView =itemView.findViewById(R.id.blogImage)
        private  var blogDescription: TextView =itemView.findViewById(R.id.blogDescription)

        fun bindBlogData(blogList: EducationalContent){

            blogTitle.text = blogList.title
            blogDescription.text =blogList.description

            // Load the image using Glide
            Glide.with(itemView.context)
                .load(blogList.contentUrl)
                .into(blogImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.viewholder_educontent,parent,false)
        return BlogViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return  blogList.size
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bindBlogData(blogList[position])
    }


}