package me.supcheg.evaluator.expression.node;

import lombok.Data;
import me.supcheg.evaluator.expression.Operation;

@Data
public class RightVariableComparisonNode implements ComparisonNode {
    private final ConstantNode left;
    private final Operation operation;
    private final VariableNode right;

    @Override
    public VariableNode getVariable() {
        return right;
    }

    @Override
    public ConstantNode getConstant() {
        return left;
    }
}
