package com.alxd.crud

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxd.crud.db.Subscriber
import com.alxd.crud.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(),Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButton = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statusMessage

    init {
        saveOrUpdateButton.value = "Guardar"
        clearAllOrDeleteButton.value = "Limpiar todo"
    }

    fun saveOrUpdate(){

        if(inputName.value == null) {
            statusMessage.value = Event("Por favor ingrese un nombre")
        }else if(inputEmail.value == null){
            statusMessage.value = Event("Por favor ingrese un email")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Por favor ingrese un email valido")
        }else{
            if (isUpdateOrDelete){
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            }else{
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0,name,email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete(){
        if (isUpdateOrDelete)
            delete(subscriberToUpdateOrDelete)
        else
            clearAll()
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val newRowId:Long = repository.insert(subscriber)
        if(newRowId>-1)
            statusMessage.value = Event("Subscriber Insertado Satisfactoriamente $newRowId")
        else
            statusMessage.value = Event("Ocurrio un error al tartar de insertar")
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows = repository.update(subscriber)
        if(noOfRows>0){
            cleanAndReplaceValues()
            statusMessage.value = Event("$noOfRows Fila Actualizada Satisfactoriamente")
        }else{
            statusMessage.value = Event("Ocurrio un error al tratar de actualizar")
        }

    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(subscriber)

        if(noOfRowsDeleted>0){
            cleanAndReplaceValues()
            statusMessage.value = Event("$noOfRowsDeleted Fila Eliminado Satisfactoriamente")
        }else{
            statusMessage.value = Event("Ocurrio un error al tratar de eliminar")
        }
    }

    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted>0)
            statusMessage.value = Event("All Subscribers($noOfRowsDeleted) Eliminados Satisfactoriamente")
        else
            statusMessage.value = Event("Ocurrio un error al tratar de eliminar todo")
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButton.value = "Actualizar"
        clearAllOrDeleteButton.value = "Eliminar"
    }

    private fun cleanAndReplaceValues(){
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "Guardar"
        clearAllOrDeleteButton.value = "Limpiar todo"
    }

    //Implementados del Observable
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}