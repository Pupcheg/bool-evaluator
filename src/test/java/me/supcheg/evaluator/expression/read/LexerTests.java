package me.supcheg.evaluator.expression.read;

import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeLookup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTests {

    LexerFactory factory;

    @BeforeEach
    void setup() {
        factory = new LexerFactory(new TokenTypeLookup());
    }

    @Test
    void listTokensTest() throws SyntaxException {
        assertEquals(
                List.of(
                        new Token(TokenType.VARIABLE, "A", 0, 1),
                        new Token(TokenType.GREATER, ">", 1, 2),
                        new Token(TokenType.CONSTANT, "5", 2, 3),
                        new Token(TokenType.AND, "&", 4, 5),
                        new Token(TokenType.VARIABLE, "B", 6, 7),
                        new Token(TokenType.EQUAL_LESS, "<=", 7, 9),
                        new Token(TokenType.CONSTANT, "3", 9, 10),
                        new Token(TokenType.AND, "&", 11, 12),
                        new Token(TokenType.VARIABLE, "A", 13, 14),
                        new Token(TokenType.EQUAL, "=", 14, 15),
                        new Token(TokenType.CONSTANT, "2", 15, 16)
                ),
                factory.createLexer("A>5 & B<=3 & A=2").tokenize()
        );
    }

    @Test
    void trailingSymbolTest() throws SyntaxException {
        assertEquals(
                List.of(
                        new Token(TokenType.VARIABLE, "A", 0, 1),
                        new Token(TokenType.LESS, "<", 1, 2),
                        new Token(TokenType.CONSTANT, "5", 2, 3),
                        new Token(TokenType.GREATER, ">", 3, 4)
                ),
                factory.createLexer("A<5>").tokenize()
        );
    }

    @Test
    void noSymbolTest() throws SyntaxException {
        assertEquals(
                List.of(),
                factory.createLexer("").tokenize()
        );
    }
}
