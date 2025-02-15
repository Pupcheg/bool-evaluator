package me.supcheg.evaluator;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;
import me.supcheg.evaluator.expression.step.render.StepRenderer;
import me.supcheg.evaluator.expression.walk.SequentalExpressionTreeWalker;
import me.supcheg.evaluator.format.AnsiConsoleFormatter;
import me.supcheg.evaluator.format.ConsoleFormatter;
import me.supcheg.evaluator.message.ExpressionMessageContext;
import me.supcheg.evaluator.message.ExpressionMessageProvider;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class EvaluatorApplication implements Runnable {
    private final Evaluator evaluator = new Evaluator();
    private final ConsoleFormatter consoleFormatter = new AnsiConsoleFormatter();
    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    @Override
    public void run() {
        out.print("Enter the expression: ");

        String expression = new Scanner(in).nextLine();

        ExpressionTree tree;
        try {
            tree = evaluator.evaluate(expression);
        } catch (Exception ex) {
            handleException(ex, expression);
            return;
        }

        printSteps(tree);
    }

    private void handleException(Exception ex, String expression) {
        if (ex instanceof ExpressionMessageProvider) {
            ExpressionMessageProvider messageProvider = (ExpressionMessageProvider) ex;

            ExpressionMessageContext ctx
                    = new ExpressionMessageContext(expression, consoleFormatter);
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
