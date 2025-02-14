package me.supcheg.evaulator.expression.node;

import lombok.Data;
import me.supcheg.evaulator.expression.Operation;

@Data
public class ComparisonNode implements LogicalNode {
    private final ScalarNode left;
    private final Operation operation;
    private final ScalarNode right;
}
