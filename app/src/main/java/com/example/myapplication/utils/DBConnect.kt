package com.example.myapplication.utils

import java.sql.Connection
import java.sql.DriverManager

fun connectToDB(): Connection? {
    val url = "jdbc:mysql://WriteNow.lesspopmorefizz.com:3306"
    val user = "430team"
    val password = "G0teamG0!"
    var connection: Connection? = null

    try {
        Class.forName("com.mysql.jdbc.Driver")
        connection = DriverManager.getConnection(url, user, password)
        println("Connected to MySQL database.")
    } catch (e: Exception) {
        println("Error connecting to MySQL database: ${e.message}")
    }

    return connection
}