package io.ttcnet.ttc_pay_demo_officer.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel

/**
 * Created by lwq on 2018/12/17.
 */
object FurnitureRepository {

    fun genData(): LiveData<ArrayList<FurnitureModel>> {
        val furnitures = ArrayList<FurnitureModel>()
        val res = MutableLiveData<ArrayList<FurnitureModel>>()

        var goods1 = FurnitureModel()
        goods1.id = 1
        goods1.type = Constant.GOODS_TYPE_SOFA
        goods1.imgResId = R.mipmap.sofa1
        goods1.title = "Mellow"
        goods1.description = "Get Harmonious Moment"
        goods1.priceType = Constant.PRICE_TYPE_DOLLAR
        goods1.price = 1.0
        furnitures.add(goods1)

        var goods2 = FurnitureModel()
        goods2.id = 2
        goods2.type = Constant.GOODS_TYPE_SOFA
        goods2.imgResId = R.mipmap.sofa2
        goods2.title = "Shell"
        goods2.description = "Elegant Curve Creator"
        goods2.priceType = Constant.PRICE_TYPE_DOLLAR
        goods2.price = 2.0
        furnitures.add(goods2)

        var goods3 = FurnitureModel()
        goods3.id = 3
        goods3.type = Constant.GOODS_TYPE_SOFA
        goods3.imgResId = R.mipmap.sofa3
        goods3.title = "Designer Chair"
        goods3.description = "Elegant Curve Creator"
        goods3.priceType = Constant.PRICE_TYPE_DOLLAR
        goods3.price = 3.0
        furnitures.add(goods3)

        var goods4 = FurnitureModel()
        goods4.id = 4
        goods4.type = Constant.GOODS_TYPE_SOFA
        goods4.imgResId = R.mipmap.sofa4
        goods4.title = "Industrial"
        goods4.description = "Elegant Curve Creator"
        goods4.priceType = Constant.PRICE_TYPE_DOLLAR
        goods4.price = 4.0
        furnitures.add(goods4)

        var goods5 = FurnitureModel()
        goods5.id = 5
        goods5.type = Constant.GOODS_TYPE_SOFA
        goods5.imgResId = R.mipmap.sofa5
        goods5.title = "Belong"
        goods5.description = "Elegant Curve Creator"
        goods5.priceType = Constant.PRICE_TYPE_DOLLAR
        goods5.price = 5.0
        furnitures.add(goods5)

        var goods6 = FurnitureModel()
        goods6.type = Constant.GOODS_TYPE_SOFA
        goods6.id = 6
        goods6.imgResId = R.mipmap.sofa6
        goods6.title = "Blu dot"
        goods6.description = "Puff Puff Studio Sofa"
        goods6.priceType = Constant.PRICE_TYPE_DOLLAR
        goods6.price = 6.0
        furnitures.add(goods6)

        var goods7 = FurnitureModel()
        goods7.type = Constant.GOODS_TYPE_SOFA
        goods7.imgResId = R.mipmap.sofa7
        goods7.id = 7
        goods7.title = "Beetle"
        goods7.description = "GamFratesi for Gobi"
        goods7.priceType = Constant.PRICE_TYPE_DOLLAR
        goods7.price = 7.0
        furnitures.add(goods7)

        var goods8 = FurnitureModel()
        goods8.type = Constant.GOODS_TYPE_SOFA
        goods8.imgResId = R.mipmap.sofa8
        goods8.title = "CH162"
        goods8.id = 8
        goods8.description = "Hans J. Wegner from Carl Hansen & Son"
        goods8.priceType = Constant.PRICE_TYPE_DOLLAR
        goods8.price = 8.0
        furnitures.add(goods8)

        res.value = furnitures
        return res
    }
}