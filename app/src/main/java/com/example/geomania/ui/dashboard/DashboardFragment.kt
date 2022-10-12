package com.example.geomania.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.geomania.R
import com.example.geomania.User
import com.example.geomania.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        root = binding.root

        root.findViewById<TextView>(R.id.usernameET).text = User.username
        root.findViewById<TextView>(R.id.usernameET).doOnTextChanged { text, _, _, _ ->
            User.username = text.toString()
        }

        root.findViewById<TextView>(R.id.levelTV).text = "Επίπεδο ${User.level}"
        root.findViewById<TextView>(R.id.xpTV).text = "${User.experience} XP"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}