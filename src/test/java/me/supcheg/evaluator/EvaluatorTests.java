package me.supcheg.evaluator;

import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.step.SimpleOperand;
import me.supcheg.evaluator.expression.step.Step;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;
import me.supcheg.evaluator.expression.walk.SequentalExpressionTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvaluatorTests {

    Evaluator evaluator;

    @BeforeEach
    void setup() {
        evaluator = new Evaluator();
    }

    @Test
    void run() {
        String expression = "A>5 & B<=3 & A=2";

        ExpressionTree tree = evaluator.evaluate(expression);

        StepPrintingVisitor visitor = new StepPrintingVisitor();
        SequentalExpressionTreeWalker walker = new SequentalExpressionTreeWalker();
        walker.walk(tree, visitor);

        Step firstStep = simpleStep(1, "A", Operation.GREATER, 5);
        Step secondStep = simpleStep(2, "B", Operation.LESS_EQUAL, 3);
        Step thirdStep = referenceStep(3, firstStep, Operation.AND, secondStep);
        Step fourthStep = simpleStep(4, "A", Operation.EQUAL, 2);
        Step fifthStep = referenceStep(5, thirdStep, Operation.AND, fourthStep);

        assertEquals(
                List.of(
                        firstStep,
                        secondStep,
                        thirdStep,
                        fourthStep,
                        fifthStep
                ),
                visitor.getSteps()
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
