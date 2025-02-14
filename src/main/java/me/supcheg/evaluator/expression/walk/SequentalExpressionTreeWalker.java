package me.supcheg.evaulator.expression.walk;

import me.supcheg.evaulator.expression.ExpressionTree;
import me.supcheg.evaulator.expression.node.ComparisonNode;
import me.supcheg.evaulator.expression.node.ExpressionNode;
import me.supcheg.evaulator.expression.node.Node;

public class SequentalExpressionTreeWalker implements ExpressionTreeWalker {
    @Override
    public void walk(ExpressionTree tree, ExpressionTreeVisitor visitor) {
        walk(tree.getRoot(), visitor);
    }

    private void walk(Node node, ExpressionTreeVisitor visitor) {
        if (node instanceof ExpressionNode) {
            walk((ExpressionNode) node, visitor);
        } else if (node instanceof ComparisonNode) {
            walk((ComparisonNode) node, visitor);
        }
    }

    private void walk(ExpressionNode node, ExpressionTreeVisitor visitor) {
        walk(node.getLeft(), visitor);
        walk(node.getRight(), visitor);
        visitor.visitExpression(node);
    }

    private static void walk(ComparisonNode node, ExpressionTreeVisitor visitor) {
        visitor.visitComparison(node);
    }
}
