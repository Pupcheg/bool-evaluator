package me.supcheg.evaluator.expression.read.exception;

import me.supcheg.evaluator.message.ExpressionMessageContext;

public class UnexpectedEndException extends SyntaxException {
    public UnexpectedEndException() {
        super("Expected any other tokens, but reached the end", -1, -1);
    }

    @Override
    public String makeMessage(ExpressionMessageContext ctx) {
        return String.format(
                "%s: %s%s",
                getMessage(),
                ctx.getExpression(),
                ctx.getConsoleFormatter().makeBold(" ")
        );
    }
}
