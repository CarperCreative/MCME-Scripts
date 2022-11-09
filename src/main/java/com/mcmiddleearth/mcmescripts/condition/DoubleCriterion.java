package com.mcmiddleearth.mcmescripts.condition;

import java.util.function.Function;

public class DoubleCriterion implements Function<Double,Boolean> {

    String comparator;
    Double limit;

    public DoubleCriterion(String comparator, Double limit) {
        this.comparator = comparator;
        this.limit = limit;
    }

    @Override
    public Boolean apply(Double integer) {
        return switch (comparator) {
            case "<" -> integer < limit;
            case ">" -> integer > limit;
            case "<=" -> integer <= limit;
            case ">=" -> integer >= limit;
            case "==", "=" -> integer.equals(limit);
            case "<>", "!=" -> !integer.equals(limit);
            default -> true;
        };
    }

    public String getComparator() {
        return comparator;
    }

    public Double getLimit() {
        return limit;
    }
}
