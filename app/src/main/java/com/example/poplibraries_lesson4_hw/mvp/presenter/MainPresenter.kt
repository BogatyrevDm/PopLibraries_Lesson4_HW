package com.example.poplibraries_lesson4_hw.mvp.presenter

import com.example.poplibraries_lesson4_hw.mvp.model.IConverter
import com.example.poplibraries_lesson4_hw.mvp.view.MainView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter

class MainPresenter(val converter: IConverter, val mainThread: Scheduler) :
    MvpPresenter<MainView>() {
    val compositeDisposable:CompositeDisposable? = null
    fun startConvertion() {
        viewState.startConvertion()
    }

    fun convertImage(path:String) {
        val disposable = converter.convertImage(path)
            .observeOn(mainThread).doOnSubscribe { viewState.showDialogue() }
            .subscribe({
                viewState.hideDialogue()
                viewState.showResult()},{
                viewState.hideDialogue()
                viewState.showError()
            })


            compositeDisposable?.add(disposable)
    }
    fun dismiss() {
        compositeDisposable?.dispose()
        viewState.hideDialogue()
        viewState.showInterrupted()
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
    }
}