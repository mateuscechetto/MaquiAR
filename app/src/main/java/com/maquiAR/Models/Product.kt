package com.maquiAR.Models

import android.graphics.Color
import java.io.Serializable
import java.util.ArrayList
import kotlin.math.roundToInt

abstract class Product(
    val id: Int,
    val name: String,
    val description: String,
    val images: ArrayList<String>,
    val ecommerceLink: String,
    val texturePath: String,
    val icon: Int,
    val color: FloatArray,
    val colorName: String
) : Serializable

fun Product.getColorInt(): Int {
    val r = (color[0] * 255).roundToInt()
    val g = (color[1] * 255).roundToInt()
    val b = (color[2] * 255).roundToInt()
    val hex = String.format("#%02x%02x%02x", r, g, b)
    return Color.parseColor(hex)
}