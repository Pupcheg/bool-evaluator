package me.supcheg.evaulator.expression.read;

import lombok.Data;

@Data
public class Token {
    private final TokenType type;
    private final String lexeme;
}
