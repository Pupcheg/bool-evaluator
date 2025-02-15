package me.supcheg.evaluator.expression.step;

import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.Node;
import me.supcheg.evaluator.expression.node.ScalarNode;
import me.supcheg.evaluator.expression.walk.ExpressionTreeVisitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StepPrintingVisitor implements ExpressionTreeVisitor {
    private final List<Step> steps = new LinkedList<>();
    private final Map<Node, Step> stepByNode = new HashMap<>();
    private int stepCounter = 0;

    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    @Override
    public void visitExpression(ExpressionNode node) {
        stepCounter++;

        Step step = Step.builder()
                .number(stepCounter)
                .left(stepByNode(node.getLeft()))
                .operation(node.getOperation())
                .right(stepByNode(node.getRight()))
                .build();

        addStep(node, step);
    }

    @Override
    public void visitComparison(ComparisonNode node) {
        stepCounter++;

        Step step = Step.builder()
                .number(stepCounter)
                .left(asSimpleOperand(node.getLeft()))
                .operation(node.getOperation())
                .right(asSimpleOperand(node.getRight()))
                .build();

        addStep(node, step);
    }

    private Step addStep(Node associatedNode, Step step) {
        steps.add(step);
        stepByNode.put(associatedNode, step);
        return step;
    }

    private Step stepByNode(Node node) {
        return Objects.requireNonNull(stepByNode.get(node), "Unknown step: " + node);
    }

    private SimpleOperand asSimpleOperand(ScalarNode node) {
        return new SimpleOperand(String.valueOf(node.getScalarValue()));
    }
}
