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
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
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
        LogicalNode root = lookOR();
        assertIsEnd();
        return new ExpressionTree(root);
    }

    private LogicalNode lookOR() throws SyntaxException {
        LogicalNode left = lookAND();
        while (isInBounds() && peek().getType() == TokenType.OR) {
            BooleanOperation operation = nextBooleanOperation();
            LogicalNode right = lookAND();
            left = new ExpressionNode(left, operation, right);
        }
        return left;
    }

    private LogicalNode lookAND() throws SyntaxException {
        LogicalNode left = lookLogical();
        while (isInBounds() && peek().getType() == TokenType.AND) {
            BooleanOperation operation = nextBooleanOperation();
            LogicalNode right = lookLogical();
            left = new ExpressionNode(left, operation, right);
        }
        return left;
    }

    private LogicalNode lookLogical() throws SyntaxException {
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
                throw new UnexpectedTokenException("OPERATION", token);
        }
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
            throw new UnexpectedTokenException("OPERATION", next);
        }
    }

    private ComparisonOperation nextComparisonOperation() throws SyntaxException {
        Token next = next();
        try {
            return tokenTypeConverter.toComparisonOperation(next.getType());
        } catch (TokenTypeConverter.OperationNotFoundException ex) {
            throw new UnexpectedTokenException("OPERATION", next);
        }
    }

    private ConstantNode nextConstant() throws SyntaxException {
        Token next = next(TokenType.CONSTANT);
        return new ConstantNode(Integer.parseInt(next.getLexeme()));
    }

    private boolean isInBounds() {
        return pos < tokens.size();
    }

    private void assertInBounds(int pos) throws SyntaxException {
        if (pos > tokens.size()) {
            throw new UnexpectedEndException();
        }
    }

    private Token peek() throws SyntaxException {
        assertInBounds(pos);
        return tokens.get(pos);
    }

    private Token next(TokenType expectedType) throws SyntaxException {
        Token token = next();
        if (token.getType() != expectedType) {
            throw new UnexpectedTokenException(expectedType, token);
        }
        return token;
    }

    private Token next() throws SyntaxException {
        assertInBounds(pos + 1);
        return tokens.get(pos++);
    }

    private void assertIsEnd() throws SyntaxException {
        if (pos < tokens.size()) {
            throw new NotEndException();
        }
    }
}
