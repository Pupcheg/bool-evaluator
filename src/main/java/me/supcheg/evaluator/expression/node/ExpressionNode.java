package me.supcheg.evaluator.expression.node;

import lombok.Data;
import me.supcheg.evaluator.expression.Operation;

@Data
public class ExpressionNode implements LogicalNode {
    private final LogicalNode left;
    private final Operation operation;
    private final LogicalNode right;
}
