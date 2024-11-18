package com.example.dognutritionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.data.Cart
import kotlinx.coroutines.launch

class CartAdapter(
    private val viewModel: PetFoodViewModel,
    private val cartItems: MutableList<Cart>,
    private val onQuantityChange: (Cart) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodName: TextView = itemView.findViewById(R.id.foodName)
        private val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        private val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        private val incrementButton: TextView = itemView.findViewById(R.id.incrementButton)
        private val decrementButton: TextView = itemView.findViewById(R.id.decrementButton)
        private val foodPrice : TextView = itemView.findViewById(R.id.feeEachItem)
        private val foodTotal : TextView = itemView.findViewById(R.id.totalEachItem)

        fun bind(cartItem: Cart) {
            // Launch a coroutine to call the suspend function
            viewModel.viewModelScope.launch {
                val food = viewModel.getFoodById(cartItem.foodId)
                food?.let {
                    foodName.text = it.name
                    foodPrice.text = "LKR ${it.price}"
                    Glide.with(foodImage.context)
                        .load(it.imageUri)
                        .into(foodImage)
                }

                quantityText.text = cartItem.quantity.toString()
                if (food != null) {
                    foodTotal.text = "LKR ${Math.round(cartItem.quantity*food.price)}"
                }


                // Set initial styles
                setButtonStyle(incrementButton, false)
                setButtonStyle(decrementButton, false)


                incrementButton.setOnClickListener {
                    cartItem.quantity++
                    quantityText.text = cartItem.quantity.toString()
                    if (food != null) {
                        foodTotal.text = "LKR ${Math.round(cartItem.quantity*food.price)}"
                    }
                    onQuantityChange(cartItem)

                    // update button color
                    setButtonStyle(incrementButton, true)
                    setButtonStyle(decrementButton, false)

                }
                decrementButton.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        cartItem.quantity--
                        quantityText.text = cartItem.quantity.toString()
                        if (food != null) {
                            foodTotal.text = "LKR ${Math.round(cartItem.quantity*food.price)}"
                        }
                        onQuantityChange(cartItem)
                        // update button color
                        setButtonStyle(incrementButton, false)
                        setButtonStyle(decrementButton, true)
                    }
                }
            }
        }

        private fun setButtonStyle(button: TextView, isActive: Boolean) {
            if (isActive) {
                button.setBackgroundResource(R.drawable.green_btn_background) // Active background
                button.setTextColor(button.context.getColor(R.color.white)) // Active text
            } else {
                button.setBackgroundResource(R.drawable.white_bg) // Default background
                button.setTextColor(button.context.getColor(R.color.green)) // Default text
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun updateCartItems(newItems: List<Cart>) {
        cartItems.clear()
        cartItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        cartItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Cart = cartItems[position]
}
