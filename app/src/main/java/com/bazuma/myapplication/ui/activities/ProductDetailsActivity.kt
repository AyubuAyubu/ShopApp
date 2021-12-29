package com.bazuma.myapplication.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity :BaseActivity(),View.OnClickListener {
    private var mProductID:String=""
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
    fun productDetailsSuccess(product:Product){
        hideProgressDialog()
        GlideLoader(this).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text=product.title
        tv_product_details_price.text="Ksh${product.price}"
        tv_product_details_description.text=product.description
        tv_product_details_available_quantity.text=product.stock_quantity
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}