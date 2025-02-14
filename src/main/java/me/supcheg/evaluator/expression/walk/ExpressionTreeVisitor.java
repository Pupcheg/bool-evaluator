package me.supcheg.evaluator.expression.walk;

import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;

public interface ExpressionTreeVisitor {
    default void visitComparison(ComparisonNode node) {
    }

    default void visitExpression(ExpressionNode node) {
    }
}
