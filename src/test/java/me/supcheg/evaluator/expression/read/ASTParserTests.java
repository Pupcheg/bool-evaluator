package me.supcheg.evaluator.expression.read;

import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.LeftVariableComparisonNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.read.exception.SyntaxException;
import me.supcheg.evaluator.expression.read.token.Token;
import me.supcheg.evaluator.expression.read.token.TokenType;
import me.supcheg.evaluator.expression.read.token.TokenTypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ASTParserTests {

    ASTParserFactory factory;

    @BeforeEach
    void setup() {
        factory = new ASTParserFactory(new TokenTypeConverter());
    }

    @Test
    void parseTest() throws SyntaxException {
        assertEquals(
                new ExpressionTree(
                        new ExpressionNode(
                                new LeftVariableComparisonNode(
                                        new VariableNode('A'),
                                        Operation.GREATER,
                                        new ConstantNode(5)
                                ),
                                Operation.AND,
                                new ExpressionNode(
                                        new LeftVariableComparisonNode(
                                                new VariableNode('B'),
                                                Operation.LESS_EQUAL,
                                                new ConstantNode(3)
                                        ),
                                        Operation.AND,
                                        new LeftVariableComparisonNode(
                                                new VariableNode('A'),
                                                Operation.EQUAL,
                                                new ConstantNode(2)
                                        )
                                )
                        )
                ),
                factory.createASTParser(
                        List.of(
                                new Token(TokenType.VARIABLE, "A", 0, 1),
                                new Token(TokenType.GREATER, ">", 1, 2),
                                new Token(TokenType.CONSTANT, "5", 2, 3),
                                new Token(TokenType.AND, "&", 4, 5),
                                new Token(TokenType.OPEN_BRACKET, "(", 6, 7),
                                new Token(TokenType.VARIABLE, "B", 7, 8),
                                new Token(TokenType.EQUAL_LESS, "<=", 8, 10),
                                new Token(TokenType.CONSTANT, "3", 10, 11),
                                new Token(TokenType.AND, "&", 12, 13),
                                new Token(TokenType.VARIABLE, "A", 14, 15),
                                new Token(TokenType.EQUAL, "=", 15, 16),
                                new Token(TokenType.CONSTANT, "2", 16, 17),
                                new Token(TokenType.CLOSE_BRACKET, ")", 19, 20)
                        )
                ).parse()
        );
    }

    @Test
    void trailingSymbolTest() {
        ASTParser parser = factory.createASTParser(
                List.of(
                        new Token(TokenType.VARIABLE, "A", 0, 1),
                        new Token(TokenType.LESS, "<", 1, 2),
                        new Token(TokenType.CONSTANT, "5", 2, 3),
                        new Token(TokenType.GREATER, ">", 3, 4)
                )
        );
        assertThrows(
                SyntaxException.class,
                parser::parse
        );
    }
}
