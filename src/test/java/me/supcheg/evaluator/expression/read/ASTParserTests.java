package me.supcheg.evaluator.expression.read;

import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.node.LeftVariableComparisonNode;
import me.supcheg.evaluator.expression.node.VariableNode;
import me.supcheg.evaluator.expression.operation.BooleanOperation;
import me.supcheg.evaluator.expression.operation.ComparisonOperation;
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
        // A>5 & B<=3 & A=2
        assertEquals(
                new ExpressionTree(
                        new ExpressionNode(
                                new ExpressionNode(
                                        new LeftVariableComparisonNode(
                                                new VariableNode('A'),
                                                ComparisonOperation.GREATER,
                                                new ConstantNode(5)
                                        ),
                                        BooleanOperation.AND,
                                        new LeftVariableComparisonNode(
                                                new VariableNode('B'),
                                                ComparisonOperation.LESS_EQUAL,
                                                new ConstantNode(3)
                                        )
                                ),
                                BooleanOperation.AND,
                                new LeftVariableComparisonNode(
                                        new VariableNode('A'),
                                        ComparisonOperation.EQUAL,
                                        new ConstantNode(2)
                                )
                        )
                ),
                factory.createASTParser(
                        List.of(
                                new Token(TokenType.VARIABLE, "A", 0, 1),
                                new Token(TokenType.GREATER, ">", 1, 2),
                                new Token(TokenType.CONSTANT, "5", 2, 3),
                                new Token(TokenType.AND, "&", 4, 5),
                                new Token(TokenType.VARIABLE, "B", 6, 7),
                                new Token(TokenType.EQUAL_LESS, "<=", 7, 9),
                                new Token(TokenType.CONSTANT, "3", 9, 10),
                                new Token(TokenType.AND, "&", 11, 12),
                                new Token(TokenType.VARIABLE, "A", 13, 14),
                                new Token(TokenType.EQUAL, "=", 14, 15),
                                new Token(TokenType.CONSTANT, "2", 15, 16)
                        )
                ).parse()
        );
    }

    @Test
    void trailingTokenTest() {
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

    @Test
    void noTokenTest() {
        ASTParser parser = factory.createASTParser(List.of());

        assertThrows(
                SyntaxException.class,
                parser::parse
        );
    }
}
