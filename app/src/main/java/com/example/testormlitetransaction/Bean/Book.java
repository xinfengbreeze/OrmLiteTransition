package com.example.testormlitetransaction.Bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName ="book")
public class Book {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField()
    private String bookName;
    @DatabaseField
    private int price;
    @DatabaseField(foreign=true,foreignAutoRefresh = true)
    private Student student;
    public Book(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
