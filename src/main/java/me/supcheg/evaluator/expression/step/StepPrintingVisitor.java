package me.supcheg.evaluator.expression.walk.visitor;

import lombok.Getter;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.Node;
import me.supcheg.evaluator.expression.node.ScalarNode;
import me.supcheg.evaluator.expression.walk.ExpressionTreeVisitor;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StepPrintingVisitor implements ExpressionTreeVisitor {
    @Getter
    private final List<String> steps = new LinkedList<>();
    private final Map<Node, Integer> knownNodes = new IdentityHashMap<>();
    private int stepCounter = 0;

    @Override
    public void visitExpression(ExpressionNode node) {
        stepCounter++;
        knownNodes.put(node, stepCounter);

        steps.add(
                String.format("%d) [%d] %s [%d]",
                        stepCounter,
                        stepNumber(node.getLeft()),
                        node.getOperation().getStringRepresentation(),
                        stepNumber(node.getRight())
                )
        );
    }

    @Override
    public void visitComparison(ComparisonNode node) {
        stepCounter++;
        knownNodes.put(node, stepCounter);

        steps.add(
                String.format("%d) %s %s %s",
                        stepCounter,
                        stringValue(node.getLeft()),
                        node.getOperation().getStringRepresentation(),
                        stringValue(node.getRight())
                )
        );
    }

    private int stepNumber(Node node) {
        return Objects.requireNonNull(knownNodes.get(node), "Unknown step: " + node);
    }

    private String stringValue(ScalarNode node) {
        return String.valueOf(node.value());
    }
}
