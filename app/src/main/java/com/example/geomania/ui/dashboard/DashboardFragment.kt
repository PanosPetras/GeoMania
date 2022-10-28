package com.example.geomania.ui.dashboard

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.geomania.R
import com.example.geomania.User
import com.example.geomania.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val icons: MutableList<ImageButton> = mutableListOf()
    private lateinit var userIconIV: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root = binding.root

        setupUsernameET(root)

        root.findViewById<TextView>(R.id.levelTV).text = getString(R.string.level, User.level)
        root.findViewById<TextView>(R.id.xpTV).text = getString(R.string.experience, User.experience)
        userIconIV =  root.findViewById(R.id.UserIconIV)
        userIconIV.foreground = ResourcesCompat.getDrawable(resources, User.icon, resources.newTheme())

        getIconButtons(root)
        setIconButtonTags()
        icons.forEach {
            it.setOnClickListener { _ ->
                onUserIconButtonClick(it)
            }
        }
        icons.clear()

        return root
    }

    private fun setupUsernameET(root: ConstraintLayout) {
        root.findViewById<EditText>(R.id.usernameET).text = SpannableStringBuilder(User.username)
        root.findViewById<EditText>(R.id.usernameET).doOnTextChanged { text, _, _, _ ->
            User.username = text.toString()
        }
    }

    private fun onUserIconButtonClick(it: ImageButton) {
        userIconIV.foreground = it.background
        User.icon = it.tag as Int
    }

    private fun getIconButtons(root: ConstraintLayout){
        icons.add(root.findViewById(R.id.uic_mapIB))
        icons.add(root.findViewById(R.id.uic_earthIB))
        icons.add(root.findViewById(R.id.uic_old_carIB))
        icons.add(root.findViewById(R.id.uic_trainIB))
        icons.add(root.findViewById(R.id.uic_planeIB))
        icons.add(root.findViewById(R.id.uic_big_benIB))
        icons.add(root.findViewById(R.id.uic_eiffel_towerIB))
        icons.add(root.findViewById(R.id.uic_tower_of_pisaIB))
        icons.add(root.findViewById(R.id.uic_torri_gateIB))
        icons.add(root.findViewById(R.id.uic_brandenburg_gateIB))
        icons.add(root.findViewById(R.id.uic_capIB))
        icons.add(root.findViewById(R.id.uic_hatIB))
        icons.add(root.findViewById(R.id.uic_magnifying_glassIB))
        icons.add(root.findViewById(R.id.uic_binocularsIB))
        icons.add(root.findViewById(R.id.uic_paper_planeIB))
    }

    private fun setIconButtonTags(){
        icons[0].tag = R.drawable.uic_map
        icons[1].tag = R.drawable.uic_earth
        icons[2].tag = R.drawable.uic_old_car
        icons[3].tag = R.drawable.uic_train
        icons[4].tag = R.drawable.uic_plane
        icons[5].tag = R.drawable.uic_big_ben
        icons[6].tag = R.drawable.uic_eiffel_tower
        icons[7].tag = R.drawable.uic_tower_of_pisa
        icons[8].tag = R.drawable.uic_torri_gate
        icons[9].tag = R.drawable.uic_brandenburg_gate
        icons[10].tag = R.drawable.uic_cap
        icons[11].tag = R.drawable.uic_hat
        icons[12].tag = R.drawable.uic_magnifying_glass
        icons[13].tag = R.drawable.uic_binoculars
        icons[14].tag = R.drawable.uic_paper_plane
    }
}