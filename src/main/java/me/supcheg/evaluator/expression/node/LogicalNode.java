package me.supcheg.evaluator.expression.node;

import me.supcheg.evaluator.expression.operation.Operation;

public interface LogicalNode extends Node {
    Node getLeft();

    Operation getOperation();

    Node getRight();
}
