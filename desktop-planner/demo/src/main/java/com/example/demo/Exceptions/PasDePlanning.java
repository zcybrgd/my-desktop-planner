package com.example.demo.Exceptions;

import java.io.Serializable;

public class PasDePlanning extends Exception implements Serializable {
    public PasDePlanning(String message) {
        super(message);
    }
}
