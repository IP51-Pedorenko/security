package edu.pedorenko.security.lab1.genetic_algorithm.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PolyalphabeticKey implements Key {

    private static Random random = new Random(System.currentTimeMillis());

    private List<Map<Integer, Integer>> key = new ArrayList<>();

    private double fitness;

    private PolyalphabeticKey() {
    }

    public PolyalphabeticKey(List<Map<Integer, Integer>> key) {
        this.key = key;
    }

    public void mutate(int mutationProb) {

        for (Map<Integer, Integer> keyElem : key) {

            for (int letter : keyElem.keySet()) {

                if ((random.nextInt(1000) + 1) <= mutationProb) {
                    int newLetter = random.nextInt(26) + 65;

                    int replaceKey = 0;
                    for (int replaseKey : keyElem.keySet()) {
                        if (keyElem.get(replaseKey) == newLetter) {
                            replaceKey = replaseKey;
                            break;
                        }
                    }

                    if (replaceKey == 0) {
                        throw new RuntimeException("Bad key " + this.key);
                    }

                    int oldLetter = keyElem.get(letter);

                    keyElem.put(replaceKey, oldLetter);
                    keyElem.put(letter, newLetter);
                }
            }
        }
    }

    public PolyalphabeticKey clone() {
        PolyalphabeticKey clone = new PolyalphabeticKey();

        ArrayList<Map<Integer, Integer>> newKey = new ArrayList<>();

        for (Map<Integer, Integer> keyElem : key) {
            newKey.add(new HashMap<>(keyElem));
        }

        clone.key = newKey;

        return clone;
    }

    public char decryptLetter(int letterId, char letter) {
        return (char)(int) key.get(letterId % key.size()).get((int) letter);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
