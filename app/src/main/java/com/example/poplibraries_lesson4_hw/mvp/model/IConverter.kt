package com.example.poplibraries_lesson4_hw.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convertImage(path:String):Completable
}