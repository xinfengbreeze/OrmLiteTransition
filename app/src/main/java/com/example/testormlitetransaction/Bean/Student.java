package com.example.testormlitetransaction.Bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "student")
public class Student {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField()
    private String name;
    @DatabaseField()
    private int age;

    public Student(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
