package com.plusbueno.plusbueno.presenter

import java.io.InputStream

/**
 * Created by LZMA on 2018/11/14.
 */
interface URLStreamPresenter {
    fun onStreamOpened(stream : InputStream)
    fun onStreamError(reason : String)
}