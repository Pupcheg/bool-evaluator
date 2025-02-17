package me.supcheg.evaluator.expression.read.token;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.read.exception.NotEndException;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedEndException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedTokenException;

import java.util.List;

@RequiredArgsConstructor
public class TokenStream {
    private final List<Token> tokens;
    private int pos = 0;

    public Token peek() throws SyntaxException {
        if (!isInBounds()) {
            throw new UnexpectedEndException();
        }
        return tokens.get(pos);
    }

    public Token next() throws SyntaxException {
        Token token = peek();
        pos++;
        return token;
    }

    public Token next(TokenType expectedType) throws SyntaxException {
        Token token = next();
        if (token.getType() != expectedType) {
            throw new UnexpectedTokenException(expectedType, token);
        }
        return token;
    }

    public boolean isInBounds() {
        return pos < tokens.size();
    }

    public void assertIsEnd() throws SyntaxException {
        if (isInBounds()) {
            throw new NotEndException();
        }
    }
}
