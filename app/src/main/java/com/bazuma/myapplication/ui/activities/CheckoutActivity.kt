package com.bazuma.myapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bazuma.myapplication.FirebaseClass.FirestoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Address
import com.bazuma.myapplication.models.CartItem
import com.bazuma.myapplication.models.Order
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.adapters.CartItemListAdapter
import com.bazuma.myapplication.utilis.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {
    private var mAddressDetails: Address?=null
    private lateinit var mProductList:ArrayList<Product>
    private lateinit var mCartItemList:ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private lateinit var mOrderDetails: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails =intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if (mAddressDetails != null ){
            tv_checkout_address_type.text = mAddressDetails?.type
            tv_checkout_full_name.text = mAddressDetails?.name
            tv_checkout_address.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                tv_checkout_other_details.text = mAddressDetails?.otherDetails
            }
            tv_checkout_mobile_number.text = mAddressDetails?.mobileNumber
        }
        btn_place_order.setOnClickListener {
            placeAnOrder()
        }
        getProductList()
    }

    private fun  placeAnOrder(){
        showProgressDialog(resources.getString(R.string.please_wait))

       if(mAddressDetails != null){
       mOrderDetails = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemList,
            mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemList[0].image,
            mSubTotal.toString(),
            "100.0",
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )
           FirestoreClass().placeOrder(this@CheckoutActivity, mOrderDetails)
      }

    }

    fun successProductsListFromFirestore(productList:ArrayList<Product>){
        mProductList =productList
        getCartItemsList()
    }

    fun allDetailsUpdatedSuccessfully() {

        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun orderPlacedSuccess() {
        FirestoreClass().updateAllDetails(this,mCartItemList,mOrderDetails)

    }

    fun successCartItemsList(cartList:ArrayList<CartItem>) {
        hideProgressDialog()
        for (product in mProductList) {
            for (cartItem in cartList) {
                if (product.my_product_id == cartItem.product_id) {
                    cartItem.stock_quantity == product.stock_quantity
                }
            }
        }
        mCartItemList = cartList

        rv_cart_items_list.layoutManager = LinearLayoutManager(this)
        rv_cart_items_list.setHasFixedSize(true)

        val cartListAdapter = CartItemListAdapter(this, mCartItemList, false)
        rv_cart_items_list.adapter = cartListAdapter


        for (item in mCartItemList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += (price * quantity)
            }
            tv_checkout_sub_total.text = "Ksh$mSubTotal"

            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_checkout_shipping_charge.text = "Ksh100.0"

            if (mSubTotal > 0) {
                ll_checkout_place_order.visibility = View.VISIBLE

                mTotalAmount = mSubTotal + 100.0
                tv_checkout_total_amount.text = "Ksh$mTotalAmount"
            } else {
                ll_checkout_place_order.visibility = View.GONE
            }

        }
    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductList(this)
    }

    private fun getCartItemsList(){
       // showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }

        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }
    }

