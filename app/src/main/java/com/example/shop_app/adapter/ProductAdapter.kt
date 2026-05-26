package com.example.shop_app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.shop_app.R
import com.example.shop_app.model.Product

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>
) : BaseAdapter() {
    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Any = products[position]

    override fun getItemId(position: Int): Long = products[position].id.toLong()

    @SuppressLint("ViewHolder", "SetTextI18n", "MissingInflatedId")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.product_item, parent, false)

        val image = view.findViewById<ImageView>(R.id.imageProduct)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        val product = products[position]

        tvName.text = product.name
        tvPrice.text = "Цена: ${product.price}"
        when (product.type) {
            "phone" -> image.setImageResource(R.drawable.phone)
            "laptop" -> image.setImageResource(R.drawable.laptop)
            "watch" -> image.setImageResource(R.drawable.watch)
            "audio" -> image.setImageResource(R.drawable.headphones)
            "tablet" -> image.setImageResource(R.drawable.tablet)
        }

        return view
    }
}