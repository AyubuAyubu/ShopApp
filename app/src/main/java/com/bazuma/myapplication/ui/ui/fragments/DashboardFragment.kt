package com.bazuma.myapplication.ui.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bazuma.myapplication.R
import com.bazuma.myapplication.R.*
import com.bazuma.myapplication.ui.activities.SettingActivity

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //use if we want to use option menu at the fragment
        setHasOptionsMenu(true)
    }
    //private lateinit var dashboardViewModel: DashboardViewModel
    //private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
       // dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        //_binding = FragmentDashboardBinding.inflate(inflater, container, false)
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
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

 */
}
