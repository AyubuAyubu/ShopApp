package com.bazuma.myapplication.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.ui.activities.ProductDetailsActivity
import com.bazuma.myapplication.ui.ui.fragments.ProductsFragment
import com.bazuma.myapplication.utilis.Constants
import com.bazuma.myapplication.utilis.GlideLoader
import kotlinx.android.synthetic.main.item_product_layout.view.*
import kotlinx.android.synthetic.main.item_product_layout.view.*

open class MyProductListAdapter(
    private val context: Context,
    private val list:ArrayList<Product>,
    private val fragment:ProductsFragment
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_product_layout,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val model = list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text =model.title
            holder.itemView.tv_item_price.text ="Ksh${model.price}"

            holder.itemView.ib_delete_product.setOnClickListener{
                    fragment.deleteProduct(model.my_product_id)
            }
            holder.itemView.setOnClickListener{
                val intent= Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.my_product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}