package me.supcheg.evaluator.expression.read.exception;

import me.supcheg.evaluator.message.ExpressionMessageContext;

public class NotEndException extends SyntaxException {
    public NotEndException() {
        super("Expected end, but has tokens left", -1, -1);
    }

    @Override
    public String makeMessage(ExpressionMessageContext ctx) {
        String expression = ctx.getExpression();
        return String.format(
                "%s: %s%s",
                getMessage(),
                expression.substring(0, expression.length() - 1),
                ctx.getConsoleFormatter().makeBold(expression.substring(expression.length() - 1))
        );
    }
}
