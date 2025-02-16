package me.supcheg.evaluator.expression.analyze;

import lombok.Data;

@Data
public class Interval {
    public static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    public static final int POSITIVE_INFINITY = Integer.MAX_VALUE;

    private final int start;
    private final int end;

    public boolean isEmpty() {
        if (start > end) return true;
        return start == end - 1;
    }

    public boolean isFull() {
        return start == NEGATIVE_INFINITY && end == POSITIVE_INFINITY;
    }

    public Interval union(Interval other) {
        return new Interval(
                Math.min(this.start, other.start),
                Math.max(this.end, other.end)
        );
    }

    public Interval intersect(Interval other) {
        return new Interval(
                Math.max(this.start, other.start),
                Math.min(this.end, other.end)
        );
    }
}
