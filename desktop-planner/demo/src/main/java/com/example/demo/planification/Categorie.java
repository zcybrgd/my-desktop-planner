package com.example.demo.planification;


import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categorie implements Serializable {

    private String nom;
    private transient Color couleur;

    // une Map pour stocker les couleurs des différentes catégories
    private static Map<String, Color> couleursParDefaut = new HashMap<>();

    // initialisation des couleurs par défaut
    static {
        couleursParDefaut.put("Studies", Color.BLUE);
        couleursParDefaut.put("Work", Color.GREEN);
        couleursParDefaut.put("Hobby", Color.PURPLE);
        couleursParDefaut.put("Sport", Color.RED);
        couleursParDefaut.put("Health", Color.ORANGE);
        couleursParDefaut.put("Other", Color.GRAY);
    }

    // constructeur pour une catégorie existante avec une couleur par défaut
    public Categorie(String nom) {
        this.nom = nom;
        this.couleur = couleursParDefaut.getOrDefault(nom, Color.BLACK);
    }

    // constructeur pour une nouvelle catégorie avec une couleur personnalisée
    public Categorie(String nom, Color couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }
    public Categorie(){}

    // getters et setters pour nom et couleur
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    // méthode pour ajouter une nouvelle catégorie avec une couleur personnalisée
    public static void ajouterCategorie(String nom, Color couleur) {
        // si la catégorie n'existe pas déjà, on l'ajoute avec la couleur personnalisée
        if (!couleursParDefaut.containsKey(nom)) {
            couleursParDefaut.put(nom, couleur);
        }
    }


    public static List<String> getCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        for (String categoryName : couleursParDefaut.keySet()) {
            categoryNames.add(categoryName);
        }
        return categoryNames;
    }

}
