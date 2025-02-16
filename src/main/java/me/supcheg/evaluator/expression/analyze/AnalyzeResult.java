package me.supcheg.evaluator.expression.analyze;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AnalyzeResult {
    ALWAYS_TRUE("Always true"),
    ALWAYS_FALSE("Always false"),
    NOT_DETERMINED("Not determined");

    private final String stringRepresentation;
}
