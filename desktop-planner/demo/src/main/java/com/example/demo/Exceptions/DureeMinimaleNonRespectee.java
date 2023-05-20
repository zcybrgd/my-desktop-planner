package com.example.demo.Exceptions;

import java.io.Serializable;

// dans la décomposition du créneau
public class DureeMinimaleNonRespectee extends Exception implements Serializable {
    public DureeMinimaleNonRespectee(String message) {
        super(message);
    }
}
