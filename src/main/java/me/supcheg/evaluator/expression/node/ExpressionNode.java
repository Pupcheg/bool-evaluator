package me.supcheg.evaulator.expression.node;

import lombok.Data;
import me.supcheg.evaulator.expression.Operation;

@Data
public class ExpressionNode implements LogicalNode {
    private final Node left;
    private final Operation operation;
    private final Node right;
}
