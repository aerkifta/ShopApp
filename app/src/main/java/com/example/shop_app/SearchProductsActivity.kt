package com.example.shop_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.shop_app.adapter.ProductAdapter
import com.example.shop_app.database.DatabaseHelper
import com.example.shop_app.model.Product

class SearchProductsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_products)

        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val btnSearch = findViewById<Button>(R.id.btnSearchProducts)
        val listView = findViewById<ListView>(R.id.listViewProducts)

        val dbHelper = DatabaseHelper(this)

        btnSearch.setOnClickListener {

            val cursor = dbHelper.searchProducts(
                etName.text.toString(),
                etPrice.text.toString()
            )

            val products = ArrayList<Product>()

            while (cursor.moveToNext()) {

                products.add(
                    Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4)
                    )
                )
            }

            val adapter = ProductAdapter(this, products)

            listView.adapter = adapter
        }
    }

}