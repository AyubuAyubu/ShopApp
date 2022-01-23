package com.bazuma.myapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bazuma.myapplication.FirebaseClass.FirestoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.CartItem
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.adapters.CartItemListAdapter
import com.bazuma.myapplication.utilis.Constants
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*

class CartListActivity : BaseActivity() {
    private lateinit var mProductList:ArrayList<Product>
    private lateinit var mCartListItem:ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setActionBar()

        btn_checkout.setOnClickListener {
            val intent=Intent(this,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
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
        for (product in mProductList){
            for (cartItem in cartList){
                if (product.my_product_id == cartItem.product_id){
                    cartItem.stock_quantity == product.stock_quantity
                }
                if (product.stock_quantity.toInt() == 0){
                    cartItem.cart_quantity=product.stock_quantity
                }
            }
        }
        mCartListItem=cartList
        if(mCartListItem.size>0){
            rv_cart_items_list.visibility= View.VISIBLE
            ll_checkout.visibility= View.VISIBLE
            tv_no_cart_item_found.visibility= View.GONE
            rv_cart_items_list.layoutManager=LinearLayoutManager(this)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter=CartItemListAdapter(this,mCartListItem,true)
            rv_cart_items_list.adapter=cartListAdapter

            var subTotal:Double=0.0
            for (item in mCartListItem) {
                val availableQuantity = item.stock_quantity.toInt()
                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
            }

            tv_sub_total.text = "Ksh$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_shipping_charge.text = "Ksh10.0"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.text = "Ksh$total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    private fun getCartItemsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }
    fun itemRemoveSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            "Item was removed Successfully!",
            Toast.LENGTH_SHORT
        ).show()
        getCartItemsList()
    }
    override fun onResume() {
        super.onResume()
        getProductList()
    }
    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductList(this)
    }
    fun successProductsListFromFirestore(productList:ArrayList<Product>){
        hideProgressDialog()
        mProductList =productList
        getCartItemsList()
    }

}