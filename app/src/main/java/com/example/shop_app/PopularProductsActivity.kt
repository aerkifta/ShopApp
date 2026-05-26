package com.example.shop_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.shop_app.database.DatabaseHelper

class PopularProductsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_products)

        val listView = findViewById<ListView>(R.id.listViewPopular)

        val dbHelper = DatabaseHelper(this)

        val cursor = dbHelper.getTopProducts()

        val list = ArrayList<String>()

        while (cursor.moveToNext()) {

            val name = cursor.getString(0)
            val count = cursor.getInt(1)

            list.add("$name - $count шт.")
        }

        listView.adapter = ArrayAdapter(
            this,
            R.layout.item_order,
            list
        )
    }

}