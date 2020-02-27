package com.company;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class WGYNSubset {
    private int[] set;
    private WGYNSubset[] subSets = new WGYNSubset[1];
    private int numSubsets = 0;
    private SolutionSet[] solutions = new SolutionSet[100];
    private String[] operations;

    public WGYNSubset(@NotNull int[] set, String[] operations) {
        for (int i = 0; i < 100; i++) {
            solutions[i] = new SolutionSet(i + 1);
        }
        this.operations = operations;
        this.set = set;
        if (set.length > 1) {
            generateBasicOperationSubsets();
        }
        solve();
    }

    public WGYNSubset(int[] set, boolean canConcat, boolean dummySet) {
        this.set = set;
    }

    public WGYNSubset(@NotNull int[] set, boolean canConcat) {
        for (int i = 0; i < 100; i++) {
            solutions[i] = new SolutionSet(i + 1);
        }
        String[] format = new String[set.length];
        for (int i = 0; i < set.length; i++) {
            format[i] = Integer.toString(set[i]);
        }

        this.operations = format;
        this.set = set;
        if (canConcat)
            generateConcatenatedSubsets();
        if (set.length > 1) {
            generateBasicOperationSubsets();
        }
        solve();
    }

    void solve() {
        if (set.length == 1) {
            if (set[0] > 0 && set[0] <= 100) {
                this.solutions[set[0] - 1].append(new Solution(this.set[0], this.operations[0]));
            }
        } else {
            for (int i = 0; i < numSubsets; i++) {
                WGYNSubset s = subSets[i];
                for (SolutionSet sol : s.solutions) {
                    if (sol.getNumSolutions() != 0 && sol.value() > 0 && sol.value() <= 100) {
                        solutions[sol.value() - 1].merge(sol);
                    }
                }
            }
        }
    }


    private void generateBasicOperationSubsets() {
        int[] operands = new int[2];
        int[] other = new int[set.length - 2];
        String[] format = new String[1 + other.length];
        for (int i = 0; i < set.length; i++) {
            for (int j = 0; j < set.length - 1; j++) {
                int k = j < i ? j : j + 1;
                operands[0] = i;
                operands[1] = k;

                int otherCount = 0;
                for (int count = 0; count < set.length; count++) {
                    if (!(count == i || count == k)) {
                        other[otherCount] = set[count];
                        format[otherCount + 1] = operations[count];
                        otherCount++;
                    }
                }

                Solution[] operationResults = basicOperations(set[operands[0]], set[operands[1]],
                        operations[operands[0]], operations[operands[1]]);

                for (Solution sol : operationResults) {
                    if (sol != null) {
                        int[] s = new int[1 + other.length];
                        s[0] = sol.value();
                        System.arraycopy(other, 0, s, 1, other.length);
                        format[0] = sol.toString();
                        if (!Arrays.stream(subSets)
                                .anyMatch(new WGYNSubset(s, false, true)::equals)) {
                            appendSet(new WGYNSubset(s, format));
                            if (s.length==1 && s[0] == 3) {
                                int[] endFactoral = {3};
                                String[] endFactorialFormat = {format[0]+"!"};
                                appendSet(new WGYNSubset(endFactoral,endFactorialFormat));
                            }
                            if (s.length==1 && s[0] == 4) {
                                int[] endFactoral = {24};
                                String[] endFactorialFormat = {format[0]+"!"};
                                appendSet(new WGYNSubset(endFactoral,endFactorialFormat));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < set.length; i++) {
            String log = operations[i] + set[i];
            if (((set[i] > 2 && set[i] < 10) || (set[i] == 0)) &&
                    (operations[i].length() < 2 || !this.operations[i].substring(operations[i].length() - 1).equals("!"))) {
                int[] newSubset = Arrays.copyOf(set, set.length);
                newSubset[i] = factorial(set[i]);
                String[] newFormat = Arrays.copyOf(operations, operations.length);
                newFormat[i] = operations[i] + "!";
                appendSet(new WGYNSubset(newSubset, newFormat));
            }
        }
    }

    private void generateConcatenatedSubsets() {
        int[][] concatinatedSets = new int[61][];

        concatinatedSets[0] = this.set;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int k = j < i ? j : j + 1;
                int l = !(i == 0 || k == 0) ? 0 : !(i == 1 || k == 1) ? 1 : 2;
                int m = !(i == 1 || k == 1 || l == 1) ? 1 : !(i == 2 || k == 2 || l == 2) ? 2 : 3;
                int concatinatedNum = Integer.parseInt(Integer.toString(this.set[i]) + this.set[k]);
                int firstNum = (this.set[l]);
                int secondNum = (this.set[m]);
                concatinatedSets[1 + i * 3 + j] = new int[]{concatinatedNum, firstNum, secondNum}; //one concat

                //two concat
                concatinatedSets[13 + 6 * i + 2 * j] = new int[]{concatinatedNum, Integer.parseInt(Integer.toString(firstNum) + secondNum)};
                concatinatedSets[14 + 6 * i + 2 * j] = new int[]{concatinatedNum, Integer.parseInt(Integer.toString(secondNum) + firstNum)};

                //triple concat
                concatinatedSets[37 + 6 * i + 2 * j] = new int[]{firstNum, Integer.parseInt(concatinatedNum + Integer.toString(secondNum))};
                concatinatedSets[38 + 6 * i + 2 * j] = new int[]{secondNum, Integer.parseInt(Integer.toString(concatinatedNum) + firstNum)};
            }
        }
        for (int[] arr : concatinatedSets) {
            appendSet(new WGYNSubset(arr, false));
        }
    }

    private int factorial(int n) {
        if (n == 0) return 1;
        int val = 1;
        for (int i = 1; i <= n; i++)
            val = val * i;
        return val;
    }

    private static int intlog(int b, int a) {
        if (a < 1 || b < 2) return -2147483648;
        double l = (Math.log(a) / Math.log(b));
        return l == Math.floor(l) ? (int) l : -2147483648;
    }

    private Solution[] basicOperations(int a, int b, String formatA, String formatB) {
        return new Solution[]{
                new Solution(a + b, "(" + formatA + "+" + formatB + ")"), //Addition
                new Solution(a * b, "(" + formatA + "*" + formatB + ")"), //multiplication
                new Solution(a - b, "(" + formatA + "-" + formatB + ")"), //subtraction
                (b > 10) ? null : new Solution((int) Math.pow(a, b), "(" + formatA + "^" + formatB + ")"), //exponentiation
                ((!(a == 0 || b == 0) && a % b == 0) ? new Solution(a / b, "(" + formatA + "/" + formatB + ")") : null), //division (checks if integer) min int value if not
                intlog(a, b) == Integer.MIN_VALUE ? null : new Solution(intlog(a, b), "(log_" + formatA + "(" + formatB + "))")};
    }

    public void appendSet(WGYNSubset s) {
        if (Arrays.stream(subSets).anyMatch(s::equals)) return;
        numSubsets++;
        if (numSubsets >= subSets.length) {
            WGYNSubset[] clone = new WGYNSubset[subSets.length * 2];
            System.arraycopy(subSets, 0, clone, 0, subSets.length);
            subSets = clone;
        }
        subSets[numSubsets - 1] = s;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof WGYNSubset)) {
            return false;
        }
        WGYNSubset s = (WGYNSubset) o;
        if ((new HashSet<Integer>(Arrays.stream(set).boxed().collect(Collectors.toList()))).equals(
                new HashSet<Integer>(Arrays.stream(s.set).boxed().collect(Collectors.toList())))) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i : set) {
            s.append(i).append(", ");
        }
        return s.substring(0, s.length() - 2);
    }

    public void printPossible() {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            if (solutions[i] != null) {
                System.out.println(i + 1 + " - " + solutions[i].getNumSolutions());
                if(solutions[i].getNumSolutions()>0) count++;
            }
        }
        System.out.println("Found: "+count+"/100");
    }

    public void printSolutions(int n) {
        solutions[n - 1].printSolutions(20);
    }

    public void printSubsets() {
        for (int i = 0; i < numSubsets; i++) {
            System.out.println(subSets[i].toString());
        }
    }

    public static void main(String[] args) {
        int[] set = {};
        WGYNSubset s = new WGYNSubset(set, true);
        s.printPossible();
        s.printSolutions(4);
    }
}