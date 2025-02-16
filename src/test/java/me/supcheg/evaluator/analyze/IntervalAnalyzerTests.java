package me.supcheg.evaluator.analyze;

import me.supcheg.evaluator.Evaluator;
import me.supcheg.evaluator.expression.analyze.AnalyzeResult;
import me.supcheg.evaluator.expression.analyze.RangeAnalyzer;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.walk.SequentalExpressionTreeWalker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntervalAnalyzerTests {

    static Evaluator EVALUATOR;

    @BeforeAll
    static void setup() {
        EVALUATOR = new Evaluator();
    }

    @Test
    void notDeterminedTest() throws SyntaxException {
        assertEquals(
                AnalyzeResult.NOT_DETERMINED,
                analyze("A<5")
        );
    }

    @Test
    void alwaysFalseTest() throws SyntaxException {
        assertEquals(
                AnalyzeResult.ALWAYS_FALSE,
                analyze("A>5 & (B<=3 & A=2)")
        );
    }

    @Test
    void alwaysTrueTest() throws SyntaxException {
        assertEquals(
                AnalyzeResult.ALWAYS_TRUE,
                analyze("(A=3 | A>3 | A<3) & (B <= -10 | B > -10)")
        );
    }

    @Test
    void alwaysTrueMixedTest() throws SyntaxException {
        assertEquals(
                AnalyzeResult.ALWAYS_TRUE,
                analyze("A>3 | (B<3 | A<=3) | B>=3")
        );
    }

    AnalyzeResult analyze(String expression) throws SyntaxException {
        RangeAnalyzer rangeAnalyzer = new RangeAnalyzer();

        SequentalExpressionTreeWalker.INSTANCE.walk(EVALUATOR.evaluate(expression), rangeAnalyzer);

        return rangeAnalyzer.getResult();
    }
}
