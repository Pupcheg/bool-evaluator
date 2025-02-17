package me.supcheg.evaluator.expression.operation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BooleanOperation implements Operation {
    AND("&"),
    OR("|");

    private final String stringRepresentation;
}
