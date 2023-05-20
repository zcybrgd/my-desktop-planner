package com.example.demo.Exceptions;

import java.io.Serializable;

// pour fixer le planning
public class DateAnterieure extends Exception implements Serializable {
    public DateAnterieure(String message) {
        super(message);
    }
}
