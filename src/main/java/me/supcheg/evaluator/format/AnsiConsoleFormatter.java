package me.supcheg.evaluator.format;

public class AnsiConsoleFormatter implements ConsoleFormatter {
    private static final String UNDERLINED = "\033[4m";
    private static final String RESET = "\033[0m";

    @Override
    public String makeBold(String text) {
        return UNDERLINED + text + RESET;
    }
}
