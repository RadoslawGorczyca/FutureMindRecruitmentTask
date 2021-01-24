package org.gorczyca.futuremindrecruitmenttask.ui.viewmodels

import androidx.lifecycle.ViewModel
import org.gorczyca.futuremindrecruitmenttask.repositories.MainRepository

class ItemListViewModel(mainRepository: MainRepository) : ViewModel() {
    val itemList = mainRepository.getAllItems()
}