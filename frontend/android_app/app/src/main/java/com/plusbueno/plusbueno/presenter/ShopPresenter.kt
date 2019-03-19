package com.plusbueno.plusbueno.presenter

import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.view.ShopView

class ShopPresenter(val view: ShopView) {
    val products = ArrayList<Product>()

    fun loadData(shopId : String) {
//        var product = Product(0, "Thneed",
//                "A fine thing that all people need!",
//                4830, null)
//        products.add(product)
//
//        product = Product(1, "Portal Gun",
//                "The Device is now more valuable than the organs and combined incomes of everyone in [subject hometown here].",
//                890000000, null)
//        products.add(product)
//
//        product = Product(2, "Space Core",
//                "Dad, I'm in space! I'm proud of you son. Dad, are you space? Yes. Now we are a family again.",
//                5, null)
//        products.add(product)
//
//        product = Product(3, "ODM Gear",
//                "On that day, mankind received a grim reminder. We lived in fear of the Titans and were disgraced to live in these cages we called walls.",
//                800000, null)
//        products.add(product)

        val productArr : Array<Product> = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/store/" + shopId, Array<Product>::class.java)
        for (p in productArr) {
            products.add(p)
        }

    }


}