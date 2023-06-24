import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.tdm.adapters.CartAdapter
import com.example.tdm.databinding.CartDialogLayoutBinding
import com.example.tdm.models.RestaurantModel
import com.example.tdm.room.AppDatabase
import kotlinx.coroutines.launch

class CartDialogFragment : DialogFragment() {
    lateinit var binding: CartDialogLayoutBinding
    lateinit var cartItemModel: CartItemModel
    lateinit var restaurantModel : RestaurantModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CartDialogLayoutBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appDatabase = AppDatabase.buildDatabase(requireActivity())

        cartItemModel = ViewModelProvider(requireActivity())[CartItemModel::class.java]
        restaurantModel = ViewModelProvider(requireActivity())[RestaurantModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            cartItemModel.initialize(appDatabase?.cartItemDao())
            binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            binding.cartRecyclerView.adapter = CartAdapter(requireActivity(), cartItemModel.data, cartItemModel, this@CartDialogFragment)
            updateUi()
        }
        binding.emptyCartBtn.setOnClickListener {
            lifecycleScope.launch {
                cartItemModel.emptyCart()
                updateUi()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun updateUi() {
        binding.cartItemCountTextView.text = (binding.cartRecyclerView.adapter as CartAdapter).itemCount.toString()
        binding.cartDialogItemTotalTextView.text = cartItemModel.getTotal().toString() + " DZD"
        if (cartItemModel.restaurantId!=-1){
            val total = cartItemModel.getTotal() ?: 0.0 // Use a default value if the total is null
            val restaurantFee = cartItemModel.restaurantFee ?: 0.0 // Use a default value if the restaurant fee is null
            val totalPrice = (total + restaurantFee)
            binding.cartDialogDeliveryFeeTextView.text= "$restaurantFee DZD"
            binding.cartDialogTotalTextView.text= "$totalPrice DZD"
        }else{
            binding.cartDialogDeliveryFeeTextView.text="0 DZD"
            binding.cartDialogTotalTextView.text="0 DZD"
        }

    }
}
