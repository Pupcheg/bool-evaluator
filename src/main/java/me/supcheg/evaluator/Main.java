package me.supcheg.evaluator;

public class Main {
    public static void main(String[] args) {
        while (true) {
            new EvaluatorApplication(System.in, System.out, System.err).run();
        }
    }
}