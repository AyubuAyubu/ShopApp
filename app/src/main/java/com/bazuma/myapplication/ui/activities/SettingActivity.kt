package com.bazuma.myapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.User
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar_register_activity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() , View.OnClickListener{
    private  lateinit var mUserDetails:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setActionBar()

       btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
        ll_address.setOnClickListener(this)
    }
    private fun setActionBar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        toolbar_settings_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun getUserProfileDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User){
        mUserDetails=user
       hideProgressDialog()
        GlideLoader(this).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstName} ${user.lastName}"
        tv_gender.text=user.gender
        tv_email.text=user.email
        tv_mobile_number.text="${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserProfileDetails()
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.btn_logout->{
                    FirebaseAuth.getInstance().signOut()
                    val intent=Intent(this,LoginActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit->{
                    val intent=Intent(this,UsersProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)
                }
                R.id.ll_address->{
                    val intent=Intent(this,AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}