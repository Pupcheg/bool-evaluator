package me.supcheg.evaluator.expression.node;

import lombok.Data;
import me.supcheg.evaluator.expression.Operation;

@Data
public class ComparisonNode implements LogicalNode {
    private final ScalarNode left;
    private final Operation operation;
    private final ScalarNode right;
}
