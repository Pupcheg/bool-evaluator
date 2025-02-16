package me.supcheg.evaluator.expression.walk;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExpressionTreeWalkers {
    public static ExpressionTreeWalker sequentalExpressionTreeWalker() {
        return SequentalExpressionTreeWalker.INSTANCE;
    }
}
