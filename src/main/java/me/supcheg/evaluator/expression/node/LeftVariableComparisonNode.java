package me.supcheg.evaluator.expression.node;

import lombok.Data;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;

@Data
public class LeftVariableComparisonNode implements ComparisonNode {
    private final VariableNode left;
    private final ComparisonOperation operation;
    private final ConstantNode right;

    @Override
    public VariableNode getVariable() {
        return left;
    }

    @Override
    public ConstantNode getConstant() {
        return right;
    }
}
