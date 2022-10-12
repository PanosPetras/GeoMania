package com.example.geomania.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geomania.MapsActivity
import com.example.geomania.QuestionBrowserActivity
import com.example.geomania.R
import com.example.geomania.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        root = binding.root

        root.findViewById<Button>(R.id.playBtn).setOnClickListener {
            play()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun play() {
        val intent = Intent(context, QuestionBrowserActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}