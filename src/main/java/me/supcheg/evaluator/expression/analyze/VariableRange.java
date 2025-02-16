package me.supcheg.evaluator.expression.analyze;

import me.supcheg.evaluator.expression.Operation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class VariableRange {
    private List<Interval> intervals = new ArrayList<>();

    public void addIntervals(Iterable<Interval> intervals, Operation operation) {
        for (Interval interval : intervals) {
            addInterval(interval, operation);
        }
    }

    public void addInterval(Interval interval, Operation operation) {
        switch (operation) {
            case OR:
                intervals.add(interval);
                intervals = mergeIntervals();
                break;
            case AND:
                intervals = intervals.stream()
                        .map(existing -> Interval.intersect(existing, interval))
                        .filter(not(Interval::isEmpty))
                        .collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }

    private List<Interval> mergeIntervals() {
        List<Interval> merged = new ArrayList<>();
        for (Interval interval : (Iterable<Interval>) intervals.stream().sorted(Comparator.comparingInt(Interval::getLower))::iterator) {

            if (merged.isEmpty()) {
                merged.add(interval);
                continue;
            }

            Interval last = merged.get(merged.size() - 1);
            if (last.getUpper() >= interval.getLower() - 1) {
                merged.set(merged.size() - 1, Interval.union(last, interval));
            } else {
                merged.add(interval);
            }
        }
        return merged;
    }

    public boolean isAlwaysTrue() {
        return intervals.stream().anyMatch(Interval::isFull);
    }

    public boolean isAlwaysFalse() {
        return intervals.isEmpty();
    }
}
