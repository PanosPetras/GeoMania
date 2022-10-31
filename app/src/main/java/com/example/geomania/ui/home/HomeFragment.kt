package com.example.geomania.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.geomania.QuestionBrowserActivity
import com.example.geomania.R
import com.example.geomania.databinding.FragmentHomeBinding
import com.example.geomania.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var root: CoordinatorLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        root = binding.root

        root.findViewById<Button>(R.id.playBtn).setOnClickListener {
            play()
        }

        return root
    }

    private fun play() {
        if(!isNetworkAvailable(root.context)){
            Snackbar.make(root, getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(context, QuestionBrowserActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}