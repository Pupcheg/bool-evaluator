package me.supcheg.evaluator.expression.analyze;

import lombok.Data;

@Data
public class Interval {
    public static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    public static final int POSITIVE_INFINITY = Integer.MAX_VALUE;

    private final int lower;
    private final int upper;
    private final boolean includeLower;
    private final boolean includeUpper;

    public boolean isEmpty() {
        if (lower > upper) return true;
        return lower == upper && (!includeLower || !includeUpper);
    }

    public boolean isFull() {
        return lower == NEGATIVE_INFINITY && upper == POSITIVE_INFINITY;
    }

    public Interval union(Interval other) {
        int newLower = Math.min(this.lower, other.lower);
        int newUpper = Math.max(this.upper, other.upper);
        boolean includeLower = (this.lower == newLower && this.includeLower) ||
                               (other.lower == newLower && other.includeLower);
        boolean includeUpper = (this.upper == newUpper && this.includeUpper) ||
                               (other.upper == newUpper && other.includeUpper);
        return new Interval(newLower, newUpper, includeLower, includeUpper);
    }

    public Interval intersect(Interval other) {
        int newLower = Math.max(this.lower, other.lower);
        int newUpper = Math.min(this.upper, other.upper);
        boolean includeLower = (this.lower == newLower ? this.includeLower : other.includeLower);
        boolean includeUpper = (this.upper == newUpper ? this.includeUpper : other.includeUpper);

        return new Interval(newLower, newUpper, includeLower, includeUpper);
    }
}
