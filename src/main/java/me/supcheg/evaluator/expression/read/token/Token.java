package me.supcheg.evaluator.expression.read.token;

import lombok.Data;

@Data
public class Token {
    private final TokenType type;
    private final String lexeme;
    private final int start;
    private final int end;
}
