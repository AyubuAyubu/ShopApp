package com.bazuma.myapplication.utilis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
  //Collection in cud firestore
    const val USERS:String ="users"
    const val PRODUCT:String="products"

    const val MYSHOPPAL_PREFERENCES:String ="MyShopPalPrefs"
    const val LOGGED_IN_USERNAME:String ="logged_in_username"

    const val FIRST_NAME:String="firstName"
    const val LAST_NAME:String="lastName"

    const val EXTRA_USER_DETAILS:String ="extra_user_details"
    const val EXTRA_PRODUCT_ID:String ="extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID:String ="extra_product_owner_id"

    const val USER_ID:String="user_id"
    const val DEFAULT_CART_QUANTITY:String="1"
    const val CART_ITEMS:String="cart_items"
    const val PRODUCT_ID:String="product_id"

    const val READ_STORAGE_PERMISSION_CODE =1
    const val PICK_IMAGE_REQUEST_CODE =2

    const val COMPLETE_PROFILE:String="profileCompleted"

    const val MALE:String="male"
    const val FEMALE:String="female"
    const val GENDER:String="gender"

    const val IMAGE:String="image"
    const val MOBILE:String="mobile"
    const val PRODUCT_IMAGE:String="Product_Image"
    const val USER_PROFILE_IMAGE:String="User_Profile_Image"

    fun showImageChooser(activity: Activity){
        //An Intent for launching the image selection from phone storage
        val galleryIntent=Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Launching image selection using constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(activity:Activity,uri: Uri?):String?{
        /**
         * MimeTypeMap:Two way map that maps MIME-types to file extensionss and vice versa
         * getSingleton() Get singleton instance of MimeTypeMap
         * getExtensionFromMimeTypeMap: Return registered extension for give MimeTypeMap
         * contentResolver.getType() Return the MIME type of given content URL
         */
        //Give me file extension
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(activity.contentResolver.getType(uri!!))
    }
}