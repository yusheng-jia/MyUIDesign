package com.jayson.dagger2demo.modal;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/9/3.
 * desc:
 */
public class Books {

    Book book;

    Books(Book book){
        this.book = book;
    }

    @Override
    public String toString() {
        return "书库: " + book;
    }
}
