package com.example.dognutritionapp.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.data.Category
import com.example.dognutritionapp.data.PetFoods

class FoodSearchFilterListAdapter(
    private var petFoodList: List<PetFoods>,
    private val clickListener: (PetFoods) -> Unit
) : RecyclerView.Adapter<FoodSearchFilterListAdapter.PetFoodViewHolder>() {

    inner class PetFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petFoodTitle: TextView = itemView.findViewById(R.id.foodCardTitle)
        private val petFoodPrice: TextView = itemView.findViewById(R.id.foodListPrice)
        private val petFoodImage: ImageView = itemView.findViewById(R.id.foodListImg)

        fun bindPetFoodData(petFoods: PetFoods, clickListener: (PetFoods) -> Unit) {
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
        val listItem = layoutInflater.inflate(R.layout.viewholder_food, parent, false)
        return PetFoodViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: PetFoodViewHolder, position: Int) {
        val petFood = petFoodList[position]
        holder.bindPetFoodData(petFood, clickListener)
    }

    override fun getItemCount(): Int {
        return petFoodList.size
    }

    // Update data for filtered or searched results
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPetFoodList: List<PetFoods>) {
        petFoodList = newPetFoodList
        notifyDataSetChanged()
    }
}



