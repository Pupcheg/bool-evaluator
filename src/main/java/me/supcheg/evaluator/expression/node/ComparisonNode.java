package me.supcheg.evaluator.expression.node;

import me.supcheg.evaluator.expression.operation.ComparisonOperation;

public interface ComparisonNode extends LogicalNode {
    @Override
    ScalarNode getLeft();

    @Override
    ComparisonOperation getOperation();

    @Override
    ScalarNode getRight();

    VariableNode getVariable();

    ConstantNode getConstant();
}
