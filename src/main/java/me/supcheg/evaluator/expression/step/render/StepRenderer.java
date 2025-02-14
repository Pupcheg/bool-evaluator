package me.supcheg.evaluator.expression.step.render;

import lombok.SneakyThrows;
import me.supcheg.evaluator.expression.step.Operand;
import me.supcheg.evaluator.expression.step.SimpleOperand;
import me.supcheg.evaluator.expression.step.Step;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class StepRenderer {

    @SneakyThrows
    public String renderToString(Step step) {
        StringWriter sw = new StringWriter();
        render(step, sw);
        return sw.toString();
    }

    public void render(Step step, Writer out) throws IOException {
        out.write(String.valueOf(step.getNumber()));
        out.write(") ");

        render(step.getLeft(), out);

        out.write(' ');
        out.write(step.getOperation().getStringRepresentation());
        out.write(' ');

        render(step.getRight(), out);
    }


    private void render(Operand operand, Writer out) throws IOException {
        if (operand instanceof Step) {
            Step step = (Step) operand;

            out.write('[');
            out.write(String.valueOf(step.getNumber()));
            out.write(']');

        } else if (operand instanceof SimpleOperand) {
            SimpleOperand simpleOperand = (SimpleOperand) operand;

            out.write(String.valueOf((simpleOperand.getValue())));

        } else {
            throw new IllegalArgumentException("Unsupported operand: " + operand);
        }
    }
}
