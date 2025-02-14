package me.supcheg.evaluator.expression.step;

import lombok.Data;

@Data
public class SimpleOperand implements Operand {
    private final String value;
}
