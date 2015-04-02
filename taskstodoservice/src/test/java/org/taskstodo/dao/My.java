package org.taskstodo.dao;

/**
 * Created by selcukturhan on 01.02.15.
 */
public class My {
    private String name;
    private int age;
    private String position;


    public My(String name, int age, String position) {
        this.name = name;
        this.age = age;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        My my = (My) o;

        if (age != my.age) return false;
        if (!name.equals(my.name)) return false;
        if (!position.equals(my.position)) return false;

        return true;
    }




    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + position.hashCode();
        return result;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof My)) return false;

        My my = (My) o;

        if (age != my.age) return false;
        if (!name.equals(my.name)) return false;
        if (!position.equals(my.position)) return false;

        return true;
    }
*/

    @Override
    public String toString() {
        return "My{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                '}';
    }
}
