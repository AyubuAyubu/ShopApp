package com.bazuma.myapplication.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Address
import com.bazuma.myapplication.models.CartItem
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.adapters.CartItemListAdapter
import com.bazuma.myapplication.utilis.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {
    private var mAddressDetails: Address?=null
    private lateinit var mProductList:ArrayList<Product>
    private lateinit var mCartItemList:ArrayList<CartItem>
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
        getProductList()
    }

    fun successProductsListFromFirestore(productList:ArrayList<Product>){
        mProductList =productList
        getCartItemsList()
    }


    fun successCartItemsList(cartList:ArrayList<CartItem>){
        hideProgressDialog()
        for (product in mProductList){
            for (cartItem in cartList){
                if (product.my_product_id == cartItem.product_id){
                    cartItem.stock_quantity == product.stock_quantity
                }
            }
        }
        rv_cart_items_list.layoutManager= LinearLayoutManager(this)
        rv_cart_items_list.setHasFixedSize(true)

        val cartListAdapter=CartItemListAdapter(this,mCartItemList,true)
        rv_cart_items_list.adapter=cartListAdapter

        mCartItemList=cartList

    }
    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllProductList(this)
    }
    private fun getCartItemsList(){
       // showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CheckoutActivity)
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

