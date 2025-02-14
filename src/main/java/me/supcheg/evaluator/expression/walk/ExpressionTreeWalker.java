package me.supcheg.evaluator.expression.walk;

import me.supcheg.evaluator.expression.ExpressionTree;

public interface ExpressionTreeWalker {
    void walk(ExpressionTree tree, ExpressionTreeVisitor visitor);
}
