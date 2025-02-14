package me.supcheg.evaluator;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.read.Lexer;
import me.supcheg.evaluator.expression.read.ASTParser;
import me.supcheg.evaluator.expression.read.Token;
import me.supcheg.evaluator.expression.step.render.StepRenderer;
import me.supcheg.evaluator.expression.walk.SequentalExpressionTreeWalker;
import me.supcheg.evaluator.expression.step.StepPrintingVisitor;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class EvaluatorApplication implements Runnable {
    private final InputStream in;
    private final PrintStream out;

    @Override
    public void run() {
        out.print("Enter the expression: ");

        String expression = new Scanner(in).nextLine();

        List<Token> tokens = new Lexer(expression).listTokens();
        ExpressionTree tree = new ASTParser(tokens).parse();

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
