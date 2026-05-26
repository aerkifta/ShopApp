package com.example.shop_app.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "shop.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE clients(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            full_name TEXT
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE products(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            manufacturer TEXT,
            price REAL,
            type TEXT
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE orders(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            order_date TEXT,
            client_id INTEGER
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE order_products(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            order_id INTEGER,
            product_id INTEGER,
            quantity INTEGER
            )
        """.trimIndent()
        )

        insertTestData(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS order_products")
        db.execSQL("DROP TABLE IF EXISTS orders")
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS clients")
        onCreate(db)
    }

    private fun insertTestData(db: SQLiteDatabase) {
        val clients = listOf(
            "Иванов Иван", "Петров Петр", "Сидоров Сергей", "Смирнова Анна",
            "Кузнецов Андрей", "Попова Елена", "Васильев Дмитрий", "Павлова Ольга",
            "Соколов Максим", "Новикова Мария"
        )
        for (client in clients) {
            db.execSQL("INSERT INTO clients(full_name) VALUES('$client')")
        }

        val products = listOf(
            "('iPhone 15', 'Apple', 1200.0, 'phone')",
            "('Galaxy S24', 'Samsung', 1000.0, 'phone')",
            "('Pixel 8 Pro', 'Google', 950.0, 'phone')",
            "('Redmi Note 13', 'Xiaomi', 300.0, 'phone')",
            "('MacBook Pro', 'Apple', 2500.0, 'laptop')",
            "('Lenovo Legion', 'Lenovo', 1800.0, 'laptop')",
            "('Asus ZenBook', 'Asus', 1300.0, 'laptop')",
            "('MateBook X Pro', 'Huawei', 1600.0, 'laptop')",
            "('AirPods Pro 2', 'Apple', 250.0, 'audio')",
            "('Galaxy Buds2 Pro', 'Samsung', 180.0, 'audio')",
            "('Sony WH-1000XM5', 'Sony', 350.0, 'audio')",
            "('iPad Air', 'Apple', 600.0, 'tablet')",
            "('Galaxy Watch 6', 'Samsung', 300.0, 'watch')"
        )
        for (product in products) {
            db.execSQL("INSERT INTO products(name, manufacturer, price, type) VALUES $product")
        }

        val orders = listOf(
            "(1, '2026-05-01')",
            "(1, '2026-05-10')",
            "(1, '2026-05-20')",
            "(2, '2026-05-11')",
            "(2, '2026-05-25')",
            "(3, '2026-05-12')",
            "(4, '2026-05-15')",
            "(4, '2026-05-18')",
            "(5, '2026-05-19')",
            "(6, '2026-05-22')",
            "(7, '2026-05-24')",
            "(8, '2026-05-26')",
            "(9, '2026-05-28')",
            "(10, '2026-05-30')"
        )
        for (order in orders) {
            db.execSQL("INSERT INTO orders(client_id, order_date) VALUES $order")
        }

        val orderProducts = listOf(
            "(1, 1, 1)", "(1, 5, 1)",
            "(2, 2, 2)",
            "(3, 9, 1)",
            "(4, 6, 1)",
            "(5, 4, 1)", "(5, 10, 1)",
            "(6, 3, 1)", "(6, 11, 1)",
            "(7, 12, 1)",
            "(8, 13, 1)", "(8, 10, 1)",
            "(9, 7, 1)",
            "(10, 1, 1)", "(10, 9, 2)",
            "(11, 8, 1)",
            "(12, 4, 3)",
            "(13, 11, 1)",
            "(14, 1, 1)", "(14, 12, 1)"
        )
        for (op in orderProducts) {
            db.execSQL("INSERT INTO order_products(order_id, product_id, quantity) VALUES $op")
        }
    }


    fun getOrdersByClientAndPeriod(
        clientFullName: String,
        dateFrom: String,
        dateTo: String
    ): Cursor {
        val db = readableDatabase

        return db.rawQuery(
            """
            SELECT orders.id,
                   orders.order_date,
                   SUM(products.price * order_products.quantity) as total
            FROM orders
            JOIN order_products
                ON orders.id = order_products.order_id
            JOIN products
                ON products.id = order_products.product_id
            JOIN clients
                ON clients.id = orders.client_id
            WHERE clients.full_name = ?
            AND orders.order_date BETWEEN ? AND ?
            GROUP BY orders.id
        """.trimIndent(), arrayOf(clientFullName, dateFrom, dateTo)
        )
    }

    fun getTopProducts(): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            """
            SELECT products.name,
                   SUM(order_products.quantity) as total_count
            FROM order_products
            JOIN products
                ON products.id = order_products.product_id
            GROUP BY products.id
            ORDER BY total_count DESC
            LIMIT 5
        """, null
        )
    }

    fun searchProducts(
        name: String,
        maxPrice: String
    ): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            """
            SELECT *
            FROM products
            WHERE name LIKE ?
            AND price <= ?
        """, arrayOf("%$name%", maxPrice)
        )
    }

}