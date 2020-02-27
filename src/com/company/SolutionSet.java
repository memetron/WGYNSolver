package com.company;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;

public class SolutionSet {
    public Solution[] set = new Solution[1];
    private int numSolutions = 0;
    private int n;

    public SolutionSet(int n) {
        this.n = n;
    }

    public int value() {
        return this.n;
    }

    public int getNumSolutions() {
        return this.numSolutions;
    }

    public void append(Solution s) {
        if (Arrays.asList(set).contains(s)) return;
        numSolutions++;
        if (numSolutions >= set.length) {
            Solution[] clone = new Solution[set.length * 2];
            System.arraycopy(set, 0, clone, 0, set.length);
            set = clone;
        }
        set[numSolutions - 1] = s;
    }

    public void merge(SolutionSet s) {
        for (Solution sol : s.set) {
            if (sol != null) append(sol);
        }
    }

    public void printSolutions(int n) {
        if (numSolutions > 0) {
            for (int i : uniqueRandom(numSolutions,Math.min(numSolutions,n))) {
                System.out.println(set[i]);
            }
        } else {
            System.out.println("No Solutions Found!");
        }
    }


    private int[] uniqueRandom(int range, int n) {
        int[] rand = new int[n];
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < range; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        for (int i = 0; i < n; i++) {
            rand[i] = list.get(i);
        }
        return rand;
    }
}
