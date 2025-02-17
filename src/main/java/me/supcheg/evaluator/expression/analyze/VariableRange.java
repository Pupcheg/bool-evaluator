package me.supcheg.evaluator.expression.analyze;

import me.supcheg.evaluator.expression.operation.BooleanOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class VariableRange {
    private List<Interval> intervals = new LinkedList<>();

    public void addIntervals(Iterable<Interval> intervals, BooleanOperation operation) {
        for (Interval interval : intervals) {
            addInterval(interval, operation);
        }
    }

    public void addInterval(Interval interval, BooleanOperation operation) {
        switch (operation) {
            case OR:
                unionWith(interval);
                break;
            case AND:
                intersectWith(interval);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    private void intersectWith(Interval interval) {
        intervals = intervals.stream()
                .map(existing -> existing.intersect(interval))
                .filter(not(Interval::isEmpty))
                .collect(toList());
    }

    /**
     * Unions all contacting intervals like
     * <p>{@code [0, 1) and [2, 3)}<p/>
     * <p>{@code [0, 10) and [5, 40)}<p/>
     */
    private void unionWith(Interval interval) {
        List<Interval> compact = new LinkedList<>();

        Iterable<Interval> sortedByLower =
                Stream.concat(intervals.stream(), Stream.of(interval))
                        .sorted(comparingInt(Interval::getLower))
                        ::iterator;

        for (Interval cursor : sortedByLower) {

            if (compact.isEmpty()) {
                compact.add(cursor);
                continue;
            }

            Interval last = compact.get(compact.size() - 1);
            if (last.touchesRight(cursor)) {
                compact.set(compact.size() - 1, last.union(cursor));
                continue;
            }

            compact.add(cursor);
        }

        intervals = compact;
    }

    public boolean isAlwaysTrue() {
        return intervals.stream()
                .anyMatch(Interval::isFull);
    }

    public boolean isAlwaysFalse() {
        return intervals.isEmpty();
    }
}
