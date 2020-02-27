package com.company;

public class WGYNSet extends WGYNSubset {

    WGYNSet(int[] set) {

        super(set, true);
    }


    public static void main(String[] args) {
        int[] set = {1, 2, 3, 4};
        WGYNSet s = new WGYNSet(set);
        s.printPossible();
        s.printSolutions(99);
    }
}