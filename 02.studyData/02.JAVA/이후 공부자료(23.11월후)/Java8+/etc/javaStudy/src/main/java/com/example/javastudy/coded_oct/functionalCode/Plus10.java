package com.example.javastudy.coded_oct.functionalCode;

import java.util.function.Function;

public class Plus10 implements Function<Integer,Integer> {
    @Override
    public Integer apply(Integer integer) {
        return integer+10;
    }
}
