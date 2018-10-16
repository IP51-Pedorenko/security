package edu.pedorenko.security.lab1;

import edu.pedorenko.security.lab1.genetic_algorithm.Dictionary;
import edu.pedorenko.security.lab1.genetic_algorithm.genome.Key;
import edu.pedorenko.security.lab1.genetic_algorithm.genome.PolyalphabeticKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubstitutionCipherHelper {

    private SubstitutionCipherHelper() {
    }

    public static String decrypt(String encryptedString, Key key) {

        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (char c : encryptedString.toCharArray()) {
            sb.append(key.decryptLetter(index, c));
            index++;
        }
        return sb.toString();
    }

    public static Key getInitialKey(String encryptedString) {
        int keySize= getKeyLength(encryptedString);
        List<Map<Integer, Integer>> initialKeyList = new ArrayList<>(keySize);

        for (int i = 0; i < keySize; ++i) {

            StringBuilder sb = new StringBuilder();
            for (int j = i; j < encryptedString.length() - keySize + 1; j += keySize) {
                sb.append(encryptedString.charAt(j));
            }

            Map<Integer, Integer> initialKeyMap = getInitialKeyMap(sb.toString());
            initialKeyList.add(initialKeyMap);
        }

        return new PolyalphabeticKey(initialKeyList);
    }

    private static int getKeyLength(String encryptedString) {
        int firstMatch = -1;
        int keyLength = 0;
        for (int i = 0; i < encryptedString.length() - 1; ++i) {
            keyLength++;
            int coincidences = getCoincidencesNumber(encryptedString.substring(i + 1), encryptedString);
            System.out.println(keyLength + " " + coincidences / (double) encryptedString.length());

            if (firstMatch != -1 && coincidences / (double) encryptedString.length() >= 0.05) {
                return keyLength - firstMatch;
            }

            if (coincidences / (double) encryptedString.length() >= 0.05) {
                firstMatch = keyLength;
            }
        }
        return keyLength;
    }

    private static int getCoincidencesNumber(String string1, String string2) {

        int coincidences = 0;
        for (int i = 0; i < Math.min(string1.length(), string2.length()); ++i) {
            if (string1.charAt(i) == string2.charAt(i)) {
                coincidences++;
            }
        }
        return coincidences;
    }

    private static Map<Integer, Integer> getInitialKeyMap(String encryptedString) {
        HashMap<Character, Integer> frequency = new HashMap<>();
        for (int i = 0; i < encryptedString.length(); ++i) {
            char letter = encryptedString.charAt(i);
            if (frequency.keySet().contains(letter)) {
                frequency.put(letter, frequency.get(letter) + 1);
            } else {
                frequency.put(letter, 1);
            }
        }

        List<Map.Entry<Character, Integer>> list = new ArrayList<>(frequency.entrySet());
        list.sort(Map.Entry.comparingByValue((o1, o2) -> o2 - o1));

        Map<Character, Integer> sortedFrequency = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list) {
            sortedFrequency.put(entry.getKey(), entry.getValue());
        }

        Map<Integer, Integer> initialKeyMap = new HashMap<>();

        ArrayList<Integer> letters = new ArrayList<>();
        for (int i = 65; i < 91; ++i) {
            letters.add(i);
        }

        List<Map.Entry<Character, Integer>> sortedEncryptedFrequencyList = new ArrayList<>(sortedFrequency.entrySet());
        List<Map.Entry<Character, Integer>> sortedRealFrequencyList = new ArrayList<>(Dictionary.LETTER_FREQUENCY.entrySet());
        for (int i = 0; i < sortedRealFrequencyList.size(); ++i) {
            if (sortedEncryptedFrequencyList.size() > i) {
                initialKeyMap.put((int) sortedEncryptedFrequencyList.get(i).getKey(), (int) sortedRealFrequencyList.get(i).getKey());
                letters.remove((Integer)(int) sortedEncryptedFrequencyList.get(i).getKey());
            } else {
                initialKeyMap.put(letters.remove(0), (int) sortedRealFrequencyList.get(i).getKey());
            }
        }

        return initialKeyMap;
    }
}
