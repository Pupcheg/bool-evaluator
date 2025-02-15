package me.supcheg.evaluator.expression.read.token;

import java.util.Map;
import java.util.Objects;

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

    public TokenType operatorToken(String raw) {
        return Objects.requireNonNull(operators.get(raw), "Unknown operator: " + raw);
    }

    public TokenType bracketTokenType(String raw) {
        return Objects.requireNonNull(brackets.get(raw), "Unknown bracket: " + raw);
    }
}
