package edu.pedorenko.security.lab1.genetic_algorithm.genome;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MonoalphabeticKey implements edu.pedorenko.security.lab1.genetic_algorithm.genome.Key {

    private static Random random = new Random(System.currentTimeMillis());

    private Map<Integer, Integer> key = new HashMap<>();

    private double fitness;

    private MonoalphabeticKey() {
    }

    public MonoalphabeticKey(Map<Integer, Integer> key) {
        this.key = key;
    }

    public void mutate(int mutationProb) {

        for (int letter : key.keySet()) {

            if ((random.nextInt(1000) + 1) <= mutationProb) {
                int newLetter = random.nextInt(26) + 65;

                int replaceKey = 0;
                for (int replaseKey : key.keySet()) {
                    if (key.get(replaseKey) == newLetter) {
                        replaceKey = replaseKey;
                        break;
                    }
                }

                if (replaceKey == 0) {
                    throw new RuntimeException("Bad key " + this.key);
                }

                int oldLetter = key.get(letter);

                key.put(replaceKey, oldLetter);
                key.put(letter, newLetter);
            }
        }
    }

    public MonoalphabeticKey clone() {
        MonoalphabeticKey clone = new MonoalphabeticKey();

        clone.key = new HashMap<>(key);

        return clone;
    }

    public char decryptLetter(int letterId, char letter) {
        return (char)(int) key.get((int) letter);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
