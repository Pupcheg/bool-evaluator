package me.supcheg.evaluator.expression.read.token;

import lombok.experimental.StandardException;
import me.supcheg.evaluator.expression.Operation;

import java.util.Map;

public class TokenTypeConverter {
    private final Map<TokenType, Operation> operationByTokenType;

    public TokenTypeConverter() {
        this.operationByTokenType = Map.of(
                TokenType.AND, Operation.AND,
                TokenType.OR, Operation.OR,
                TokenType.EQUAL, Operation.EQUAL,
                TokenType.NOT_EQUAL, Operation.NOT_EQUAL,
                TokenType.LESS, Operation.LESS,
                TokenType.EQUAL_LESS, Operation.LESS_EQUAL,
                TokenType.GREATER, Operation.GREATER,
                TokenType.EQUAL_GREATER, Operation.GREATER_EQUAL
        );
    }

    public Operation toOperation(TokenType type) {
        Operation operation = operationByTokenType.get(type);
        if (operation == null) {
            throw new OperationNotFoundException("Unknown token type: " + type);
        }
        return operation;
    }

    @StandardException
    public static class OperationNotFoundException extends RuntimeException {
    }
}
