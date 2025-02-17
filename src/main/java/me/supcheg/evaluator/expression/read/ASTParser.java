package me.supcheg.evaluator.expression.read;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.LeftVariableComparisonNode;
import me.supcheg.evaluator.expression.node.LogicalNode;
import me.supcheg.evaluator.expression.node.RightVariableComparisonNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.read.exception.NotEndException;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedEndException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedTokenException;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;

import java.util.List;

@RequiredArgsConstructor
public class ASTParser {
    private final TokenTypeConverter tokenTypeConverter;
    private final List<Token> tokens;
    private int pos = 0;

    public ExpressionTree parse() throws SyntaxException {
        LogicalNode root = parseLogicalOR();
        assertIsEnd();
        return new ExpressionTree(root);
    }

    private LogicalNode parseLogicalOR() throws SyntaxException {
        return parseLogical(TokenType.OR, this::parseLogicalAND);
    }

    private LogicalNode parseLogicalAND() throws SyntaxException {
        return parseLogical(TokenType.AND, this::parseAnyLogical);
    }

    private LogicalNode parseAnyLogical() throws SyntaxException {
        Token token = peek();
        switch (token.getType()) {
            case OPEN_BRACKET:
                next(TokenType.OPEN_BRACKET);
                LogicalNode node = parseLogicalOR();
                next(TokenType.CLOSE_BRACKET);
                return node;

            case VARIABLE:
            case CONSTANT:
                return nextComparison();

            default:
                throw new UnexpectedTokenException(
                        String.format("%s, %s or %s", TokenType.OPEN_BRACKET, TokenType.VARIABLE, TokenType.CONSTANT),
                        token
                );
        }
    }

    private LogicalNode parseLogical(TokenType operator, ParseDownstream downstream) throws SyntaxException {
        LogicalNode left = downstream.parse();
        while (isInBounds() && peek().getType() == operator) {
            left = new ExpressionNode(
                    left,
                    nextBooleanOperation(),
                    downstream.parse()
            );
        }
        return left;
    }

    private ComparisonNode nextComparison() throws SyntaxException {
        if (peek().getType() == TokenType.VARIABLE) {
            return new LeftVariableComparisonNode(
                    nextVariable(),
                    nextComparisonOperation(),
                    nextConstant()
            );
        } else {
            return new RightVariableComparisonNode(
                    nextConstant(),
                    nextComparisonOperation(),
                    nextVariable()
            );
        }
    }

    private VariableNode nextVariable() throws SyntaxException {
        Token next = next(TokenType.VARIABLE);
        return new VariableNode(next.getLexeme().charAt(0));
    }

    private BooleanOperation nextBooleanOperation() throws SyntaxException {
        Token next = next();
        try {
            return tokenTypeConverter.toBooleanOperation(next.getType());
        } catch (TokenTypeConverter.OperationNotFoundException ex) {
            throw new UnexpectedTokenException("BOOLEAN_OPERATION", next);
        }
    }

    private ComparisonOperation nextComparisonOperation() throws SyntaxException {
        Token next = next();
        try {
            return tokenTypeConverter.toComparisonOperation(next.getType());
        } catch (TokenTypeConverter.OperationNotFoundException ex) {
            throw new UnexpectedTokenException("COMPARISON_OPERATION", next);
        }
    }

    private ConstantNode nextConstant() throws SyntaxException {
        Token next = next(TokenType.CONSTANT);
        return new ConstantNode(Integer.parseInt(next.getLexeme()));
    }

    private Token next(TokenType expectedType) throws SyntaxException {
        Token token = next();
        if (token.getType() != expectedType) {
            throw new UnexpectedTokenException(expectedType, token);
        }
        return token;
    }

    private Token next() throws SyntaxException {
        Token peek = peek();
        pos++;
        return peek;
    }

    private Token peek() throws SyntaxException {
        if (!isInBounds()) {
            throw new UnexpectedEndException();
        }
        return tokens.get(pos);
    }

    private boolean isInBounds() {
        return pos < tokens.size();
    }

    private void assertIsEnd() throws SyntaxException {
        if (pos < tokens.size()) {
            throw new NotEndException();
        }
    }

    @FunctionalInterface
    private interface ParseDownstream {
        LogicalNode parse() throws SyntaxException;
    }
}
