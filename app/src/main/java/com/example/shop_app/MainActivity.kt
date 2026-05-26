package com.example.shop_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.shop_app.database.DatabaseHelper


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase

        } catch (e: Exception) {
            e.printStackTrace()
            println(e.toString())
        }


        val btnOrders = findViewById<Button>(R.id.btnOrders)
        val btnPopular = findViewById<Button>(R.id.btnPopular)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        btnPopular.setOnClickListener {
            startActivity(Intent(this, PopularProductsActivity::class.java))
        }

        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchProductsActivity::class.java))
        }
    }

}
