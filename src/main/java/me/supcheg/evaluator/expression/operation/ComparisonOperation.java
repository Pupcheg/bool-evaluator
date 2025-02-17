package me.supcheg.evaluator.expression.operation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ComparisonOperation implements Operation {
    EQUAL("="),
    NOT_EQUAL("!="),
    LESS("<"),
    GREATER(">"),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">=");

    private final String stringRepresentation;
}
