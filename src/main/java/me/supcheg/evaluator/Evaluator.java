package me.supcheg.evaluator;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.read.ASTParserFactory;
import me.supcheg.evaluator.expression.read.LexerFactory;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;
import me.supcheg.evaluator.expression.read.token.TokenTypeLookup;

import java.util.List;

@RequiredArgsConstructor
public class Evaluator {
    private final LexerFactory lexerFactory;
    private final ASTParserFactory astParserFactory;

    public Evaluator() {
        this(
                new LexerFactory(
                        new TokenTypeLookup()
                ),
                new ASTParserFactory(
                        new TokenTypeConverter()
                )
        );
    }

    public ExpressionTree evaluate(String expression) {
        return evaluate(tokenize(expression));
    }

    private List<Token> tokenize(String expression) {
        return lexerFactory.createLexer(expression).tokenize();
    }

    private ExpressionTree evaluate(List<Token> tokens) {
        return astParserFactory.createASTParser(tokens).parse();
    }
}
