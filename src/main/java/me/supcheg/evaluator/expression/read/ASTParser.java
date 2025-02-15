package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.LogicalNode;
import me.supcheg.evaluator.expression.node.ScalarNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;

import java.util.List;

@RequiredArgsConstructor
public class ASTParser {
    private final TokenTypeConverter tokenTypeConverter;
    private final List<Token> tokens;
    private int pos = 0;

    public ExpressionTree parse() {
        return new ExpressionTree(lookOR());
    }

    private LogicalNode lookOR() {
        LogicalNode left = lookAND();
        while (isInBounds() && peek().getType() == TokenType.OR) {
            Token operation = next();
            LogicalNode right = lookAND();
            left = new ExpressionNode(left, tokenTypeConverter.toOperation(operation.getType()), right);
        }
        return left;
    }

    private LogicalNode lookAND() {
        LogicalNode left = lookLogical();
        while (isInBounds() && peek().getType() == TokenType.AND) {
            Token operation = next();
            LogicalNode right = lookLogical();
            left = new ExpressionNode(left, tokenTypeConverter.toOperation(operation.getType()), right);
        }
        return left;
    }

    private LogicalNode lookLogical() {
        Token token = peek();
        switch (token.getType()) {
            case OPEN_BRACKET:
                next(TokenType.OPEN_BRACKET);
                LogicalNode node = lookOR();
                next(TokenType.CLOSE_BRACKET);
                return node;

            case VARIABLE:
            case CONSTANT:
                return nextComparison();

            default:
                throw new ParseException("Unsupported token: " + token);
        }
    }

    private ComparisonNode nextComparison() {
        ScalarNode left;
        Operation operation;
        ScalarNode right;

        if (peek().getType() == TokenType.VARIABLE) {
            left = nextVariable();
            operation = nextOperation();
            right = nextConstant();
        } else {
            left = nextConstant();
            right = nextVariable();
            operation = nextOperation();
        }

        return new ComparisonNode(left, operation, right);
    }

    private VariableNode nextVariable() {
        return new VariableNode(next(TokenType.VARIABLE).getLexeme().charAt(0));
    }

    private Operation nextOperation() {
        return tokenTypeConverter.toOperation(next().getType());
    }

    private ConstantNode nextConstant() {
        return new ConstantNode(Integer.parseInt(next(TokenType.CONSTANT).getLexeme()));
    }

    private boolean isInBounds() {
        return pos < tokens.size();
    }

    private Token peek() {
        if (!isInBounds()) {
            throw new ParseException("Expected any token, but no any present at " + pos);
        }

        return tokens.get(pos);
    }

    private Token next(TokenType expected) {
        Token token = next();
        if (token.getType() != expected) {
            throw new ParseException("Expected " + expected + " but got " + token);
        }
        return token;
    }

    private Token next() {
        if (pos + 1 > tokens.size()) {
            throw new ParseException("Expected any token, but reached the end at " + pos);
        }

        return tokens.get(pos++);
    }
}
