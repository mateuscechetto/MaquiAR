package com.maquiAR

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maquiAR.Models.Product
import com.maquiAR.support.recyclers.ProductCardViewRecycler
import com.maquiAR.support.FavoriteUtils
import com.maquiAR.support.ProductLoader

class ProductListActivity : AppCompatActivity() {

    private var allProducts: MutableList<Product> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        loadToolbar()
        loadBottomMenuNavigation()

        allProducts = ProductLoader.loadProducts()

        FavoriteUtils.getFavorites(this)

        showProducts(allProducts)

    }

    private fun showProducts(products: List<Product>) {
        val productExhibition = findViewById<View>(R.id.recycler_products_list) as RecyclerView
        val prodCardAdapter = ProductCardViewRecycler(this, products)
        productExhibition.layoutManager = GridLayoutManager(this, 2)
        productExhibition.adapter = prodCardAdapter
    }

    private fun loadToolbar() {
        val toolbar = findViewById<View>(R.id.app_toolbar) as Toolbar
        toolbar.title = "Todas as Maquiagens"
        setSupportActionBar(toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun loadBottomMenuNavigation() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomMenu.setOnNavigationItemSelectedListener(listener)
        bottomMenu.selectedItemId = R.id.menuProducts
    }

    private var listener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val intent: Intent
            when (item.itemId) {
                R.id.menuCamera -> {
                    intent = Intent(this@ProductListActivity, ARVisualizationActivity::class.java)
                    intent.putExtra("Product", ProductLoader.getRandomProduct())
                    startActivity(intent)
                }
                R.id.menuProducts -> {
                }
                R.id.menuFavorite -> {
                    intent = Intent(this@ProductListActivity, FavoriteActivity::class.java )
                    startActivity(intent)
                }
            }
            true
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}