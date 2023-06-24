package com.example.tdm.mainApp

import CartItemModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tdm.adapters.CartAdapter
import com.example.tdm.adapters.DishAdapter
import com.example.tdm.adapters.RestaurantAdapter
import com.example.tdm.models.RestaurantModel
import com.example.tdm.databinding.FragmentRestaurantMenuBinding
import com.example.tdm.models.DishModel
import com.example.tdm.room.AppDatabase
import kotlinx.coroutines.launch


class RestaurantMenuFragment : Fragment() {

    lateinit var binding: FragmentRestaurantMenuBinding
    lateinit var restaurantModel : RestaurantModel
    lateinit var dishModel : DishModel
    lateinit var cartItemModel: CartItemModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantMenuBinding.inflate(inflater, container, false)
        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appDatabase = AppDatabase.buildDatabase(requireActivity())
        cartItemModel = ViewModelProvider(requireActivity())[CartItemModel::class.java]
        restaurantModel = ViewModelProvider(requireActivity())[RestaurantModel::class.java]
        dishModel = ViewModelProvider(requireActivity())[DishModel::class.java]


        viewLifecycleOwner.lifecycleScope.launch {
            cartItemModel.initialize(appDatabase?.cartItemDao())
        }
        Glide.with(requireActivity()).load(restaurantModel.currentRestaurant?.backGroundImageUrl).into(binding.restaurantImageView)
        Glide.with(requireActivity()).load(restaurantModel.currentRestaurant?.logoPictureUrl).into(binding.logoImageView)
        binding.nameTextView.text=restaurantModel.currentRestaurant?.name
        binding.ratingBar.rating=restaurantModel.currentRestaurant?.averageRating ?: 0f
        binding.typeTextView.text=restaurantModel.currentRestaurant?.cuisineType
        binding.reviewsText.text=restaurantModel.currentRestaurant?.numberOfReviews.toString()
        binding.dishRecyclerView.layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        val adapter = DishAdapter(requireActivity(),dishModel,this)
        binding.dishRecyclerView.adapter=adapter

        dishModel.loadDishes(restaurantModel.currentRestaurant?.id)

        dishModel.loading.observe(requireActivity()) { loading ->
            if (loading) {
                binding.dishesProgressBar.visibility = View.VISIBLE
            } else {
                binding.dishesProgressBar.visibility = View.GONE
            }
        }

        dishModel.errorMessage.observe(requireActivity()) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        dishModel.dishes.observe(requireActivity()) { dishes ->
            adapter.setDishes(dishes)
        }

    }

    fun showDish (){
        val dialogFragment= DishDialogFragment()
        dialogFragment.show(childFragmentManager,"DishDialogFragment")
    }

}