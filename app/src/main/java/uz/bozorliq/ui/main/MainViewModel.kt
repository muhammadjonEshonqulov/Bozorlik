package uz.bozorliq.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.bozorliq.data.Repository
import uz.bozorliq.model.category.CategoryData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    fun insertCategory(category: CategoryData) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertCategory(category)
    }

    fun updateCategory(title: String, category_id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.updateCategory(title, category_id)
    }

    fun delete(categoryData: CategoryData) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.delete(categoryData)
    }


//    private val _getCategoryResponse = Channel<List<CategoryData>>()
//    val getCategoryResponse = _getCategoryResponse.receiveAsFlow()

    val getCategory = repository.local.getCategory()

//    fun getCategory() = viewModelScope.launch {
////        _getCategoryResponse.send(repository.local.getCategory())
//    }

}