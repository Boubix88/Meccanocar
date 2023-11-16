package com.example.meccanocar.utils;

import java.util.HashSet;

public class SimilarityUtils {

    /**
     * Calculez la similarité de Jaccard entre deux chaînes
     *
     * @param s1 la première chaîne
     * @param s2 la deuxième chaîne
     * @return la similarité de Jaccard entre les deux chaînes
     */
    public static double jaccardSimilarity(String s1, String s2) {
        HashSet<Character> set1 = new HashSet<>();
        HashSet<Character> set2 = new HashSet<>();

        for (char c : s1.toCharArray()) {
            set1.add(c);
        }

        for (char c : s2.toCharArray()) {
            set2.add(c);
        }

        HashSet<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        HashSet<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}

