package com.bazuma.myapplication.ui.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bazuma.myapplication.R

class OrdersFragment : Fragment() {

   // private lateinit var notificationsViewModel: NotificationsViewModel
   // private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_orders,container, false)

        val textView: TextView = root.findViewById(R.id.text_notifications)
        textView.text = "It is notifications fragment"
        return root

    }
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

 */
}