package me.supcheg.evaulator.expression.walk;

import me.supcheg.evaulator.expression.ExpressionTree;

public interface ExpressionTreeWalker {
    void walk(ExpressionTree tree, ExpressionTreeVisitor visitor);
}
