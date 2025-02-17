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
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedTokenException;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenStream;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;

import java.util.List;

@RequiredArgsConstructor
public class ASTParser {
    private final TokenTypeConverter tokenTypeConverter;
    private final TokenStream tokenStream;

    public ASTParser(TokenTypeConverter tokenTypeConverter, List<Token> tokens) {
        this(
                tokenTypeConverter,
                new TokenStream(tokens)
        );
    }

    public ExpressionTree parse() throws SyntaxException {
        LogicalNode root = parseLogicalOR();
        tokenStream.assertIsEnd();
        return new ExpressionTree(root);
    }

    private LogicalNode parseLogicalOR() throws SyntaxException {
        return parseLogical(TokenType.OR, this::parseLogicalAND);
    }

    private LogicalNode parseLogicalAND() throws SyntaxException {
        return parseLogical(TokenType.AND, this::parseAnyLogical);
    }

    private LogicalNode parseAnyLogical() throws SyntaxException {
        Token token = tokenStream.peek();
        switch (token.getType()) {
            case OPEN_BRACKET:
                tokenStream.next(TokenType.OPEN_BRACKET);
                LogicalNode node = parseLogicalOR();
                tokenStream.next(TokenType.CLOSE_BRACKET);
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
        while (tokenStream.isInBounds() && tokenStream.peek().getType() == operator) {
            left = new ExpressionNode(
                    left,
                    nextBooleanOperation(),
                    downstream.parse()
            );
        }
        return left;
    }

    private ComparisonNode nextComparison() throws SyntaxException {
        if (tokenStream.peek().getType() == TokenType.VARIABLE) {
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
        Token next = tokenStream.next(TokenType.VARIABLE);
        return new VariableNode(next.getLexeme().charAt(0));
    }

    private BooleanOperation nextBooleanOperation() throws SyntaxException {
        Token next = tokenStream.next();
        try {
            return tokenTypeConverter.toBooleanOperation(next.getType());
        } catch (TokenTypeConverter.OperationNotFoundException ex) {
            throw new UnexpectedTokenException("BOOLEAN_OPERATION", next);
        }
    }

    private ComparisonOperation nextComparisonOperation() throws SyntaxException {
        Token next = tokenStream.next();
        try {
            return tokenTypeConverter.toComparisonOperation(next.getType());
        } catch (TokenTypeConverter.OperationNotFoundException ex) {
            throw new UnexpectedTokenException("COMPARISON_OPERATION", next);
        }
    }

    private ConstantNode nextConstant() throws SyntaxException {
        Token next = tokenStream.next(TokenType.CONSTANT);
        return new ConstantNode(Integer.parseInt(next.getLexeme()));
    }

    @FunctionalInterface
    private interface ParseDownstream {
        LogicalNode parse() throws SyntaxException;
    }
}
