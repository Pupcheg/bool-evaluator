package me.supcheg.evaluator.expression.node;

import lombok.Data;

@Data
public class ConstantNode implements ScalarNode {
    private final int value;

    public Object getScalarValue() {
        return value;
    }
}
