package me.supcheg.evaluator.expression.analyze;

import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.walk.ExpressionTreeVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeAnalyzer implements ExpressionTreeVisitor {
    private final Map<VariableNode, VariableRange> rangeByVariable = new HashMap<>();
    private Operation previousOperation;
    private Operation currentOperation = Operation.OR; // by default for trees with one comparison node

    @Override
    public void preVisitExpression(ExpressionNode node) {
        previousOperation = currentOperation;
        currentOperation = node.getOperation();
    }

    @Override
    public void visitComparison(ComparisonNode node) {
        VariableNode variable = node.getVariable();

        List<Interval> intervals = createIntervals(
                node.getOperation(),
                node.getConstant().getValue()
        );
        rangeByVariable.computeIfAbsent(variable, __ -> new VariableRange())
                .addIntervals(intervals, currentOperation);
    }

    @Override
    public void visitExpression(ExpressionNode node) {
        currentOperation = previousOperation;
    }

    private static List<Interval> createIntervals(Operation op, int value) {
        switch (op) {
            case GREATER:
                return List.of(new Interval(value + 1, Interval.POSITIVE_INFINITY, true, false));
            case LESS:
                return List.of(new Interval(Interval.NEGATIVE_INFINITY, value - 1, false, true));
            case GREATER_EQUAL:
                return List.of(new Interval(value, Interval.POSITIVE_INFINITY, true, false));
            case LESS_EQUAL:
                return List.of(new Interval(Interval.NEGATIVE_INFINITY, value, false, true));
            case EQUAL:
                return List.of(new Interval(value, value, true, true));
            case NOT_EQUAL:
                return List.of(
                        new Interval(Interval.NEGATIVE_INFINITY, value, false, false),
                        new Interval(value, Interval.POSITIVE_INFINITY, false, false)
                );
            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
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
