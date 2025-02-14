package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.LogicalNode;
import me.supcheg.evaluator.expression.node.VariableNode;

import java.util.List;

@RequiredArgsConstructor
public class Parser {
    private final TokenTypeConverter tokenTypeConverter;
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this(new TokenTypeConverter(), tokens);
    }

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
        LogicalNode left = lookLOGICAL();
        while (isInBounds() && peek().getType() == TokenType.AND) {
            Token operation = next();
            LogicalNode right = lookLOGICAL();
            left = new ExpressionNode(left, tokenTypeConverter.toOperation(operation.getType()), right);
        }
        return left;
    }

    private LogicalNode lookLOGICAL() {
        Token token = peek();
        if (token.getType() == TokenType.OPEN_BRACKET) {
            next();
            LogicalNode node = lookOR();
            next();
            return node;
        } else if (token.getType() == TokenType.VARIABLE || token.getType() == TokenType.CONSTANT) {
            return nextComparison();
        }

        throw new UnsupportedOperationException("Unsupported token type: " + token);
    }

    private ComparisonNode nextComparison() {
        if (peek().getType() == TokenType.VARIABLE) {
            return new ComparisonNode(
                    nextVariable(),
                    nextOperation(),
                    nextConstant()
            );
        } else {
            return new ComparisonNode(
                    nextConstant(),
                    nextOperation(),
                    nextVariable()
            );
        }
    }

    private VariableNode nextVariable() {
        return new VariableNode(next().getLexeme().charAt(0));
    }

    private Operation nextOperation() {
        return tokenTypeConverter.toOperation(next().getType());
    }

    private ConstantNode nextConstant() {
        return new ConstantNode(Integer.parseInt(next().getLexeme()));
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

    private Token next() {
        if (pos + 1 > tokens.size()) {
            throw new ParseException("Expected any token, but reached the end at " + pos);
        }

        return tokens.get(pos++);
    }
}
