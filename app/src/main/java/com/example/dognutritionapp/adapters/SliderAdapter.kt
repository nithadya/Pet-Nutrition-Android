package com.example.dognutritionapp.adapters


import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R

class SliderAdapter (private val bannerList :List<Int>):RecyclerView.Adapter<BannerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.slider_item_container,parent,false)
        return BannerViewHolder(listItem)
    }


    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(bannerList[position])
            .into(holder.imageBanner)
    }


    override fun getItemCount(): Int {
        return bannerList.size
    }
}


class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

     val imageBanner:ImageView = itemView.findViewById(R.id.imageBanner)

//    fun bindBannerImg(bannerImgItem: BannerItem) {
//        Glide.with(itemView.context)
//            .load(bannerImgItem.bannerImgItem) // URL or Drawable resource ID like R.drawable.sample_image
//            .into(imageBanner)
//    }
}


