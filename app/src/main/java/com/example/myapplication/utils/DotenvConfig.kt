package com.example.myapplication.utils

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv {
    directory = "/assets"
    filename= "env"
}