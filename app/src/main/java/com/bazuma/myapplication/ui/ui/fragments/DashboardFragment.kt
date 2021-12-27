package com.bazuma.myapplication.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.R.*
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.activities.SettingActivity
import com.bazuma.myapplication.ui.adapters.DashboardItemsListAdapter
import com.bazuma.myapplication.ui.adapters.MyProductListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_products.tv_no_product_found

class DashboardFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //use if we want to use option menu at the fragment
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root= inflater.inflate(layout.fragment_dashboard,container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        val id=item.itemId
        when(id){
            R.id.action_settings->{
                startActivity(Intent(activity, SettingActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemList(dashboardItemList: ArrayList<Product>) {
        hideProgressDialogForFragment()
        if(dashboardItemList.size>0) {
            rv_dashboard_items.visibility =View.VISIBLE
            tv_no_dashboard_item_found.visibility =View.GONE

            rv_dashboard_items.layoutManager= GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapterProduct= DashboardItemsListAdapter(requireActivity(),dashboardItemList)
            rv_dashboard_items.adapter=adapterProduct
        } else {
           rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_item_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }
    fun getDashboardItemList(){
            showProgressDialogForFragment(resources.getString(R.string.please_wait))
            FireStoreClass().getDashboardItemList(this)
        }
}

