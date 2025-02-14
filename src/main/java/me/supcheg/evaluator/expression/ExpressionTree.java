package me.supcheg.evaulator.expression;

import lombok.Data;
import me.supcheg.evaulator.expression.node.Node;

@Data
public class ExpressionTree {
    private final Node root;
}
