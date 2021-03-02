package com.example.gramenkovtestproject.presentation.base

open class BasePresenter<V> : IBasePresenter<V> {
    private var fragment: V? = null

    override fun attachView(view: V) {
        fragment = view
    }

    override fun detachView() {
        if (fragment != null) fragment = null
    }

    fun getView(): V? = fragment

}

interface IBaseView<T> {
    fun onResult(data: T)
    fun onError(err: String)
}

interface IBasePresenter<V> {
    fun attachView(view: V)
    fun detachView()
}