package com.example.testapp4k.Base;

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

}
