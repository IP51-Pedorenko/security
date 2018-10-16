package edu.pedorenko.security.lab1.genetic_algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Dictionary {

    Map<Character, Integer> LETTER_FREQUENCY = new LinkedHashMap<Character, Integer>() {{
        put('E', 120);
        put('T', 91);
        put('A', 81);
        put('O', 77);
        put('I', 73);
        put('N', 69);
        put('S', 63);
        put('R', 60);
        put('H', 59);
        put('D', 43);
        put('L', 40);
        put('U', 29);
        put('C', 27);
        put('M', 26);
        put('F', 23);
        put('Y', 21);
        put('W', 21);
        put('G', 20);
        put('P', 19);
        put('B', 15);
        put('V', 11);
        put('K', 7);
        put('X', 1);
        put('Q', 1);
        put('J', 1);
        put('Z', 1);
    }};
}
