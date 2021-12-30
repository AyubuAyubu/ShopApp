package com.bazuma.myapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.CartItem
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity :BaseActivity(),View.OnClickListener {
        private var mProductID:String=""
         private lateinit var mProductDetails:Product
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setActionBar()
            if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
                mProductID=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            }
            var productOwnerID:String=""
            if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
                productOwnerID=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
            }
            if(FireStoreClass().getCurrentUserID()==productOwnerID){
                btn_add_to_cart.visibility= View.GONE
            }else{
                btn_add_to_cart.visibility=View.VISIBLE
            }
            getProductDetails()

            btn_add_to_cart.setOnClickListener(this)
            btn_go_to_cart.setOnClickListener(this)

    }
    fun getProductDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductsDetails(this,mProductID)
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }

    }

    fun addToCart(){
        val cartItem=CartItem(
            FireStoreClass().getCurrentUserID(),
            mProductID,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addCartItems(this,cartItem)
    }

   fun productExistsInCart(){
       hideProgressDialog()
       btn_add_to_cart.visibility=View.GONE
       btn_go_to_cart.visibility=View.VISIBLE
   }

    fun productDetailsSuccess(product:Product){
        mProductDetails=product
        hideProgressDialog()
        GlideLoader(this).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text=product.title
        tv_product_details_price.text="Ksh${product.price}"
        tv_product_details_description.text=product.description
        tv_product_details_available_quantity.text=product.stock_quantity

        if (FireStoreClass().getCurrentUserID()==product.user_id){
            hideProgressDialog()
        }else{
            FireStoreClass().checkIfItemExistInCart(this,mProductID)
        }
    }

    fun addToCartSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            "Item us been successfully add to cart",
           Toast.LENGTH_SHORT
        )
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.btn_add_to_cart->{
                    addToCart()
                }
                R.id.btn_go_to_cart->{
                    startActivity(Intent(this,CartListActivity::class.java))
                }
            }
        }
    }
}