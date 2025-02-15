package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.read.token.TokenTypeLookup;

@RequiredArgsConstructor
public class LexerFactory {
    private final TokenTypeLookup tokenTypeLookup;

    public Lexer createLexer(String expression) {
        return new Lexer(tokenTypeLookup, expression);
    }
}
