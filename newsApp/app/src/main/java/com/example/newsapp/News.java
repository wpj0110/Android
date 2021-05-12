package com.example.newsapp;

import java.io.Serializable;

public class News implements Serializable {

    private final String id;
    private final String name;
    private final String category;


    News(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    String getId() { return id;}
    String getName() {return name;}
    String getCategory() {return category;}
}
