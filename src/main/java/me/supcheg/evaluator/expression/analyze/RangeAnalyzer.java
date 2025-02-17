package me.supcheg.evaluator.expression.analyze;

import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
import me.supcheg.evaluator.expression.walk.ExpressionTreeVisitor;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RangeAnalyzer implements ExpressionTreeVisitor {
    private final Map<VariableNode, VariableRange> rangeByVariable;
    private final Deque<BooleanOperation> expressionOperations;

    public RangeAnalyzer() {
        this.rangeByVariable = new HashMap<>();
        this.expressionOperations = new LinkedList<>();
    }

    @Override
    public void preVisitExpression(ExpressionNode node) {
        expressionOperations.addLast(node.getOperation());
    }

    @Override
    public void visitComparison(ComparisonNode node) {
        VariableNode variable = node.getVariable();

        List<Interval> intervals = createIntervals(
                node.getOperation(),
                node.getConstant().getValue()
        );
        BooleanOperation operation = getCurrentExpressionOperation();

        getVariableRange(variable)
                .addIntervals(intervals, operation);
    }

    @Override
    public void visitExpression(ExpressionNode node) {
        expressionOperations.removeLast();
    }

    private BooleanOperation getCurrentExpressionOperation() {
        return expressionOperations.isEmpty() ? BooleanOperation.OR : expressionOperations.peekLast();
    }

    private VariableRange getVariableRange(VariableNode variable) {
        return rangeByVariable.computeIfAbsent(variable, __ -> new VariableRange());
    }

    private static List<Interval> createIntervals(ComparisonOperation operation, int value) {
        switch (operation) {
            case GREATER:
                return List.of(new Interval(value + 1, Interval.POSITIVE_INFINITY));
            case LESS:
                return List.of(new Interval(Interval.NEGATIVE_INFINITY, value - 1));
            case GREATER_EQUAL:
                return List.of(new Interval(value, Interval.POSITIVE_INFINITY));
            case LESS_EQUAL:
                return List.of(new Interval(Interval.NEGATIVE_INFINITY, value));
            case EQUAL:
                return List.of(new Interval(value, value));
            case NOT_EQUAL:
                return List.of(
                        new Interval(Interval.NEGATIVE_INFINITY, value - 1),
                        new Interval(value + 1, Interval.POSITIVE_INFINITY)
                );
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    public AnalyzeResult getResult() {
        if (
                rangeByVariable.values()
                        .stream()
                        .anyMatch(VariableRange::isAlwaysFalse)
        ) {
            return AnalyzeResult.ALWAYS_FALSE;
        }

        if (
                rangeByVariable.values()
                        .stream()
                        .allMatch(VariableRange::isAlwaysTrue)
        ) {
            return AnalyzeResult.ALWAYS_TRUE;
        }

        return AnalyzeResult.NOT_DETERMINED;
    }
}
