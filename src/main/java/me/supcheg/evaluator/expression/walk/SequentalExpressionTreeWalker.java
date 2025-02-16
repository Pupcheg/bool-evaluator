package me.supcheg.evaluator.expression.walk;

import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.Node;

enum SequentalExpressionTreeWalker implements ExpressionTreeWalker {
    INSTANCE;

    @Override
    public <V extends ExpressionTreeVisitor> V walk(ExpressionTree tree, V visitor) {
        walk(tree.getRoot(), visitor);
        return visitor;
    }

    private void walk(Node node, ExpressionTreeVisitor visitor) {
        if (node instanceof ExpressionNode) {
            walk((ExpressionNode) node, visitor);
        } else if (node instanceof ComparisonNode) {
            walk((ComparisonNode) node, visitor);
        }
    }

    private void walk(ExpressionNode node, ExpressionTreeVisitor visitor) {
        visitor.preVisitExpression(node);
        walk(node.getLeft(), visitor);
        walk(node.getRight(), visitor);
        visitor.visitExpression(node);
    }

    private static void walk(ComparisonNode node, ExpressionTreeVisitor visitor) {
        visitor.visitComparison(node);
    }
}
