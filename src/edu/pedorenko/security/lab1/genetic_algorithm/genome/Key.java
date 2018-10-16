package edu.pedorenko.security.lab1.genetic_algorithm.genome;

public interface Key {

    void mutate(int mutationProb);

    Key clone();

    char decryptLetter(int letterId, char letter);

    double getFitness();

    void setFitness(double fitness);
}
