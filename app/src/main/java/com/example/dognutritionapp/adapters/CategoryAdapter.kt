package com.example.dognutritionapp.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.data.Category

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle: TextView = itemView.findViewById(R.id.categoryTxt)
        private val categoryImage: ImageView = itemView.findViewById(R.id.categoryIcon)

        fun bindCategoryData(category: Category, clickListener: (Category) -> Unit) {
            categoryTitle.text = category.name

            // Load the image using Glide without modifying the original color
            Glide.with(itemView.context)
                .load(category.imageUri)
                .into(categoryImage)

            // Set up click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
                clickListener(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.viewholder_category, parent, false)
        return CategoryViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bindCategoryData(category, clickListener)

        if (selectedPosition == position) {
            // Highlight the selected item by changing background and text color
            holder.itemView.findViewById<LinearLayout>(R.id.categoryMainLayout)
                .setBackgroundResource(R.drawable.green_btn_background)
            holder.itemView.findViewById<TextView>(R.id.categoryTxt).apply {
                visibility = View.VISIBLE
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
        } else {
            // Reset background and hide text for unselected items
            holder.itemView.findViewById<LinearLayout>(R.id.categoryMainLayout).setBackgroundResource(0)
            holder.itemView.findViewById<TextView>(R.id.categoryTxt).apply {
                visibility = View.GONE
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
