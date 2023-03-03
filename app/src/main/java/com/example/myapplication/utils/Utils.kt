package com.example.myapplication.utils

import java.util.*

var defaultText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Arcu risus quis varius quam. Laoreet suspendisse interdum consectetur libero id faucibus nisl. Scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. Quisque non tellus orci ac auctor augue."

fun capitalizeFirstLetter(string: String): String {
    return string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}