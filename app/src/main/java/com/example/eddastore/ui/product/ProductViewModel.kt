package com.example.eddastore.ui.product


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.repo.ProductRepository
import com.example.eddastore.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repo: ProductRepository
) : ViewModel() {

    fun productsAll(): Flow<List<Product>> = repo.observeAll()
    fun productsActive(): Flow<List<Product>> = repo.observeActive()

    fun save(p: Product) = viewModelScope.launch {
        repo.upsert(p)
    }

    fun deactivate(p: Product) = viewModelScope.launch {
        repo.setActive(id = p.id, active = false)
    }

    fun activate(p: Product) = viewModelScope.launch {
        repo.setActive(id = p.id, active = true)
    }

    class Factory(private val repo: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(ProductViewModel::class.java))
            return ProductViewModel(repo) as T
        }
    }

    /** Factory para Compose */
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = AppDb.get(context)
                    val repo = ProductRepository(db.productDao())
                    return ProductViewModel(repo) as T
                }
            }
    }
}
