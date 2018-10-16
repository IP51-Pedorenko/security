package edu.pedorenko.security.lab1.genetic_algorithm;

import edu.pedorenko.security.lab1.SubstitutionCipherHelper;
import edu.pedorenko.security.lab1.genetic_algorithm.genome.Key;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class Population {

    private ArrayList<Key> keys;

    private String encryptedString;

    private int mutationProb;

    private Map<String, Double> dictionary;

    private int currentGenerationId = 1;

    private double totalFitness;

    private double bestGenerationMaxFitness = -1;

    private long bestGenerationId;


    public Population(int size, int mutationProb, String encryptedString, Map<String, Double> dictionary, Key superParent) {
        keys = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            Key child = superParent.clone();
            child.mutate(mutationProb);
            keys.add(child);
        }

        this.mutationProb = mutationProb;
        this.encryptedString = encryptedString;
        this.dictionary = dictionary;
    }

    public boolean evaluate() {

        totalFitness = 0;

        Stream.of(keys.toArray()).parallel().forEach(key -> evaluate((Key) key));

        double minFitness = 0;
        for (Key key : keys) {
            if (key.getFitness() < minFitness) {
                minFitness = key.getFitness();
            }
        }

        for (Key key : keys) {
            key.setFitness(key.getFitness() - minFitness);
        }

        for (Key key : keys) {
            totalFitness += key.getFitness();
        }

        Key bestKey = getBestKey();

        if (bestGenerationMaxFitness != -1) {

            if (bestKey.getFitness() > bestGenerationMaxFitness) {
                bestGenerationMaxFitness = bestKey.getFitness();
                bestGenerationId = currentGenerationId;

            } else if (currentGenerationId - bestGenerationId >= 100) {

                bestGenerationId = currentGenerationId;


                bestGenerationMaxFitness = -1;

                return false;
            }

        } else {
            bestGenerationMaxFitness = bestKey.getFitness();
            bestGenerationId = currentGenerationId;
        }

        return true;
    }

    private void evaluate(Key key) {

        String decryptedString = SubstitutionCipherHelper.decrypt(encryptedString, key);

        long fitness = getFitness(decryptedString, dictionary);

        key.setFitness(fitness);
    }

    private long getFitness(String string, Map<String, Double> dictionary) {

        double fitness = 0;

        for (String word : dictionary.keySet()) {

            fitness += fillFrequency(string, word, dictionary.get(word));
        }

        return (long) fitness;
    }

    private double fillFrequency(String decryptedString, String dictionaryString, double score) {

        double fitness = 0;

        String temp = decryptedString;
        while (temp.contains(dictionaryString)) {

            fitness += score * dictionaryString.length() * dictionaryString.length();

            if (temp.length() > temp.indexOf(dictionaryString) + dictionaryString.length()) {
                temp = temp.substring(temp.indexOf(dictionaryString) + dictionaryString.length());
            } else {
                break;
            }
        }

        return fitness;
    }

    public void makeNewPopulation() {

        currentGenerationId++;

        ArrayList<Key> newPopulation = new ArrayList<>(keys.size());

        Key bestKey = getBestKey();

        Key perfectCopy = bestKey.clone();
        newPopulation.add(perfectCopy);

        for (int i = 1; i < keys.size(); ++i) {

            Key child = bestKey.clone();

            child.mutate(mutationProb);

            newPopulation.add(child);
        }

        keys = newPopulation;
    }


    public Key getBestKey() {
        Key bestKey = keys.get(0);
        double bestFitness = bestKey.getFitness();
        for (Key key : keys) {
            double fitness = key.getFitness();
            if (fitness > bestFitness) {
                bestKey = key;
                bestFitness = fitness;
            }
        }
        return bestKey;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public void setDictionary(Map<String, Double> dictionary) {
        this.dictionary = dictionary;
    }
}
