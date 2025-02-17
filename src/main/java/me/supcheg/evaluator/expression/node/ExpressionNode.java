package me.supcheg.evaluator.expression.node;

import lombok.Data;
import me.supcheg.evaluator.expression.operation.BooleanOperation;

@Data
public class ExpressionNode implements LogicalNode {
    private final LogicalNode left;
    private final BooleanOperation operation;
    private final LogicalNode right;
}
