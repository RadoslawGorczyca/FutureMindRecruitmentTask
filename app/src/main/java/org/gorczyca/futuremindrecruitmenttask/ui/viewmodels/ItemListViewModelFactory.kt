package org.gorczyca.futuremindrecruitmenttask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.gorczyca.futuremindrecruitmenttask.repositories.MainRepository

/**
 * Created by: Radosław Gorczyca
 * 28.12.2020 13:27
 */
@Suppress("UNCHECKED_CAST")
class ItemListViewModelFactory(private val mainRepository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemListViewModel::class.java)) {
            return ItemListViewModel(mainRepository = mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}