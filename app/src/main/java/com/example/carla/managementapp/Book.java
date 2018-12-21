package com.example.carla.managementapp;

public class Book {
    private final String author;
    private final int id;
    private final String name, genre, addingDate;
    private final double price;

    public Book(int id, String name, String genre, String addingDate, String author,double price) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.addingDate = addingDate;
        this.price = price;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getAddingDate() {
        return addingDate;
    }

    public double getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }
}
