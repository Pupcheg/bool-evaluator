package me.supcheg.evaluator.expression.node;

import lombok.Data;

@Data
public class VariableNode implements ScalarNode {
    private final char variable;

    @Override
    public Object getScalarValue() {
        return variable;
    }
}
