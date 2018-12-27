package io.ttcnet.ttc_pay_demo_officer.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel

/**
 * Created by lwq on 2018/12/17.
 */
object CartRepository {
    var value = ArrayList<FurnitureModel>()
    var checkedFurnitures = MutableLiveData<ArrayList<FurnitureModel>>()

    fun getData():LiveData<ArrayList<FurnitureModel>>{
        checkedFurnitures.value = value
        return checkedFurnitures
    }

    fun addFurniture(furnitureModel: FurnitureModel){
        checkedFurnitures.value?.add(furnitureModel)
    }

    fun removeFurniture(furnitureModel: FurnitureModel){
        var data = checkedFurnitures.value
        var id = furnitureModel.id
        var removing : FurnitureModel ?= null
        if (data != null) {
            for (model in data) {
                if (model.id == id) {
                    removing = model
                    break
                }
            }
            if (removing != null) {
                data.remove(removing)
            }
        }
    }

}