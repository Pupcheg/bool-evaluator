package me.supcheg.evaulator.expression.walk;

import me.supcheg.evaulator.expression.node.ComparisonNode;
import me.supcheg.evaulator.expression.node.ExpressionNode;

public interface ExpressionTreeVisitor {
    default void visitComparison(ComparisonNode node) {
    }

    default void visitExpression(ExpressionNode node) {
    }
}
