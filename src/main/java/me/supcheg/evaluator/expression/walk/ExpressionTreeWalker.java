package me.supcheg.evaluator.expression.walk;

import me.supcheg.evaluator.expression.ExpressionTree;

public interface ExpressionTreeWalker {
    <V extends ExpressionTreeVisitor> V walk(ExpressionTree tree, V visitor);
}
