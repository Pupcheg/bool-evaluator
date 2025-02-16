package me.supcheg.evaluator.expression.read.token;

import lombok.experimental.StandardException;

import java.util.Map;

public class TokenTypeLookup {
    private final Map<String, TokenType> operators;
    private final Map<String, TokenType> brackets;

    public TokenTypeLookup() {
        this.operators = Map.of(
                ">", TokenType.GREATER,
                ">=", TokenType.EQUAL_GREATER,
                "<", TokenType.LESS,
                "<=", TokenType.EQUAL_LESS,
                "=", TokenType.EQUAL,
                "!=", TokenType.NOT_EQUAL,
                "&", TokenType.AND,
                "|", TokenType.OR
        );
        this.brackets = Map.of(
                "(", TokenType.OPEN_BRACKET,
                ")", TokenType.CLOSE_BRACKET
        );
    }

    public TokenType findOperatorTokenType(String raw) {
        TokenType tokenType = operators.get(raw);
        if (tokenType == null) {
            throw new UnknownTokenTypeException("Unknown operator: " + raw);
        }
        return tokenType;
    }

    public TokenType findBracketTokenType(String raw) {
        TokenType tokenType = brackets.get(raw);
        if (tokenType == null) {
            throw new UnknownTokenTypeException("Unknown bracket: " + raw);
        }
        return tokenType;
    }

    @StandardException
    public static class UnknownTokenTypeException extends RuntimeException {
    }
}
