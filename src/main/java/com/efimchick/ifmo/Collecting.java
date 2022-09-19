package com.efimchick.ifmo;


import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {

    public int sum(IntStream intStream) {
        return intStream.sum();
    }

    public int production(IntStream intStream) {
        Int anInt = new Int();
        intStream.forEach(anInt::multiply);
        return anInt.getTotal();
    }

    public int oddSum(IntStream intStream) {
        return intStream
                .filter(s -> s % 2 == 1 || s % 2 == -1)
                .sum();
    }

    public Map<Integer, Integer> sumByRemainder(int divider, IntStream intStream) {
        HashMap<Integer, Integer> map = new HashMap<>();
        intStream.forEach(s -> {
            int k = s % divider;
            Integer val = map.getOrDefault(k, 0);
            map.put(k, s + val);
        });
        return map;
    }

    public Map<Person, Double> totalScores(Stream<CourseResult> results) {

        HashMap<Person, Double> map = new HashMap<>();
        Int v = new Int(0);
        results.forEach(s -> {
            Integer val = s.getTaskResults().get("Lab 1. Figures");
            s.getTaskResults().values().forEach(v::add);
            map.put(s.getPerson(), (v.getTotal() / (double) (val == null ? 4 : 3)));
            v.multiply(0);
        });
        return map;
    }

    public double averageTotalScore(Stream<CourseResult> results) {

        Int v = new Int(0);
        results.forEach(s -> {
            boolean b = s.getTaskResults().containsKey("Lab 1. Figures");
            v.setHistorical(!b);
            s.getTaskResults().values().forEach(v::add);
        });

        return v.getTotal() / (double) (v.isHistorical() ? 12 : 9);

    }


    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> results) {
        HashMap<String, Int> examResults = new HashMap<>();
        results.forEach(s -> {
            s.getTaskResults()
                    .forEach((key, val) -> {
                        Int orDefault = examResults.getOrDefault(key, new Int(0));
                        orDefault.add(val);
                        examResults.put(key, orDefault);
                        if (key.startsWith("Lab"))
                            orDefault.setHistorical(!key.startsWith("Lab"));
                    });
        });
        HashMap<String, Double> result = new HashMap<>();
        examResults.forEach((key, val) -> {
            result.put(key, val.getTotal() / (double) 3);
        });

        return result;
    }


    public Map<Person, String> defineMarks(Stream<CourseResult> results) {

        Map<Person, Double> personDoubleMap = totalScores(results);
        Map<Person, String> marks = new HashMap<>();
        personDoubleMap.forEach((key, val) -> {
            String str = val < 60 ? "F" : val < 68 ? "E" : val < 75 ? "D" : val < 83 ? "C" : val <= 90 ? "B" : "A";
            marks.put(key, str);
        });
        return marks;
    }


    public String easiestTask(Stream<CourseResult> results) {
        Map<String, Double> stringDoubleMap = averageScoresPerTask(results);
        final StringBuilder result = new StringBuilder();
        final double[] maximum = {0};
        stringDoubleMap.forEach((key, val) -> {
            if (Math.max(maximum[0], val) > maximum[0]) {
                maximum[0] = val;
                result.delete(0, result.length());
                result.append(key);
            }
        });
        return result.toString();
    }


    public Collector<CourseResult, ?, String> printableStringCollector() {

        return new Coll();

    }

}

