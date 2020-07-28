package com.example.neige.traitements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Salah-Eddine ET-TALEBY
 * Classe anonyme afin de refactorer une méthode utilisée par plusieurs classes
 */
public class TraitementJSON {

    // Lire le contenu du fichier JSON et retourner le résultat dans une chaîne (String)
    static String lireForm(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}