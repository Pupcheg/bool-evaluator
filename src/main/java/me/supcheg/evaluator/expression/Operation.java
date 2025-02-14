package me.supcheg.evaluator.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Operation {
    AND("&"),
    OR("|"),
    EQUAL("="),
    NOT_EQUAL("!="),
    LESS("<"),
    GREATER(">"),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">=");

    private final String stringRepresentation;
}
