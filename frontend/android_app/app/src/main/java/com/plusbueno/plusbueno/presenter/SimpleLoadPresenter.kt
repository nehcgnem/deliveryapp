package com.plusbueno.plusbueno.presenter

/**
 * Created by LZMA on 2018/11/10.
 */
interface SimpleLoadPresenter<T> {
    fun onSimpleLoadSuccess(result : T)
    fun onSimpleLoadFail(reason : String)
}