package me.supcheg.evaluator.expression.step;

import me.supcheg.evaluator.Evaluator;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static me.supcheg.evaluator.expression.walk.ExpressionTreeWalkers.sequentalExpressionTreeWalker;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StepPrintingVisitorTests {
    static Evaluator EVALUATOR;

    @BeforeAll
    static void setup() {
        EVALUATOR = new Evaluator();
    }

    @Test
    void oneStepTest() throws SyntaxException {
        assertEquals(
                List.of(
                        Step.builder()
                                .number(1)
                                .left(new SimpleOperand("A"))
                                .operation(ComparisonOperation.LESS)
                                .right(new SimpleOperand("5"))
                                .build()
                ),
                sequentalExpressionTreeWalker()
                        .walk(EVALUATOR.evaluate("A < 5"), new StepPrintingVisitor())
                        .getSteps()
        );
    }
}
