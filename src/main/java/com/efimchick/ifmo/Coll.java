package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class Coll implements Collector<CourseResult, Object, String> {

    private static final String[] tasks5 = {"Phalanxing", "Shieldwalling", "Tercioing", "Wedging"};

    private static final String[] tasks4 = {"Lab 1. Figures", "Lab 2. War and Peace", "Lab 3. File Tree"};
    public StringBuilder builder = new StringBuilder();


    private List<Double> listOfAvg = new ArrayList<>();

    private int maxLengthPerson;
    private int count;

    public TreeSet<Row> listRows = new TreeSet<>();

    public Map<String, Integer> mapOfTasks = new HashMap<>();
    private int countOfTask = 0;
    private final Supplier<Object> supplier;
    private final BinaryOperator<Object> combiner;
    private final Function<Object, String> finisher;
    private final Set<Characteristics> characteristics = CH_ID;

    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    static final Set<Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));

    Coll(Supplier<Object> supplier,
         BinaryOperator<Object> combiner,
         Function<Object, String> finisher) {
        this.supplier = supplier;
        this.combiner = combiner;
        this.finisher = finisher;
    }

    Coll() {
        this(() -> "builder.toString()", (o, o2) -> o, castingIdentity());
    }

    public BiConsumer<Object, CourseResult> accumulator() {
        return this::accept;
    }

    @Override
    public Supplier<Object> supplier() {
        return () -> builder;
    }

    @Override
    public BinaryOperator<Object> combiner() {
        return combiner;
    }

    @Override
    public Function<Object, String> finisher() {
        return ((str)->done());
    }


    @Override
    public Set<Characteristics> characteristics() {
        return characteristics;
    }


    //TODO
    private void accept(Object o, CourseResult courseResult) {

        Map<String, Integer> taskResults = courseResult.getTaskResults();
        Person person = courseResult.getPerson();

        maxLengthPerson = Math.max(person.getLastName().length()
                + person.getFirstName().length() + 2, maxLengthPerson);

        taskResults.forEach((key, val) -> {
            Integer orDefault = mapOfTasks.getOrDefault(key, 0);
            mapOfTasks.put(key, val + orDefault);
        });

        if (countOfTask == 0) {
            if (taskResults.get("Lab 1. Figures") == null) countOfTask = 4;
            else countOfTask = 3;
        }

        if (countOfTask == 3)
            listRows.add(new Row(person, tacks4(taskResults, 0), tacks4(taskResults, 1), tacks4(taskResults, 2)));
        else
            listRows.add(new Row(person, tacks5(taskResults, 0), tacks5(taskResults, 1), tacks5(taskResults, 2), tacks5(taskResults, 3)));


    }

    private Integer tacks4(Map<String, Integer> taskResults, int index) {
        return taskResults.getOrDefault(tasks4[index], 0);
    }

    private Integer tacks5(Map<String, Integer> taskResults, int index) {
        return taskResults.getOrDefault(tasks5[index], 0);
    }


    private void firstLine() {
        if (countOfTask == 3)
            builder.append("Student").append(" ".repeat(maxLengthPerson-8)).append(" |")
                    .append(" Lab 1. Figures | Lab 2. War and Peace | Lab 3. File Tree | Total | Mark |\n");
        else
            builder.append("Student").append(" ".repeat(maxLengthPerson-8)).append(" |")
                    .append(" Phalanxing | Shieldwalling | Tercioing | Wedging | Total | Mark |\n");

    }


    public String done() {

            firstLine();
        for (Row listRow : listRows) {
            listRow.toString(maxLengthPerson, builder);
            builder.append("\n");
        }


        builder.append("Average")
                .append(" ".repeat(maxLengthPerson - 8))
                .append(" |");


        if (countOfTask == 3)
            write4();
        else write5();

        return builder.toString();
    }


    public void write4() {

        String num;
        for (int i = 0; i < 3; i++) {

            num = Double.toString(Math.round(((mapOfTasks.get(tasks4[i]) / 3.) * 100)) / 100.);
            listOfAvg.add(Math.round(((mapOfTasks.get(tasks4[i]) / 3.) * 100)) / 100.);
            num = num.length() < 5 ? num.concat("0") : num;
            builder.append(" ".repeat(Row.length4[i] - num.length()))
                    .append(num).append(" |");
        }

        double avg = 0;
        for (Double aDouble : listOfAvg) {
            avg += aDouble;
        }

        num = Double.toString(Math.round((avg/3  * 100) ) / 100.);
        builder.append(" ".repeat(Row.length4[3] - num.length())).append(num).append(" |");
        writeMark(builder, avg / 3);

    }


    public void write5() {

        String num;
        for (int i = 0; i < 4; i++) {

            num = Double.toString(Math.round(((mapOfTasks.get(tasks5[i]) / 3.) * 100) ) / 100.);
            listOfAvg.add(Math.round(((mapOfTasks.get(tasks5[i]) / 3.) * 100)) / 100.);
            num = num.length() < 5 ? num.concat("0") : num;
            builder.append(" ".repeat(Row.length5[i] - num.length()))
                    .append(num).append(" |");
        }

        double avg = 0;
        for (Double aDouble : listOfAvg) {
            avg += aDouble;
        }

        num = Double.toString(Math.round((avg * 100) / 4) / 100.);
        num = num.length() < 5 ? num.concat("0") : num;
        builder.append(" ".repeat(Row.length5[4] - num.length())).append(num).append(" |");
        writeMark(builder, avg / 4);

    }


    public void writeMark(StringBuilder builder, double val) {
        String str = val < 60 ? "F" : val < 68 ? "E" : val < 75 ? "D" : val < 83 ? "C" : val <= 90 ? "B" : "A";
        builder.append(" ".repeat(4)).append(str).append(" |");
    }


}
