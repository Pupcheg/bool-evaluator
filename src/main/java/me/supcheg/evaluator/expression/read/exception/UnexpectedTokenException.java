package me.supcheg.evaluator.expression.read.exception;

import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;

public class UnexpectedTokenException extends SyntaxException {

    public UnexpectedTokenException(TokenType expectedType, Token token) {
        this(expectedType.toString(), token);
    }

    public UnexpectedTokenException(String expectedType, Token token) {
        super(
                String.format(
                        "Expected %s at %d to %d, but got %s",
                        expectedType,
                        token.getStart(), token.getEnd(),
                        token.getType()
                ),
                token.getStart(),
                token.getEnd()
        );
    }
}
