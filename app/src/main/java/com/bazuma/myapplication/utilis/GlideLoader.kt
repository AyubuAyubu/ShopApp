package com.bazuma.myapplication.utilis

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bazuma.myapplication.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context) {
   fun loadUserPicture(image: Any,imageView:ImageView){
       try{
           //Load the image into the image View
           Glide
               .with(context)
               //URI of the image
               .load(Uri.parse(image.toString()))
               //Scale type of the image
               .centerCrop()
               //default placeholder when image failed to load
               .placeholder(R.drawable.ic_user_placeholder)
               //the view in which the image will be load
               .into(imageView)
       }catch (e:IOException){
           e.printStackTrace()
       }
   }
    fun loadProductPicture(image: Any,imageView:ImageView){
        try{
            //Load the image into the image View
            Glide
                .with(context)
                .load(Uri.parse(image.toString()))
                .centerCrop()
                .into(imageView)
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}