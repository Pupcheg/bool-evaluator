package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.exception.WrongTokenException;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeLookup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class Lexer {
    private final TokenTypeLookup tokenTypeLookup;
    private final String input;
    private int pos = 0;

    public List<Token> tokenize() throws SyntaxException {
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

            throw new WrongTokenException(pos, pos + 1);
        }

        return Collections.unmodifiableList(tokens);
    }

    private Token nextConstantToken() {
        int start = pos;
        String lexeme = buildLexemeWhile(Character::isDigit);
        return new Token(TokenType.CONSTANT, lexeme, start, pos);
    }

    private Token nextVariableToken() {
        int start = pos;
        int end = pos + 1;
        pos++;
        return new Token(TokenType.VARIABLE, input.substring(start, end), start, end);
    }

    private Token nextOperatorToken() throws SyntaxException {
        int start = pos;

        String lexeme = buildLexemeWhile(Lexer::isOperatorElement);

        try {
            return new Token(tokenTypeLookup.operatorToken(lexeme), lexeme, start, pos);
        } catch (NullPointerException ex) {
            throw new WrongTokenException(String.format("Unknown operator '%s'", lexeme), start, pos);
        }
    }

    private Token nextBracketToken() throws SyntaxException {
        int start = pos;
        String lexeme = buildLexemeWhile(Lexer::isBracket);

        try {
            return new Token(tokenTypeLookup.bracketTokenType(lexeme), lexeme, start, pos);
        } catch (NullPointerException ex) {
            throw new WrongTokenException(String.format("Unknown bracket: %s", lexeme), start, pos);
        }
    }

    private String buildLexemeWhile(Predicate<Character> predicate) {
        StringBuilder lexeme = new StringBuilder();

        char cursor;
        while (isInBounds() && predicate.test(cursor = cursor())) {
            lexeme.append(cursor);
            pos++;
        }

        return lexeme.toString();
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
