package com.ttc.behavior.web;

public interface Callback<T> {
    void success(T t);

    void error(Throwable e);
}