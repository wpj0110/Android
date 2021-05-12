package com.example.testcode;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country
        implements Serializable { // Needed to add as extra

    private final String name;


    Country(String nm) {
        name = nm;

    }
    @NonNull
    public String toString() {
        return name;
    }
}