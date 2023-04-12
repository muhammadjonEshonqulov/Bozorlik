package uz.bozorliq.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.bozorliq.data.Repository
import uz.bozorliq.model.category.CategoryData
import uz.bozorliq.model.product.ProductData
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    fun insertProduct(category: ProductData) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertProduct(category)
    }

    fun updateProduct(title: String, checked: Boolean, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.updateProduct(title, checked, id)
    }

    fun delete(categoryData: ProductData) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.delete(categoryData)
    }


//    private val _getProductResponse = Channel<List<ProductData>>()
//    val getProductResponse = _getProductResponse.receiveAsFlow()

    fun getProduct(category_id:Int) = repository.local.getProduct(category_id)

//    fun getProduct() = viewModelScope.launch {
////        _getProductResponse.send(repository.local.getProduct())
//    }

}