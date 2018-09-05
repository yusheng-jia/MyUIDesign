package com.jayson.dagger2demo.modal;

import com.jayson.dagger2demo.MainActivity;

import dagger.Component;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/9/1.
 * desc:
 */
@Component(modules = BookModule.class)
public interface BookComponent {
     void inject(MainActivity activity);
}
