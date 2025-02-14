package me.supcheg.evaulator;

import me.supcheg.evaulator.expression.read.Lexer;
import me.supcheg.evaulator.expression.read.Parser;
import me.supcheg.evaulator.expression.walk.SequentalExpressionTreeWalker;
import me.supcheg.evaulator.expression.walk.visitor.StepPrintingVisitor;

public class Main {
    public static void main(String[] args) {

        StepPrintingVisitor stepPrintingVisitor = new StepPrintingVisitor();
        new SequentalExpressionTreeWalker()
                .walk(new Parser(
                                new Lexer("A > 5 & B <= 3 | (7 = C | 2 = A)")
                                        .listTokens()
                        )
                        .parse(), stepPrintingVisitor
                );

        for (String step : stepPrintingVisitor.getSteps()) {
            System.out.println(step);
        }
    }
}