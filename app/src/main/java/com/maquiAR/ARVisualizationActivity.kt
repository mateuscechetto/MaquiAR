package com.maquiAR

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maquiAR.Models.Product
import com.maquiAR.arface.AugmentedFaceFragment
import com.maquiAR.arface.AugmentedFaceListener
import com.maquiAR.arface.AugmentedFaceNode
import com.maquiAR.support.FavoriteUtils
import com.maquiAR.support.ProductLoader
import com.maquiAR.support.recyclers.ARVisualizationProductCardViewRecycler
import kotlinx.android.synthetic.main.activity_ar_visualization.*


class ARVisualizationActivity : AppCompatActivity(), AugmentedFaceListener,
    ARVisualizationProductCardViewRecycler.OnProductSelectedListener {

    private var productChanged = false
    private lateinit var productPicked: Product

    private lateinit var openDescriptionButton: ImageButton
    private lateinit var addToFavoriteButton: ImageButton
    private lateinit var takePhotoButton: ImageButton
    private lateinit var listProductsButton: ImageButton
    private lateinit var listProductsRecycler: RecyclerView
    private lateinit var toolbar: Toolbar

    private lateinit var augmentedFaceFragment: AugmentedFaceFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_visualization)

        productPicked = intent.extras!!.getSerializable("Product") as Product
        loadToolbar()
        loadLayoutElements()
        augmentedFaceFragment = face_view as AugmentedFaceFragment
        augmentedFaceFragment.setAugmentedFaceListener(this)
    }

    override fun onFaceAdded(face: AugmentedFaceNode) {
        face.setFaceMeshTexture(productPicked.texturePath)
        face.setTextureColor(productPicked.color)
    }

    override fun onFaceUpdate(face: AugmentedFaceNode) {
        if (productChanged) {
            productChanged = false
            face.setFaceMeshTexture(productPicked.texturePath)
            face.setTextureColor(productPicked.color)
        }
    }

    private fun loadToolbar() {
        toolbar = findViewById(R.id.app_toolbar)
        toolbar.title = productPicked.name + " - " + productPicked.colorName
        setSupportActionBar(toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun loadLayoutElements() {
        openDescriptionButton = findViewById(R.id.openProductDescriptionButton)
        openDescriptionButton.setImageResource(R.drawable.icon_cart)
        addToFavoriteButton = findViewById(R.id.toggleFavorite)
        setFavoriteIcon()
        takePhotoButton = findViewById(R.id.takePhotoButton)
        takePhotoButton.setImageResource(R.drawable.icon_camera)

        listProductsButton = findViewById(R.id.listProducts)
        listProductsButton.setImageResource(R.drawable.icon_list)

        loadProducts()
    }

    private fun loadProducts() {
        loadProductsRecycler(ProductLoader.loadProducts())
    }

    private fun loadProductsRecycler(products: List<Product>) {
        val prodCardAdapter = ARVisualizationProductCardViewRecycler(this, products, this)
        listProductsRecycler = findViewById<RecyclerView>(R.id.recycler_simple_products_list).apply {
            visibility = View.VISIBLE
            layoutManager =
                LinearLayoutManager(this@ARVisualizationActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = prodCardAdapter
        }

    }

    override fun onProductSelected(product: Product) {
        this.productPicked = product
        toolbar.title = productPicked.name + " - " + productPicked.colorName
        setFavoriteIcon()
        productChanged = true

    }

    fun openProductDescription(view: View?) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("ProductCheck", productPicked)
        startActivity(intent)
    }

    fun toggleFavorite(view: View?) {
        if (checkProductInFavorite()) {
            removeProductFromFavorite()
        } else {
            addProductToFavorite()
        }
    }

    fun checkProductInFavorite(): Boolean {
        return FavoriteUtils.isFavorite(this.productPicked.id)
    }

    fun setFavoriteIcon() {
        if (checkProductInFavorite()) {
            addToFavoriteButton.setImageResource(R.drawable.icon_favorite_64dp)
        } else {
            addToFavoriteButton.setImageResource(R.drawable.icon_favorite_bordered_64dp)
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
        FavoriteUtils.removeFavorite(productPicked!!.id, this)
        Toast.makeText(
            applicationContext, "Produto removido dos favoritos!",
            Toast.LENGTH_SHORT
        ).show()
        addToFavoriteButton.setImageResource(R.drawable.icon_favorite_bordered_64dp)
    }

    fun takePhoto(view: View) {
        augmentedFaceFragment.takeScreenshot = true
    }

    fun toggleProductList(view: View) {
        if (listProductsRecycler.visibility == View.INVISIBLE) {
            listProductsRecycler.visibility = View.VISIBLE
        } else {
            listProductsRecycler.visibility = View.INVISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}