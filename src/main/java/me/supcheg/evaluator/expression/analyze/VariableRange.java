package me.supcheg.evaluator.expression.analyze;

import me.supcheg.evaluator.expression.Operation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class VariableRange {
    private List<Interval> intervals = new LinkedList<>();

    public void addIntervals(Iterable<Interval> intervals, Operation operation) {
        for (Interval interval : intervals) {
            addInterval(interval, operation);
        }
    }

    public void addInterval(Interval interval, Operation operation) {
        switch (operation) {
            case OR:
                intervals.add(interval);
                compact();
                break;
            case AND:
                intersect(interval);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    private void intersect(Interval interval) {
        intervals = intervals.stream()
                .map(existing -> existing.intersect(interval))
                .filter(not(Interval::isEmpty))
                .collect(Collectors.toList());
    }

    private void compact() {
        List<Interval> compact = new LinkedList<>();

        Iterable<Interval> sortedByLower = intervals.stream().sorted(Comparator.comparingInt(Interval::getStart))::iterator;

        for (Interval cursor : sortedByLower) {

            if (compact.isEmpty()) {
                compact.add(cursor);
                continue;
            }

            Interval last = compact.get(compact.size() - 1);
            if (last.getEnd() >= cursor.getStart() - 1) {
                compact.set(compact.size() - 1, last.union(cursor));
            } else {
                compact.add(cursor);
            }
        }

        intervals = compact;
    }

    public boolean isAlwaysTrue() {
        return intervals.stream().anyMatch(Interval::isFull);
    }

    public boolean isAlwaysFalse() {
        return intervals.isEmpty();
    }
}
