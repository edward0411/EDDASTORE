package com.example.eddastore.ui.product.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eddastore.R
import com.example.eddastore.domain.model.Product

class ProductAdapter(
    private val isAdmin: Boolean,
    private val onAddToCart: (Product) -> Unit,
    private val onEdit: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product) = a.id == b.id
        override fun areContentsTheSame(a: Product, b: Product) = a == b
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val img: ImageView = v.findViewById(R.id.img)
        private val name: TextView = v.findViewById(R.id.tvName)
        private val desc: TextView = v.findViewById(R.id.tvDesc)
        private val price: TextView = v.findViewById(R.id.tvPrice)
        private val btnCart: ImageButton = v.findViewById(R.id.btnAddCart)
        private val btnEdit: ImageButton = v.findViewById(R.id.btnEdit)

        fun bind(p: Product) {
            name.text = p.name
            desc.text = p.description
            price.text = "$ ${"%,.2f".format(p.price)}"

            Glide.with(img)
                .load(p.imagePath) // puede ser null o ""
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(img)

            btnEdit.visibility = if (isAdmin) View.VISIBLE else View.GONE
            btnCart.setOnClickListener { onAddToCart(p) }
            btnEdit.setOnClickListener { onEdit(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
