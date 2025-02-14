package me.supcheg.evaulator.expression.node;

import lombok.Data;

@Data
public class VariableNode implements ScalarNode {
    private final char variable;

    @Override
    public Object value() {
        return variable;
    }
}
