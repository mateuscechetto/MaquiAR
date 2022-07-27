package com.maquiAR.support

import android.app.Activity
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.maquiAR.Models.Product

object FavoriteUtils {

    private var favoriteIds:  MutableList<Int> = ArrayList()


    private fun loadFavoriteIds(activity: Activity): List<Int> {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val json = sharedPref.getString("Favoritos", "[]") ?: "[]"
        if(json.length < 3) {
            this.favoriteIds = ArrayList()
            return this.favoriteIds
        }
        var stringList = json.substring(1, json.length - 1).split(",")
        var intList = stringList.map { string ->
            string.toInt()
        }.toMutableList()
        this.favoriteIds = intList
        return this.favoriteIds
    }

    private fun saveFavorites(activity: Activity) {
        val key = "Favoritos"
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val gson = Gson()
        val json: String = gson.toJson(favoriteIds)
        with(sharedPref.edit()) {
            putString(key, json)
            apply()
        }

    }

    fun getFavorites(activity: Activity): List<Int> {
        if(favoriteIds.isEmpty()) {
            loadFavoriteIds(activity)
        }
        return this.favoriteIds
    }

    fun addFavorite(id: Int, activity: Activity) {
        this.favoriteIds.add(id)
        saveFavorites(activity)
    }

    fun removeFavorite(id: Int, activity: Activity) {
        this.favoriteIds.remove(id)
        saveFavorites(activity)
    }

    fun isFavorite(id: Int) : Boolean {
        return favoriteIds.contains(id)
    }

    fun getProductsByIdsList(allProducts: List<Product>, favoriteIds: List<Int>): List<Product> {
        return allProducts.filter { product ->
            favoriteIds.contains(product.id)
        }
    }
}