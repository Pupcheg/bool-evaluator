package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeLookup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class Lexer {
    private final TokenTypeLookup tokenTypeLookup;
    private final String input;
    private int pos = 0;

    public List<Token> tokenize() {
        List<Token> tokens = new LinkedList<>();

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

            throw new LexerException(String.format("Unknown character '%s' at %d", ch, pos));
        }

        return Collections.unmodifiableList(tokens);
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
