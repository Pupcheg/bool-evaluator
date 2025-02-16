package me.supcheg.evaluator.expression.analyze;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
public class Interval {
    public static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;
    public static final int POSITIVE_INFINITY = Integer.MAX_VALUE;

    public static final Interval FULL = new Interval(Interval.NEGATIVE_INFINITY, Interval.POSITIVE_INFINITY, true, true);
    public static final Interval EMPTY = new Interval(0, -1, false, false);

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

    public static Interval union(Interval a, Interval b) {
        int newLower = Math.min(a.lower, b.lower);
        int newUpper = Math.max(a.upper, b.upper);
        boolean includeLower = (a.lower == newLower && a.includeLower) ||
                               (b.lower == newLower && b.includeLower);
        boolean includeUpper = (a.upper == newUpper && a.includeUpper) ||
                               (b.upper == newUpper && b.includeUpper);
        return new Interval(newLower, newUpper, includeLower, includeUpper);
    }

    public static Interval intersect(Interval a, Interval b) {
        int newLower = Math.max(a.lower, b.lower);
        int newUpper = Math.min(a.upper, b.upper);
        boolean includeLower = (a.lower == newLower ? a.includeLower : b.includeLower);
        boolean includeUpper = (a.upper == newUpper ? a.includeUpper : b.includeUpper);

        return Interval.builder()
                .lower(newLower)
                .includeLower(includeLower)
                .upper(newUpper)
                .includeUpper(includeUpper)
                .build();
    }

    public List<Interval> subtract(Interval toRemove) {
        if (toRemove.contains(this)) {
            return List.of(EMPTY);
        }

        List<Interval> result = new ArrayList<>();

        if (this.lower < toRemove.lower) {
            result.add(new Interval(
                    this.lower,
                    toRemove.lower - 1,
                    this.includeLower,
                    true
            ));
        }

        if (this.upper > toRemove.upper) {
            result.add(new Interval(
                    toRemove.upper + 1,
                    this.upper,
                    true,
                    this.includeUpper
            ));
        }

        return Collections.unmodifiableList(result);
    }

    private boolean contains(Interval other) {
        return this.lower <= other.lower && this.upper >= other.upper;
    }
}
