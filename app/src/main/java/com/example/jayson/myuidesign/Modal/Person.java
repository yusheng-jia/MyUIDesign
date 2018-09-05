package com.example.jayson.myuidesign.Modal;

import java.util.Objects;

/**
 * author: jayson
 * blog: http://blog.csdn.net/jia4525036
 * time: 2018/9/5.
 * desc:
 */
public class Person {

    private String name;
    private int age;

    private int p = 31;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return age == person.age &&
                name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return p*name.hashCode() + age;
    }
}
