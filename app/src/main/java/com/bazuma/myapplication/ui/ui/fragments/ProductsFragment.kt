package com.bazuma.myapplication.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.activities.AddProductActivity


class ProductsFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //use if we want to use option menu at the fragment
        setHasOptionsMenu(true)
    }
    fun successProductListFromFirestore(productList:ArrayList<Product>){
    hideProgressDialogForFragment()
        for (i in productList){
            Log.i("Product name",i.title)
        }
    }
    fun getProductListFromFirestore(){
        showProgressDialogForFragment(resources.getString(R.string.please_wait))
        FireStoreClass().getProductsDetails(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_products,container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        val id=item.itemId

        when(id){
            R.id.action_products->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

 */
}