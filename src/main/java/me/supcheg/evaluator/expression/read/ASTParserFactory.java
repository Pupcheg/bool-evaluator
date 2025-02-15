package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;

import java.util.List;

@RequiredArgsConstructor
public class ASTParserFactory {
    private final TokenTypeConverter tokenTypeConverter;

    public ASTParser createASTParser(List<Token> tokens) {
        return new ASTParser(tokenTypeConverter, tokens);
    }
}
