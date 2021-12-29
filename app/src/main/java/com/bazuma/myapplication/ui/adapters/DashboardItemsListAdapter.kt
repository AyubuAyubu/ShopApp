package com.bazuma.myapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bazuma.myapplication.R
import com.bazuma.myapplication.models.Product
import com.bazuma.myapplication.utilis.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*
import kotlinx.android.synthetic.main.item_product_layout.view.*

class DashboardItemsListAdapter (
    private val context: Context,
    private val list:ArrayList<Product>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_dashboard_layout,
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

            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
        class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    }

