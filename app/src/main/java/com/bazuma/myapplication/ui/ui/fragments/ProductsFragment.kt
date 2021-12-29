package com.bazuma.myapplication.ui.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bazuma.myapplication.FirebaseClass.FireStoreClass
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.activities.AddProductActivity
import com.bazuma.myapplication.ui.adapters.MyProductListAdapter
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //use if we want to use option menu at the fragment
        setHasOptionsMenu(true)
    }
    fun successProductListFromFirestore(productList:ArrayList<Product>){
        hideProgressDialogForFragment()
        if (productList.size > 0){
            iv_my_product_item.visibility =View.VISIBLE
            tv_no_product_found.visibility =View.GONE

            iv_my_product_item.layoutManager=LinearLayoutManager(activity)
            iv_my_product_item.setHasFixedSize(true)

            val adapterProduct=MyProductListAdapter(requireActivity(),productList,this)
            iv_my_product_item.adapter=adapterProduct
        }else{
            iv_my_product_item.visibility =View.GONE
            tv_no_product_found.visibility =View.VISIBLE
        }
    }
    fun deleteProduct(productID:String){
       showAlertDialogToDeleteProducts(productID)
    }

    fun showAlertDialogToDeleteProducts(productID: String){
        val builder =AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.Yes)){ dialogInterface,_ ->
                showProgressDialogForFragment(resources.getString(R.string.please_wait))
                dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.No)){ dialogInterface,_ ->
            FireStoreClass().deleteProduct(this,productID)
            dialogInterface.dismiss()

        }
        val alertDialog:AlertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun productDeleteSuccess(){
        hideProgressDialogForFragment()
        Toast.makeText(
            requireActivity(),
            "The product has been Deleted",
            Toast.LENGTH_SHORT
        ).show()
        getProductListFromFirestore()
    }

    fun getProductListFromFirestore(){
        showProgressDialogForFragment(resources.getString(R.string.please_wait))
        FireStoreClass().getProductsList(this)
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