package me.supcheg.evaluator.expression.node;

public interface ComparisonNode extends LogicalNode {
    @Override
    ScalarNode getLeft();

    @Override
    ScalarNode getRight();

    VariableNode getVariable();

    ConstantNode getConstant();
}
