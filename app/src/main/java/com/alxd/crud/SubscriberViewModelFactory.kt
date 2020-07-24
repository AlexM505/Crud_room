package com.alxd.crud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alxd.crud.repository.SubscriberRepository

class SubscriberViewModelFactory(private val repository: SubscriberRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriberViewModel::class.java)){
            return SubscriberViewModel(repository) as T
        }
        throw IllegalAccessException("View model class DESCONOCIDA")
    }


}