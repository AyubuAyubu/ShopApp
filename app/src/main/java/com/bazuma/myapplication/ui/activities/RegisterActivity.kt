package com.bazuma.myapplication.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setActionBar()
        /**
         *Launch log in Screen  when user click on TextView
         */
        tv_signin.setOnClickListener{
            onBackPressed()
        }
        btn_register.setOnClickListener{
           registerUser()

        }
    }

    /**
     * setActionBar()
     * This method is used to display the Navigation Arrow
     * Help us to move to previous activity
     * onBackPressed() by using this method
     */
    private fun setActionBar(){
        setSupportActionBar(toolbar_register_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        toolbar_register_activity.setNavigationOnClickListener{ onBackPressed() }

        btn_register.setOnClickListener{
            validateRegisterDetails()
        }

    }

    /**
     * FUNCTION TO VALIDATE THE ENTRIES OF NEW USER
     *
     * validateRegisterDetails() used to validate the entries
     * if empty space-> trim
     * if no entry ->error message on what to do
     * if no password match ->message mismatch
     * if checkbox not clicked->please agree terms and conditions
     * register user only if all conditions are true->Thank You Register Successfully
     */
    private fun validateRegisterDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(et_my_email.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_my_password.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }
            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
               // showErrorSnackBar(resources.getString(R.string.register_successfully), false)
                true
            }
        }
    }
    /**
     * Function to register new user to firebase firestore
     */
    private fun registerUser(){
        //Check if entries are valid or not
        if(validateRegisterDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))
            val email:String= et_my_email.text.toString().trim{it<=' '}
            val password:String= et_my_password.text.toString().trim{it<=' '}

            //Create an instance and register user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task->

                        //if registration is successfully DONE
                        if (task.isSuccessful){
                            //Register the user
                            val firebaseUser: FirebaseUser =task.result!!.user!!
                            val user= User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{ it<= ' '},
                                et_last_name.text.toString().trim{ it<= ' '},
                                et_email.text.toString().trim{ it<= ' '}
                            )
                            FireStoreClass().registerUser(this@RegisterActivity,user)
                           // FirebaseAuth.getInstance().signOut()
                            //finish()
                        }else{
                            hideProgressDialog()
                            //if registration is not successfully then show the error message
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
                )
        }
    }
    fun userRegistrationSuccess(){
       hideProgressDialog()
        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()

    }
}
