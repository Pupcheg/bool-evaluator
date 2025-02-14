package me.supcheg.evaulator.expression.read;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Lexer {
    private final String input;
    private final TokenTypeLookup tokenTypeLookup;
    private int pos = 0;

    public Lexer(String input) {
        this(input, new TokenTypeLookup());
    }

    public List<Token> listTokens() {
        List<Token> tokens = new ArrayList<>();

        while (isInBounds()) {
            char ch = cursor();

            if (Character.isWhitespace(ch)) {
                pos++;
                continue;
            }

            if (Character.isDigit(ch)) {
                tokens.add(nextConstantToken());
                continue;
            }

            if (isVariable(ch)) {
                tokens.add(nextVariableToken());
                continue;
            }

            if (isOperatorElement(ch)) {
                tokens.add(nextOperatorToken());
                continue;
            }

            if (isBracket(ch)) {
                tokens.add(nextBracketToken());
                continue;
            }

            throw new IllegalArgumentException("Unrecognized token: " + ch + " at position " + pos);
        }

        return tokens;
    }

    private Token nextConstantToken() {
        StringBuilder lexeme = new StringBuilder();
        while (isInBounds() && Character.isDigit(cursor())) {
            lexeme.append(cursor());
            pos++;
        }
        return new Token(TokenType.CONSTANT, lexeme.toString());
    }

    private Token nextVariableToken() {
        pos++;
        return new Token(TokenType.VARIABLE, input.substring(pos - 1, pos));
    }

    private Token nextOperatorToken() {
        StringBuilder lexemeBuilder = new StringBuilder();
        while (isInBounds() && isOperatorElement(cursor())) {
            lexemeBuilder.append(cursor());
            pos++;
        }
        String lexeme = lexemeBuilder.toString();

        return new Token(tokenTypeLookup.operatorToken(lexeme), lexeme);
    }

    private Token nextBracketToken() {
        StringBuilder lexemeBuilder = new StringBuilder();
        while (isInBounds() && isBracket(cursor())) {
            lexemeBuilder.append(cursor());
            pos++;
        }
        String lexeme = lexemeBuilder.toString();

        return new Token(tokenTypeLookup.bracketTokenType(lexeme), lexeme);
    }

    private boolean isInBounds() {
        return pos < input.length();
    }

    private char cursor() {
        return input.charAt(pos);
    }

    private static boolean isVariable(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private static boolean isOperatorElement(char ch) {
        switch (ch) {
            case '>':
            case '<':
            case '=':
            case '!':
            case '&':
            case '|':
                return true;
            default:
                return false;
        }
    }

    private static boolean isBracket(char ch) {
        return ch == '(' || ch == ')';
    }
}
