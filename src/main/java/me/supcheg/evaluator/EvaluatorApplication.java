package me.supcheg.evaluator;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.analyze.AnalyzeResult;
import me.supcheg.evaluator.expression.analyze.RangeAnalyzer;
import me.supcheg.evaluator.expression.step.Step;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;
import me.supcheg.evaluator.expression.step.render.StepRenderer;
import me.supcheg.evaluator.format.AnsiConsoleFormatter;
import me.supcheg.evaluator.format.ConsoleFormatter;
import me.supcheg.evaluator.message.ExpressionMessageContext;
import me.supcheg.evaluator.message.ExpressionMessageProvider;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static me.supcheg.evaluator.expression.walk.ExpressionTreeWalkers.sequentalExpressionTreeWalker;

@RequiredArgsConstructor
public class EvaluatorApplication implements Runnable {
    private final Evaluator evaluator = new Evaluator();
    private final ConsoleFormatter consoleFormatter = new AnsiConsoleFormatter();
    private final StepRenderer stepRenderer = new StepRenderer();
    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    @Override
    public void run() {
        out.print("Enter the expression: ");

        String expression = new Scanner(in).nextLine();

        long start = System.nanoTime();

        ExpressionTree tree;
        try {
            tree = evaluator.evaluate(expression);
        } catch (Exception ex) {
            handleException(ex, expression);
            return;
        }

        List<Step> steps = makeSteps(tree);
        AnalyzeResult analyzeResult = analyze(tree);

        long end = System.nanoTime();

        printSteps(steps);
        printAnalyzeResult(analyzeResult);

        printTime(end - start);
    }

    private void handleException(Exception ex, String expression) {
        if (ex instanceof ExpressionMessageProvider) {
            ExpressionMessageProvider messageProvider = (ExpressionMessageProvider) ex;

            ExpressionMessageContext ctx = new ExpressionMessageContext(expression, consoleFormatter);
            String message;
            try {
                message = messageProvider.makeMessage(ctx);
            } catch (Exception suppressed) {
                ex.addSuppressed(suppressed);
                ex.printStackTrace(err);
                return;
            }

            err.println(message);
            return;
        }

        ex.printStackTrace(err);
    }

    private void printAnalyzeResult(AnalyzeResult analyzeResult) {
        System.out.printf("Result: %s%n", analyzeResult.getStringRepresentation());
    }

    private void printTime(long nanos) {
        out.printf("Spent time: %d ms%n", TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    private List<Step> makeSteps(ExpressionTree tree) {
        return sequentalExpressionTreeWalker()
                .walk(tree, new StepPrintingVisitor())
                .getSteps();
    }

    private AnalyzeResult analyze(ExpressionTree tree) {
        return sequentalExpressionTreeWalker()
                .walk(tree, new RangeAnalyzer())
                .getResult();
    }

    private void printSteps(Iterable<Step> steps) {
        for (Step step : steps) {
            System.out.println(stepRenderer.renderToString(step));
        }
    }
}
