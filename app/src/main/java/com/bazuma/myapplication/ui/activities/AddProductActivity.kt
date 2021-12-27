package com.bazuma.myapplication.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_users_profile.*
import java.io.IOException

class AddProductActivity :BaseActivity(), View.OnClickListener {
    private var mSelectedImageFileUri: Uri? =null
    private var mProductImageUrl: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_product_submit.setOnClickListener(this)
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_add_update_product -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateProductDetails()) {
                        uploadProductImage()

                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))

                    try {

                        GlideLoader(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_product_image
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateProductDetails():Boolean{
        return when {
            mSelectedImageFileUri==null->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image),true)
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title),true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price),true)
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description),true)
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity),true)
                false
            }

            else -> {
                // showErrorSnackBar(resources.getString(R.string.register_successfully), false)
                true
            }
        }
    }
     fun ProductUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }
    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
    }
    private fun uploadProductDetails(){
        var username=this.getSharedPreferences(
            Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")!!

        val product= Product(
            FireStoreClass().getCurrentUserID(),
            username,
            et_product_title.text.toString().trim{it<= ' '},
            et_product_description.text.toString().trim{ it<= ' '},
            et_product_price.text.toString().trim{ it<= ' '},
            et_product_quantity.text.toString().trim{ it<= ' '},
            mProductImageUrl
        )
        FireStoreClass().uploadProductsDetails(this,product)
    }
    fun imageProductUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mProductImageUrl=imageURL
        uploadProductDetails()
    }

}