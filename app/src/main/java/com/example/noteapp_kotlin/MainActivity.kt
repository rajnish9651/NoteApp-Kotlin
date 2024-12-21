package com.example.noteapp_kotlin

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.fragmentMain)

        //adding fragment on main activity
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentMain, MainFragment())
            .commit()


    }

}
