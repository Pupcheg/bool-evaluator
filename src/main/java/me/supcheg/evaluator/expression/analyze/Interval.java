package me.supcheg.evaluator.expression.analyze;

import lombok.Data;

@Data
public class Interval {
    public static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    public static final int POSITIVE_INFINITY = Integer.MAX_VALUE;

    private final int lower;
    private final int upper;

    public boolean isEmpty() {
        return lower > upper;
    }

    public boolean isFull() {
        return lower == NEGATIVE_INFINITY && upper == POSITIVE_INFINITY;
    }

    public Interval union(Interval other) {
        return new Interval(
                Math.min(this.lower, other.lower),
                Math.max(this.upper, other.upper)
        );
    }

    public Interval intersect(Interval other) {
        return new Interval(
                Math.max(this.lower, other.lower),
                Math.min(this.upper, other.upper)
        );
    }
}
