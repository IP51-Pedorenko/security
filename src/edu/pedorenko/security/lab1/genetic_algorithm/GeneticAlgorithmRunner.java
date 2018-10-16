package edu.pedorenko.security.lab1.genetic_algorithm;

import edu.pedorenko.security.lab1.SubstitutionCipherHelper;
import edu.pedorenko.security.lab1.genetic_algorithm.genome.Key;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeneticAlgorithmRunner {

    private GeneticAlgorithmRunner() {
    }

    public static void runGeneticAlgorithm(String encryptedString, int populationSize, int mutationProb) throws IOException {
        Key initialKey = SubstitutionCipherHelper.getInitialKey(encryptedString);

        Map<String, Double> wordDictionary = getDictionary();

        Map<String, Double> bigramDictionary = getDictionary("bigrams_dictionary", 2819662855499L);

        Map<String, Double> trigramDictionary = getDictionary("trigrams_dictionary", 4274127909L);

        Population population = new Population(populationSize, mutationProb, encryptedString, bigramDictionary, initialKey);

        Key bestKey;
        int dictionaryId = 0;
        boolean dictionaryChanged = false;
        int i = 0;
        while (true) {

            if (!population.evaluate())  {
                dictionaryId += 1;
                dictionaryChanged = false;
            }

            if (dictionaryId == 1 && !dictionaryChanged) {

                population.setDictionary(trigramDictionary);
                dictionaryChanged = true;
                population.evaluate();

            } else if (dictionaryId == 2 && !dictionaryChanged) {

                population.setDictionary(wordDictionary);
                dictionaryChanged = true;
                population.evaluate();

            } else if (dictionaryId == 3) {
                bestKey = population.getBestKey();
                break;
            }

            population.evaluate();

            Key key = population.getBestKey();
            String decryptedString = SubstitutionCipherHelper.decrypt(encryptedString, key);

            System.out.println(dictionaryId + " pop " + (i + 1) + " best fitness " + key.getFitness() + " average fitness " + population.getTotalFitness() / populationSize + " " + decryptedString);

            population.makeNewPopulation();

            i++;
        }

        System.out.println();

        if (bestKey == null) {
            throw new RuntimeException();
        }


        String decryptedString = SubstitutionCipherHelper.decrypt(encryptedString, bestKey);
        System.out.println(decryptedString);
    }

    private static Map<String, Double> getDictionary() throws IOException {
        LinkedHashMap<String, Double> dictionary = new LinkedHashMap<>();

        Path path = Paths.get("./resources/dictionary1000");

        Files.lines(path).forEach(word -> dictionary.put(word.toUpperCase(), 1.0));

        return dictionary;
    }

    private static Map<String, Double> getDictionary(String filename, long among) throws IOException {
        LinkedHashMap<String, Double> dictionary = new LinkedHashMap<>();

        Path path = Paths.get("./resources/" + filename);
        Files.lines(path).limit(1000).forEach(s -> {
            String word = s.substring(0, s.indexOf(','));
            double amount = Double.parseDouble(s.substring(s.indexOf(',') + 1));
            dictionary.put(word.toUpperCase(), amount);
        });

        for (String word : dictionary.keySet()) {
            dictionary.put(word, dictionary.get(word) * 100.0 / (double) among);
        }

        return dictionary;
    }
}
