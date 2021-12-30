package com.bazuma.myapplication.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.CartItem
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setActionBar()
    }
    private fun setActionBar() {
        setSupportActionBar(toolbar_cart_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }

    }
    fun successCartItemsList(cartList:ArrayList<CartItem>){
        hideProgressDialog()
        for (x in cartList){
            Log.i("Cart item title",x.title)
        }
    }
    private fun getCartItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this)
    }

    override fun onResume() {
        super.onResume()
        getCartItemList()
    }
}