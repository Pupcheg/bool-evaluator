package me.supcheg.evaluator.expression.read;

import me.supcheg.evaluator.expression.Operation;

public class TokenTypeConverter {
    public Operation toOperation(TokenType type) {
        switch (type) {
            case AND:
                return Operation.AND;
            case OR:
                return Operation.OR;
            case EQUAL:
                return Operation.EQUAL;
            case NOT_EQUAL:
                return Operation.NOT_EQUAL;
            case LESS:
                return Operation.LESS;
            case GREATER:
                return Operation.GREATER;
            case EQUAL_LESS:
                return Operation.LESS_EQUAL;
            case EQUAL_GREATER:
                return Operation.GREATER_EQUAL;
            default:
                throw new IllegalArgumentException("Unexpected token type " + type);
        }
    }
}
