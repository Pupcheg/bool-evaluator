package me.supcheg.evaluator.expression.step;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.operation.Operation;

@Builder
@RequiredArgsConstructor
@Data
public class Step implements Operand {
    private final int number;
    private final Operand left;
    private final Operation operation;
    private final Operand right;
}
