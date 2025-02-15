package me.supcheg.evaluator.expression.read.exception;

import lombok.RequiredArgsConstructor;
import me.supcheg.evaluator.message.ExpressionMessageContext;
import me.supcheg.evaluator.message.ExpressionMessageProvider;

@RequiredArgsConstructor
public class SyntaxException extends Exception implements ExpressionMessageProvider {
    private final int start;
    private final int end;

    public SyntaxException(String message, int start, int end) {
        super(message);
        this.start = start;
        this.end = end;
    }

    @Override
    public String makeMessage(ExpressionMessageContext ctx) {
        String expression = ctx.getExpression();

        return String.format(
                "%s: %s%s%s",
                getMessage(),
                expression.substring(0, start),
                ctx.getConsoleFormatter().makeBold(expression.substring(start, end)),
                expression.substring(end)
        );
    }
}
