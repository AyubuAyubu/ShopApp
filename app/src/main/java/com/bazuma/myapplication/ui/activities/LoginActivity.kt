package com.bazuma.myapplication.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.User
import com.bazuma.myapplication.utilis.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        tv_forgot_password.setOnClickListener(this)
        tv_register_new.setOnClickListener(this)
        tv_signin.setOnClickListener(this)
    }
    fun userLoggedInSuccess(user: User){
        hideProgressDialog()
       if(user.profileCompleted==0 ){
           val myintent=Intent(this@LoginActivity,UsersProfileActivity::class.java)
           myintent.putExtra(Constants.EXTRA_USER_DETAILS,user)
           startActivity(myintent)
       }else{
           startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
       }
        finish()


    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    LogInRegisterUser()
                }
                R.id.tv_register_new -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private fun validateLogInDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_my_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true

            }
        }
    }
    private fun LogInRegisterUser(){
        if(validateLogInDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))
            val email:String= et_my_email.text.toString().trim{it<=' '}
            val password:String= et_my_password.text.toString().trim{it<=' '}

            //Create an instance and register user with email and password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->

                        //if log is successfully DONE
                        if (task.isSuccessful) {
                            //Log in the user
                            FireStoreClass().getUsersDetails(this@LoginActivity)

                        } else {
                            hideProgressDialog()
                            //if registration is not successfully then show the error message
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
        }
    }
}
