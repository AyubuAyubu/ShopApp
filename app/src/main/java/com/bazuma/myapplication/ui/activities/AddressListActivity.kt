package com.bazuma.myapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Address
import com.bazuma.myapplication.ui.adapters.AddressItemListAdapter
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.SwipeToDeleteCallBack
import com.bazuma.myapplication.utilis.SwipeToEditCallback
import com.myshoppal.ui.activities.AddEditAddressActivity
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_setting.*

class AddressListActivity : BaseActivity() {
    private var mSelectedAddress:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setActionBar()

        tv_add_address.setOnClickListener{
            val intent=Intent(this, AddEditAddressActivity::class.java)
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
            if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
                mSelectedAddress=intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
            }
            if (mSelectedAddress){
                tv_title_address.text="SELECT ADDRESS"
            }
        getAddressList()
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        // Hide the progress dialog
        hideProgressDialog()


        // Print all the list of addresses in the log with name.
        if (addressList.size > 0) {
            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE

            rv_address_list.layoutManager = LinearLayoutManager(this)
            rv_address_list.setHasFixedSize(true)

            val addressAdapter = AddressItemListAdapter(this, addressList,mSelectedAddress)
            rv_address_list.adapter = addressAdapter

            if (!mSelectedAddress) {

                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressItemListAdapter
                        adapter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                        // END
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)

                val deleteSwipeHandler = object : SwipeToDeleteCallBack(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FireStoreClass().deleteAddress(this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id)
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)

            } else {
                rv_address_list.visibility = View.GONE
                tv_no_address_found.visibility = View.VISIBLE
            }

        }
    }

    private fun getAddressList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getAddressesList(this@AddressListActivity)
    }

    private fun setActionBar(){
        setSupportActionBar(toolbar_address_list_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
        }
        toolbar_address_list_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    fun deleteAddressSuccess() {

        // Hide progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@AddressListActivity,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            getAddressList()
        }
    }
}