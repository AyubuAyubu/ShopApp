package com.bazuma.myapplication.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bazuma.myapplication.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress_bar.*

//Make BaseActivity inheritable by open it
open class BaseActivity : AppCompatActivity() {
    private var DoubleBackToExitPressOnce=false
        private lateinit var mProgressDialog: Dialog
    /**
     * showErrorSnackBar(message:String,errorMessage:Boolean) it used to
     * DISPLAY snackbar for some time
     * By Check f an error us occured or not
     * IF yes then Red Snack bar with message will be display indicate an error
     *AND if no then A Green SSnack wll be display with a message indicate success
     */
    fun showErrorSnackBar(message:String,errorMessage:Boolean) {
        val snackBar= Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView=snackBar.view

        //check if error has occur should display red snackBar
        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,R.color.colorSnackBarError
                )
            )
        }else{
            //check if error has not occur should display green snackBar
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }
    //Set screen content from a layout resource
    fun showProgressDialog(text:String){
        mProgressDialog=Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress_bar)
        mProgressDialog.tv_progress_text.text=text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
    fun doubleBackToExit() {

        if (DoubleBackToExitPressOnce) {
            super.onBackPressed()
            return
        }

        this.DoubleBackToExitPressOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({ DoubleBackToExitPressOnce = false }, 2000)
    }
}
