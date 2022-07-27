package com.maquiAR.support.recyclers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.maquiAR.ARVisualizationActivity
import com.maquiAR.Models.Product
import com.maquiAR.Models.getColorInt
import com.maquiAR.R

class ProductCardViewRecycler(
    private val context: Context,
    private val productListData: List<Product>) :
    RecyclerView.Adapter<ProductCardViewRecycler.ProductView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductView {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.card_item_product_view, parent, false)
        return ProductView(view)
    }

    override fun onBindViewHolder(holder: ProductView, position: Int) {

        holder.productName.text = productListData[position].name
        holder.productDescription.setBackgroundColor(productListData[position].getColorInt())
        holder.productImage.setImageResource(productListData[position].icon)
        holder.productCardView.setOnClickListener {
            val intent = Intent(context, ARVisualizationActivity::class.java)
            intent.putExtra("Product", productListData[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productListData.size
    }

    class ProductView(productItem: View) : RecyclerView.ViewHolder(productItem) {
        val productCardView: CardView
        val productImage: ImageView
        val productName: TextView
        val productDescription: TextView

        init {
            productCardView = productItem.findViewById<CardView>(R.id.card_product_view)
            productImage = productItem.findViewById<ImageView>(R.id.product_card_image)
            productName = productItem.findViewById<TextView>(R.id.product_card_name)
            productDescription =
                productItem.findViewById<TextView>(R.id.product_card_description)
        }
    }

}
