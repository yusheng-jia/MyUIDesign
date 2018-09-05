package com.jayson.dagger2demo.modal;

import javax.inject.Inject;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/9/1.
 * desc:
 */
public class Book {

    @Inject
    Book(){

    }
    @Override
    public String toString() {
        return "演员的自我修养";
    }
}
