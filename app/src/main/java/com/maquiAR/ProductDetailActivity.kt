package com.maquiAR

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maquiAR.Models.Product
import com.maquiAR.support.FavoriteUtils

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productPicked: Product

    //Layout TextView fields
    private lateinit var prodName: TextView
    private lateinit var prodDescription: TextView
    private lateinit var prodColor: TextView

    private lateinit var prodARViewButton: Button
    private lateinit var addToFavoriteButton: ImageButton

    private lateinit var imageSlider: ImageSlider
    private val imageList = ArrayList<SlideModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        loadToolbar()
        loadBottomMenuNavigation()
        getLayoutElements()

        val intent = intent
        productPicked = intent.extras!!.getSerializable("ProductCheck") as Product

        loadImageSlider()

        if (checkProductInFavorite()) {
            addToFavoriteButton.setImageResource(R.drawable.icon_favorite_64dp)
        } else {
            addToFavoriteButton.setImageResource(R.drawable.icon_favorite_bordered_64dp)
        }

        setProductInfo()

    }

    private fun loadToolbar() {
        val toolbar = findViewById<View>(R.id.app_toolbar) as Toolbar
        toolbar.title = "Sobre o Produto"
        setSupportActionBar(toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun loadBottomMenuNavigation() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_menu)
        bottomMenu.selectedItemId = R.id.menuProducts
        bottomMenu.setOnNavigationItemSelectedListener(listener)
    }

    private var listener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val intent: Intent
            when (item.itemId) {
                R.id.menuCamera -> {
                    intent = Intent(this@ProductDetailActivity, ARVisualizationActivity::class.java)
                    intent.putExtra("Product", productPicked)
                    startActivity(intent)
                }
                R.id.menuProducts -> {
                    intent = Intent(this@ProductDetailActivity, ProductListActivity::class.java)
                    startActivity(intent)
                }
                R.id.menuFavorite -> {
                    intent = Intent(this@ProductDetailActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    fun getLayoutElements() {
        prodARViewButton = findViewById<View>(R.id.ar_visualization_button) as Button
        addToFavoriteButton = findViewById<View>(R.id.toggleFavorite) as ImageButton
        prodName = findViewById<View>(R.id.prodName) as TextView
        prodDescription = findViewById<View>(R.id.prodTextDescription) as TextView
        prodColor = findViewById<View>(R.id.prodColor) as TextView
    }

    private fun loadImageSlider() {
        for (i in productPicked.images) {
            imageList.add(SlideModel(i))
        }
        imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

    }

    private fun checkProductInFavorite(): Boolean {
        return FavoriteUtils.isFavorite(this.productPicked.id)
    }

    private fun setProductInfo() {
       productPicked.apply {
           prodName.text = name
           prodColor.text = "Cor: $colorName"
           prodDescription.text = description
       }
    }

    fun addProductToFavorite() {
        FavoriteUtils.addFavorite(productPicked.id, this)
        Toast.makeText(
            applicationContext, "Produto adicionado aos favoritos!",
            Toast.LENGTH_SHORT
        ).show()
        addToFavoriteButton.setImageResource(R.drawable.icon_favorite_64dp)
    }

    fun removeProductFromFavorite() {
        FavoriteUtils.removeFavorite(productPicked.id, this)
        Toast.makeText(
            applicationContext, "Produto removido dos favoritos!",
            Toast.LENGTH_SHORT
        ).show()
        addToFavoriteButton.setImageResource(R.drawable.icon_favorite_bordered_64dp)
    }

    fun checkProductAR(view: View) {
        val intent = Intent(this, ARVisualizationActivity::class.java)
        intent.putExtra("Product", productPicked)
        this.startActivity(intent)
    }

    fun goToWebsite(view: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(productPicked.ecommerceLink))
        startActivity(intent)
    }

    fun toggleFavorite(view: View?) {
        if (checkProductInFavorite()) {
            removeProductFromFavorite()
        } else {
            addProductToFavorite()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}