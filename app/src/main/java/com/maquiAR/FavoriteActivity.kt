package com.maquiAR

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maquiAR.Models.Product
import com.maquiAR.support.recyclers.ProductCardViewRecycler
import com.maquiAR.support.FavoriteUtils
import com.maquiAR.support.ProductLoader

class FavoriteActivity : AppCompatActivity() {

    private var allProducts: List<Product> = ArrayList()
    private var favoriteIds:  List<Int> = ArrayList()
    private var favoriteProducts: List<Product> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        loadToolbar()
        loadBottomMenuNavigation()

        allProducts = ProductLoader.loadProducts()
        favoriteIds = FavoriteUtils.getFavorites(this)
        favoriteProducts = FavoriteUtils.getProductsByIdsList(allProducts, favoriteIds)

        showProducts(favoriteProducts)
    }


    private fun showProducts(products: List<Product>) {
        val productExhibition = findViewById<View>(R.id.recycler_products_list) as RecyclerView
        val prodCardAdapter = ProductCardViewRecycler(this, products)
        productExhibition.layoutManager = GridLayoutManager(this, 2)
        productExhibition.adapter = prodCardAdapter

        val emptyText = findViewById<View>(R.id.empty_view) as TextView
        if(products.isEmpty()) {
            productExhibition.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            productExhibition.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }
    }

    private fun loadToolbar() {
        val toolbar = findViewById<View>(R.id.app_toolbar) as Toolbar
        toolbar.title = "Favoritos"
        setSupportActionBar(toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun loadBottomMenuNavigation() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomMenu.setOnNavigationItemSelectedListener(listener)
        bottomMenu.selectedItemId = R.id.menuFavorite
    }

    var listener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val intent: Intent
            when (item.itemId) {
                R.id.menuCamera -> {
                    intent = Intent(this@FavoriteActivity, ARVisualizationActivity::class.java)
                    intent.putExtra("Product", ProductLoader.getRandomProduct())
                    startActivity(intent)
                }
                R.id.menuProducts -> {
                    intent = Intent(this@FavoriteActivity, ProductListActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuFavorite -> {}
            }
            true
        }


}