package com.company;

public class WGYNSet extends WGYNSubset {

    WGYNSet(int[] set) {
        super(set, true);
        generate(true);
    }
}