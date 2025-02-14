package me.supcheg.evaluator.expression;

import lombok.Data;
import me.supcheg.evaluator.expression.node.Node;

@Data
public class ExpressionTree {
    private final Node root;
}
