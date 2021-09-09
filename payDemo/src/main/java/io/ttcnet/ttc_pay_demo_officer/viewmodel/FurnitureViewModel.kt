package io.ttcnet.ttc_pay_demo_officer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel
import io.ttcnet.ttc_pay_demo_officer.repository.FurnitureRepository

/**
 * Created by lwq on 2018/12/14.
 */
class FurnitureViewModel : ViewModel {

    var furnitureLiveData: LiveData<ArrayList<FurnitureModel>>

    constructor() {
        furnitureLiveData = FurnitureRepository.genData()
    }
}