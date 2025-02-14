package me.supcheg.evaluator.expression.read;

import me.supcheg.evaluator.expression.node.ComparisonNode;
import me.supcheg.evaluator.expression.node.ConstantNode;
import me.supcheg.evaluator.expression.node.ExpressionNode;
import me.supcheg.evaluator.expression.ExpressionTree;
import me.supcheg.evaluator.expression.Operation;
import me.supcheg.evaluator.expression.node.VariableNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTests {
    @Test
    void parseTest() {
        assertEquals(
                new ExpressionTree(
                        new ExpressionNode(
                                new ComparisonNode(
                                        new VariableNode('A'),
                                        Operation.GREATER,
                                        new ConstantNode(5)
                                ),
                                Operation.AND,
                                new ExpressionNode(
                                        new ComparisonNode(
                                                new VariableNode('B'),
                                                Operation.LESS_EQUAL,
                                                new ConstantNode(3)
                                        ),
                                        Operation.AND,
                                        new ComparisonNode(
                                                new VariableNode('A'),
                                                Operation.EQUAL,
                                                new ConstantNode(2)
                                        )
                                )
                        )
                ),
                new Parser(
                        List.of(
                                new Token(TokenType.VARIABLE, "A"),
                                new Token(TokenType.GREATER, ">"),
                                new Token(TokenType.CONSTANT, "5"),
                                new Token(TokenType.AND, "&"),
                                new Token(TokenType.OPEN_BRACKET, "("),
                                new Token(TokenType.VARIABLE, "B"),
                                new Token(TokenType.EQUAL_LESS, "<="),
                                new Token(TokenType.CONSTANT, "3"),
                                new Token(TokenType.AND, "&"),
                                new Token(TokenType.VARIABLE, "A"),
                                new Token(TokenType.EQUAL, "="),
                                new Token(TokenType.CONSTANT, "2"),
                                new Token(TokenType.CLOSE_BRACKET, ")")
                        )
                ).parse()
        );
    }


    @Test
    void name() {
        System.out.println(new Parser(new Lexer("5>A").listTokens()).parse());
    }
}
