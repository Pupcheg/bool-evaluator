package me.supcheg.evaluator.expression.read.token;

import lombok.experimental.StandardException;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.operation.BooleanOperation;

import java.util.Map;

public class TokenTypeConverter {
    private final Map<TokenType, BooleanOperation> booleanOperationByTokenType;
    private final Map<TokenType, ComparisonOperation> comparisonOperationByTokenType;

    public TokenTypeConverter() {
        this.booleanOperationByTokenType = Map.of(
                TokenType.AND, BooleanOperation.AND,
                TokenType.OR, BooleanOperation.OR
        );
        this.comparisonOperationByTokenType = Map.of(
                TokenType.EQUAL, ComparisonOperation.EQUAL,
                TokenType.NOT_EQUAL, ComparisonOperation.NOT_EQUAL,
                TokenType.LESS, ComparisonOperation.LESS,
                TokenType.EQUAL_LESS, ComparisonOperation.LESS_EQUAL,
                TokenType.GREATER, ComparisonOperation.GREATER,
                TokenType.EQUAL_GREATER, ComparisonOperation.GREATER_EQUAL
        );
    }

    public BooleanOperation toBooleanOperation(TokenType type) {
        BooleanOperation operation = booleanOperationByTokenType.get(type);
        if (operation == null) {
            throw new OperationNotFoundException("Unknown token type: " + type);
        }
        return operation;
    }

    public ComparisonOperation toComparisonOperation(TokenType type) {
        ComparisonOperation operation = comparisonOperationByTokenType.get(type);
        if (operation == null) {
            throw new OperationNotFoundException("Unknown token type: " + type);
        }
        return operation;
    }

    @StandardException
    public static class OperationNotFoundException extends RuntimeException {
    }
}
