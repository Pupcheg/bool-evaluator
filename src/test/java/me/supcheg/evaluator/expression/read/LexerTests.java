package me.supcheg.evaluator.expression.read;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTests {

    @Test
    void listTokensTest() {
        assertEquals(
                List.of(
                        new Token(TokenType.VARIABLE, "A"),
                        new Token(TokenType.GREATER, ">"),
                        new Token(TokenType.CONSTANT, "5"),
                        new Token(TokenType.AND, "&"),
                        new Token(TokenType.OPEN_BRACKET, "("),
                        new Token(TokenType.VARIABLE, "B"),
                        new Token(TokenType.EQUAL_LESS, "<="),
                        new Token(TokenType.CONSTANT, "3"),
                        new Token(TokenType.AND, "&"),
                        new Token(TokenType.VARIABLE, "A"),
                        new Token(TokenType.EQUAL, "="),
                        new Token(TokenType.CONSTANT, "2"),
                        new Token(TokenType.CLOSE_BRACKET, ")")
                ),
                new Lexer("A>5 & (B<=3 & A=2)\n").listTokens()
        );
    }
}
