package com.josedo.bodymeasurecontrol.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.josedo.bodymeasurecontrol.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbarMain))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.setIcon(R.mipmap.ic_launcher)
        configNav()
    }

    fun configNav() {
        NavigationUI.setupWithNavController(bnvMenu, Navigation.findNavController(this,
            R.id.fragContent
        ))
    }
}
