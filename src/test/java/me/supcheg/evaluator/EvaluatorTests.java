package me.supcheg.evaluator;

import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
import me.supcheg.evaluator.expression.operation.Operation;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.step.SimpleOperand;
import me.supcheg.evaluator.expression.step.Step;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static me.supcheg.evaluator.expression.walk.ExpressionTreeWalkers.sequentalExpressionTreeWalker;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvaluatorTests {

    Evaluator evaluator;

    @BeforeEach
    void setup() {
        evaluator = new Evaluator();
    }

    @Test
    void run() throws SyntaxException {
        String expression = "A>5 & B<=3 & A=2";

        ExpressionTree tree = evaluator.evaluate(expression);

        Step firstStep = simpleStep(1, "A", ComparisonOperation.GREATER, 5);
        Step secondStep = simpleStep(2, "B", ComparisonOperation.LESS_EQUAL, 3);
        Step thirdStep = referenceStep(3, firstStep, BooleanOperation.AND, secondStep);
        Step fourthStep = simpleStep(4, "A", ComparisonOperation.EQUAL, 2);
        Step fifthStep = referenceStep(5, thirdStep, BooleanOperation.AND, fourthStep);

        assertEquals(
                List.of(
                        firstStep,
                        secondStep,
                        thirdStep,
                        fourthStep,
                        fifthStep
                ),
                sequentalExpressionTreeWalker()
                        .walk(tree, new StepPrintingVisitor())
                        .getSteps()
        );
    }

    private Step simpleStep(int num, Object left, Operation operation, Object right) {
        return Step.builder()
                .number(num)
                .left(new SimpleOperand(String.valueOf(left)))
                .operation(operation)
                .right(new SimpleOperand(String.valueOf(right)))
                .build();
    }

    private Step referenceStep(int num, Step left, Operation operation, Step right) {
        return Step.builder()
                .number(num)
                .left(left)
                .operation(operation)
                .right(right)
                .build();
    }
}
