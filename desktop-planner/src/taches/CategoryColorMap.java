package taches;

import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

public class CategoryColorMap {
    private static final Map<Categorie, Color> CategorieColorMap = new HashMap<>();

    static {
        CategorieColorMap.put(Categorie.STUDIES, Color.BLUE);
        CategorieColorMap.put(Categorie.WORK, Color.ORANGE);
        CategorieColorMap.put(Categorie.HEALTH, Color.GREEN);
        CategorieColorMap.put(Categorie.SPORT, Color.RED);
        CategorieColorMap.put(Categorie.HOBBY, Color.PINK);
        CategorieColorMap.put(Categorie.OTHER, Color.GRAY);
    }

    public static Color getColorForCategorie(Categorie Categorie) {
        return CategorieColorMap.get(Categorie);
    }
}
