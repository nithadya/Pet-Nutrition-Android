package com.example.dognutritionapp.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.adapters.CartAdapter
import com.example.dognutritionapp.data.Cart
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.PetFoods
import com.example.dognutritionapp.helper.BaseActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class CartActivity : BaseActivity() {
    private lateinit var viewModel: PetFoodViewModel
    private lateinit var cartAdapter: CartAdapter
    private var cartItems: MutableList<Cart> = mutableListOf() // Initialize here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val userId = intent.getIntExtra("USER_ID", -1)

        // Initialize ViewModel with repository
        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        // Setup RecyclerView with LinearLayoutManager
        val recyclerView = findViewById<RecyclerView>(R.id.viewCartList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize and set the adapter for RecyclerView
        cartAdapter = CartAdapter(viewModel, cartItems) { cartItem ->
            viewModel.updateCartItem(cartItem) // Handle quantity change
        }
        recyclerView.adapter = cartAdapter

        // Observe cart items for changes and update the list
        viewModel.getCartItemsByUser(userId).observe(this) { items ->
            cartItems.clear() // Clear old items
            cartItems.addAll(items) // Add new items
            cartAdapter.notifyDataSetChanged() // Notify adapter of data change
        }

        // Implement swipe-to-delete functionality
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cartItem = cartItems[position]
                viewModel.deleteCartItem(cartItem)
                cartItems.removeAt(position) // Remove item from local list
                cartAdapter.notifyItemRemoved(position) // Notify adapter of item removal
                MotionToast.createColorToast(
                    this@CartActivity,
                    "Item Deleted",
                    "Success",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@CartActivity, R.font.font1))
            }

//            override fun onChildDraw(
//                c: Canvas, recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
//                actionState: Int, isCurrentlyActive: Boolean
//            ) {
//                val itemView = viewHolder.itemView
////              val background = ColorDrawable(Color.RED)
//                val background = ColorDrawable(ContextCompat.getColor(this@CartActivity, R.color.green))
//                val deleteIcon = ContextCompat.getDrawable(this@CartActivity, R.drawable.bin)
//                background.setBounds(
//                    itemView.right + dX.toInt(),
//                    itemView.top,
//                    itemView.right,
//                    itemView.bottom
//                )
//                background.draw(c)
//                deleteIcon?.let {
//                    val dpToPx = (40 * recyclerView.context.resources.displayMetrics.density).toInt()
//                    val iconMargin = (itemView.height - dpToPx) / 2
//                    val iconTop = itemView.top + iconMargin
//                    val iconLeft = itemView.right - iconMargin - dpToPx
//                    val iconRight = itemView.right - iconMargin
//                    val iconBottom = iconTop + dpToPx
//                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//                    it.setTint(ContextCompat.getColor(this@CartActivity, R.color.white))
//                    it.draw(c)
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//            }


            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(ContextCompat.getColor(this@CartActivity, R.color.green))

                // Configure background
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                // Add Lottie animation instead of icon
                val lottieView = LottieAnimationView(recyclerView.context)
                lottieView.setAnimation(R.raw.bin) // Replace with your Lottie file
                val dpToPx = (40 * resources.displayMetrics.density).toInt()

                // Positioning Lottie
                val iconMargin = (itemView.height - dpToPx) / 2
                val iconTop = itemView.top + iconMargin
                val iconLeft = itemView.right - iconMargin - dpToPx
                val iconBottom = iconTop + dpToPx
                val iconRight = itemView.right - iconMargin

                lottieView.layoutParams = RecyclerView.LayoutParams(dpToPx, dpToPx)
                lottieView.x = iconLeft.toFloat()
                lottieView.y = iconTop.toFloat()

                // Play animation
                if (!lottieView.isAnimating) {
                    lottieView.playAnimation()
                }
                recyclerView.addView(lottieView)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }
}