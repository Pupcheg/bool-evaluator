package me.supcheg.evaluator.expression.step;

import me.supcheg.evaluator.Evaluator;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.operation.Operation;
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
                listSteps("A < 5")
        );
    }

    @Test
    void manyStepsTest() throws SyntaxException {

        Step firstStep = step(1, "A", ComparisonOperation.GREATER, 5);
        Step secondStep = step(2, "B", ComparisonOperation.LESS_EQUAL, 3);
        Step thirdStep = step(3, firstStep, BooleanOperation.AND, secondStep);
        Step fourthStep = step(4, "A", ComparisonOperation.EQUAL, 2);
        Step fifthStep = step(5, thirdStep, BooleanOperation.AND, fourthStep);

        assertEquals(
                List.of(
                        firstStep,
                        secondStep,
                        thirdStep,
                        fourthStep,
                        fifthStep
                ),
                listSteps("A>5 & B<=3 & A=2")
        );
    }

    private static List<Step> listSteps(String expression) throws SyntaxException {
        return sequentalExpressionTreeWalker()
                .walk(EVALUATOR.evaluate(expression), new StepPrintingVisitor())
                .getSteps();
    }

    private static Step step(int num, Object left, Operation operation, Object right) {
        return Step.builder()
                .number(num)
                .left(asOperand(left))
                .operation(operation)
                .right(asOperand(right))
                .build();
    }

    private static Operand asOperand(Object obj) {
        if (obj instanceof Operand) {
            return (Operand) obj;
        }

        return new SimpleOperand(String.valueOf(obj));
    }
}
