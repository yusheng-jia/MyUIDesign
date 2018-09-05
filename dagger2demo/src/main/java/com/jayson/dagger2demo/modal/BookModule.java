package com.jayson.dagger2demo.modal;

import dagger.Module;
import dagger.Provides;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/9/1.
 * desc:
 */
@Module
public class BookModule {

    @Provides
    public Book getBook(){
        return  new Book();
    }

    @Provides
    public Books getBooks(Book book){
        return new Books(book);
    }
}
