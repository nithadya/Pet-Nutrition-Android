package com.example.dognutritionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.data.PetFoods

class PetFoodListAdapter(
    private val petFoodList :List<PetFoods>,
    private val clickListener: (PetFoods) -> Unit):
    RecyclerView.Adapter<PetFoodListAdapter.PetFoodViewHolder>(){

    private lateinit var petFoodTitle: TextView
    private lateinit var petFoodImage: ImageView
    private lateinit var petFoodPrice: TextView



    inner class PetFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPetFoodData(petFoods: PetFoods, clickListener: (PetFoods) -> Unit) {
            petFoodTitle = itemView.findViewById(R.id.foodCardTitleShimmer)
            petFoodPrice = itemView.findViewById(R.id.foodListPriceShimmer)
            petFoodImage =itemView.findViewById(R.id.foodListImgShimmer)
            petFoodTitle.text = petFoods.name
            petFoodPrice.text = petFoods.price.toString()

            // Load the image using Glide
            Glide.with(itemView.context)
                .load(petFoods.imageUri)
                .placeholder(R.drawable.bell_icon)
                .error(R.drawable.star)
                .into(petFoodImage)

            // Set up click listener
            itemView.setOnClickListener {
                clickListener(petFoods)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetFoodViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.viewholder_food,parent,false)
        return PetFoodViewHolder(listItem)
    }


    override fun onBindViewHolder(holder: PetFoodViewHolder, position: Int) {
        val petFood = petFoodList[position]
        holder.bindPetFoodData(petFood,clickListener )


    }


    override fun getItemCount(): Int {
        return petFoodList.size
    }
}



