package edu.pedorenko.security.lab1.task2;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Task2Decrypter {

    private static final String encryptedEncodedString = "1c41023f564b2a130824570e6b47046b521f3f5208201318245e0e6b40022643072e13183e51183f5a1f3e4702245d4b285a1b23561965133f2413192e571e28564b3f5b0e6b50042643072e4b023f4a4b24554b3f5b0238130425564b3c564b3c5a0727131e38564b245d0732131e3b430e39500a38564b27561f3f5619381f4b385c4b3f5b0e6b580e32401b2a500e6b5a186b5c05274a4b79054a6b67046b540e3f131f235a186b5c052e13192254033f130a3e470426521f22500a275f126b4a043e131c225f076b431924510a295f126b5d0e2e574b3f5c4b3e400e6b400426564b385c193f13042d130c2e5d0e3f5a086b52072c5c192247032613433c5b02285b4b3c5c1920560f6b47032e13092e401f6b5f0a38474b32560a391a476b40022646072a470e2f130a255d0e2a5f0225544b24414b2c410a2f5a0e25474b2f56182856053f1d4b185619225c1e385f1267131c395a1f2e13023f13192254033f13052444476b4a043e131c225f076b5d0e2e574b22474b3f5c4b2f56082243032e414b3f5b0e6b5d0e33474b245d0e6b52186b440e275f456b710e2a414b225d4b265a052f1f4b3f5b0e395689cbaa186b5d046b401b2a500e381d61";

    private static int getKeyLength(String encryptedString) {
        int keyLength = 0;
        for (int i = 0; i < encryptedString.length() - 1; ++i) {
            keyLength++;
            int coincidences = getCoincidencesNumber(encryptedString.substring(i + 1), encryptedString);
            if (coincidences / (double) encryptedString.length() >= 0.03) {
                break;
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

    private static int[] getKey(int keyLength, String encryptedString) {
        int[] key = new int[keyLength];
        for (int i = 0; i < keyLength; ++i) {
            Map<Character, Integer> map = new HashMap<>();
            for (int j = i; j < encryptedString.length(); j += keyLength) {
                char c = encryptedString.charAt(j);
                if (map.containsKey(c)) {
                    map.put(c, map.get(c) + 1);
                } else {
                    map.put(c, 1);
                }
            }

            char mostCommonChar = ' ';
            int mostCommonCharAmount = 0;
            for (char c : map.keySet()) {
                int charAmount = map.get(c);
                if (charAmount > mostCommonCharAmount) {
                    mostCommonChar = c;
                    mostCommonCharAmount = charAmount;
                }
            }

            key[i] = mostCommonChar ^ ' ';
        }

        return key;
    }

    private static String decryptWithKey(String encryptedString, int[] key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encryptedString.length(); ++i) {
            char character = encryptedString.charAt(i);
            char decryptedCharacter = (char) (character ^ key[i % 3]);
            sb.append(decryptedCharacter);
        }

        return sb.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encryptedEncodedString.length(); i+=2) {
            sb.append((char)Integer.parseInt(encryptedEncodedString.substring(i, i + 2), 16));
        }

        String encryptedDecodedString = sb.toString();

        int keyLength = getKeyLength(encryptedDecodedString);

        int[] key = getKey(keyLength, encryptedDecodedString);

        System.out.println("Key is " + Arrays.toString(key));
        System.out.println(decryptWithKey(encryptedDecodedString, key));
    }
}
