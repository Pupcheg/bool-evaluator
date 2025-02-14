package me.supcheg.evaulator.expression.node;

import me.supcheg.evaulator.expression.Operation;

public interface LogicalNode extends Node {
    Node getLeft();

    Operation getOperation();

    Node getRight();
}
