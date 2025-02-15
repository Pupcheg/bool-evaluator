package me.supcheg.evaluator.message;

import lombok.Data;
import me.supcheg.evaluator.format.ConsoleFormatter;

@Data
public class ExpressionMessageContext {
    private final String expression;
    private final ConsoleFormatter consoleFormatter;
}
