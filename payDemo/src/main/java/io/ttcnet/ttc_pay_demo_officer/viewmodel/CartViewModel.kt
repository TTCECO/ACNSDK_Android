package io.ttcnet.ttc_pay_demo_officer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel;
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel
import io.ttcnet.ttc_pay_demo_officer.repository.CartRepository

class CartViewModel : ViewModel {

    lateinit var checkedFurniture: LiveData<ArrayList<FurnitureModel>>

    constructor(){
        checkedFurniture = CartRepository.getData()
    }

    fun addFurniture(furniture: FurnitureModel) {
        CartRepository.addFurniture(furniture)
        checkedFurniture = CartRepository.getData()
    }

    fun removeFurniture(furniture: FurnitureModel) {
        CartRepository.removeFurniture(furniture)
        checkedFurniture = CartRepository.getData()
    }

    fun clear(){
        checkedFurniture.value?.clear()
    }
}
