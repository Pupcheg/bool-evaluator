package me.supcheg.evaluator;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;
import me.supcheg.evaluator.expression.step.render.StepRenderer;
import me.supcheg.evaluator.expression.walk.SequentalExpressionTreeWalker;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class EvaluatorApplication implements Runnable {
    private final Evaluator evaluator = new Evaluator();
    private final InputStream in;
    private final PrintStream out;

    @Override
    public void run() {
        out.print("Enter the expression: ");

        String expression = new Scanner(in).nextLine();

        ExpressionTree tree = evaluator.evaluate(expression);

        printSteps(tree);
    }

    private void printSteps(ExpressionTree tree) {
        StepPrintingVisitor visitor = new StepPrintingVisitor();
        SequentalExpressionTreeWalker walker = new SequentalExpressionTreeWalker();
        walker.walk(tree, visitor);

        StepRenderer stepRenderer = new StepRenderer();

        visitor.getSteps().stream()
                .map(stepRenderer::renderToString)
                .forEach(out::println);
    }
}
