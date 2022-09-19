package com.efimchick.ifmo;

import com.efimchick.ifmo.util.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Row implements Comparable<Row>{

    public static final int[] length4 = {15,21,17,6};
    public static final int[] length5 = {11,14,10,8,6} ;

    public String name;

    public double[] values;




    public Row(Person name, double ... values) {
        this.name = name.getLastName()+" "+ name.getFirstName();
        this.values = values;
    }


    public void toString(int max, StringBuilder str) {
        int n = max - name.length();
        str.append(name).append(" ".repeat(n)).append("|");
        if (values.length==3) write4(str);
        else write5(str);
    }


    public void write4(StringBuilder builder) {

        double sum = 0;
        String num;
        for (int i = 0; i < 3; i++) {
            num = Integer.toString((int) values[i]);
            builder.append(" ".repeat(length4[i]-num.length())).append(num).append(" |");
            sum+=values[i];
        }

        num = Double.toString(Math.round((sum*100)/3)/100.);
        num = num.length() < 5 ? num.concat("0") : num;
        builder.append(" ".repeat(length4[3]-num.length())).append(num).append(" |");
        writeMark(builder,sum/3);
    }


    public void write5(StringBuilder builder) {

        double sum = 0;
        String num;
        for (int i = 0; i < 4; i++) {
            num = Integer.toString((int) values[i]);
            builder.append(" ".repeat(length5[i]-num.length())).append(num).append(" |");
            sum+=values[i];
        }

        num = Double.toString(Math.round((sum*100)/4)/100.);
        num = num.length() < 5 ? num.concat("0") : num;
        builder.append(" ".repeat(length5[4]-num.length())).append(num).append(" |");
        writeMark(builder,sum/4);
    }

    public void writeMark(StringBuilder builder, double val){
        String str = val < 60 ? "F" : val < 68 ? "E" : val < 75 ? "D" : val < 83 ? "C" : val <= 90 ? "B" : "A";
        builder.append(" ".repeat(4)).append(str).append(" |");
    }


    @Override
    public int compareTo(Row o) {
        return this.name.compareTo(o.name);
    }
}
