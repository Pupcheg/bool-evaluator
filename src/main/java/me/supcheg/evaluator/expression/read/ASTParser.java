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
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.exception.UnexpectedTokenException;
import me.supcheg.evaluator.expression.read.exception.WrongTokenException;
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
        return new ExpressionTree(lookOR());
    }

    private LogicalNode lookOR() throws SyntaxException {
        LogicalNode left = lookAND();
        while (isInBounds() && peek().getType() == TokenType.OR) {
            Token operation = next();
            LogicalNode right = lookAND();
            left = new ExpressionNode(left, tokenTypeConverter.toOperation(operation.getType()), right);
        }
        return left;
    }

    private LogicalNode lookAND() throws SyntaxException {
        LogicalNode left = lookLogical();
        while (isInBounds() && peek().getType() == TokenType.AND) {
            Token operation = next();
            LogicalNode right = lookLogical();
            left = new ExpressionNode(left, tokenTypeConverter.toOperation(operation.getType()), right);
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
        ScalarNode left;
        Operation operation;
        ScalarNode right;

        if (peek().getType() == TokenType.VARIABLE) {
            left = nextVariable();
            operation = nextOperation();
            right = nextConstant();
        } else {
            left = nextConstant();
            operation = nextOperation();
            right = nextVariable();
        }

        return new ComparisonNode(left, operation, right);
    }

    private VariableNode nextVariable() throws SyntaxException {
        Token next = next(TokenType.VARIABLE);
        return new VariableNode(next.getLexeme().charAt(0));
    }

    private Operation nextOperation() throws SyntaxException {
        return tokenTypeConverter.toOperation(next().getType());
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
            throw new WrongTokenException("Expected any token, but reached the end", pos - 2, pos - 1);
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
}
