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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        root.findViewById<Button>(R.id.playBtn).setOnClickListener {
            play()
        }

        return root
    }

    private fun play() {
        val intent = Intent(context, QuestionBrowserActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}