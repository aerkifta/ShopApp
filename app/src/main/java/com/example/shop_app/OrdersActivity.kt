package com.example.shop_app

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.shop_app.database.DatabaseHelper
import java.time.LocalDate

class OrdersActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val dbHelper = DatabaseHelper(this)

        val etClientFullName = findViewById<EditText>(R.id.etClientFullName)
        val etDateFrom = findViewById<EditText>(R.id.etDateFrom)
        val etDateTo = findViewById<EditText>(R.id.etDateTo)
        val btnShow = findViewById<Button>(R.id.btnShow)
        val listView = findViewById<ListView>(R.id.listViewOrders)

        btnShow.setOnClickListener {
            var dateFrom = etDateFrom.text.toString()
            if (dateFrom.isEmpty()) {
                dateFrom = "2001-01-01"
            }
            var dateTo = etDateTo.text.toString()
            if (dateTo.isEmpty()) {
                dateTo = LocalDate.now().toString()
            }
            val cursor = dbHelper.getOrdersByClientAndPeriod(
                etClientFullName.text.toString(),
                dateFrom,
                dateTo
            )

            val list = ArrayList<String>()

            while (cursor.moveToNext()) {

                val id = cursor.getInt(0)
                val date = cursor.getString(1)
                val total = cursor.getDouble(2)

                list.add(
                    "Заказ: $id\nДата: $date\nСумма: $total"
                )
            }
            if (list.isEmpty()){
                list.add("По заданным параметрам ничего не найдено")
            }

            val adapter = ArrayAdapter(
                this,
                R.layout.item_order,
                R.id.textOrder,
                list
            )

            listView.adapter = adapter
        }
    }
}