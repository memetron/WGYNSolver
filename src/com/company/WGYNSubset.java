package com.company;

import java.util.Arrays;
import java.util.stream.Stream;

class WGYNSubset {
    private Solution[] set;
    private WGYNSubset[] subSets = new WGYNSubset[1];
    private int numSubsets = 0;
    private SolutionSet[] solutions;

    private WGYNSubset(Solution[] set, boolean canConcat) {
        this.solutions = generateInitialSolutionSetArray();
        this.set = set;
        Arrays.sort(set);
        generateSubsets(canConcat);
        solve();
    }

    WGYNSubset(int[] set, boolean canConcat) {
        this.set = new Solution[set.length];
        this.solutions = generateInitialSolutionSetArray();
        Stream.iterate(0, i -> i + 1).limit(set.length).forEach(
                i -> this.set[i] = new Solution(set[i], Integer.toString(set[i]))
        );
        Arrays.sort(set);
        generateSubsets(canConcat);
        solve();
    }

    private WGYNSubset(int[] set, String[] format, boolean canConcat) {
        this.solutions = generateInitialSolutionSetArray();
        this.set = new Solution[set.length];
        Stream.iterate(0, i -> i + 1).limit(set.length).forEach(
                i -> this.set[i] = new Solution(set[i], format[i])
        );
        Arrays.sort(set);
        generateSubsets(canConcat);
        solve();
    }

    private SolutionSet[] generateInitialSolutionSetArray() {
        SolutionSet[] solutions = new SolutionSet[100];
        Stream.iterate(0, i -> i + 1).limit(100).forEach(
                i -> solutions[i] = new SolutionSet(i + 1)
        );
        return solutions;
    }

    private void generateSubsets(boolean canConcat) {
        if (canConcat)
            generateConcatenatedSubsets();

        if (set.length > 1) {
            generateBasicOperationSubsets();
            generateFactorialSubsets();
        }

    }

    private void solve() {
        if (set.length == 1) {
            if (set[0].value() > 0 && set[0].value() <= 100) {
                this.solutions[set[0].value() - 1].append(new Solution(this.set[0].value(), this.set[0].getFormat()));
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
                        other[otherCount] = set[count].value();
                        format[otherCount + 1] = set[count].getFormat();
                        otherCount++;
                    }
                }

                Solution[] operationResults = basicOperations(set[operands[0]].value(), set[operands[1]].value(),
                        set[operands[0]].getFormat(), set[operands[1]].getFormat());

                for (Solution sol : operationResults) {
                    if (sol != null) {
                        int[] s = new int[1 + other.length];
                        s[0] = sol.value();
                        System.arraycopy(other, 0, s, 1, other.length);
                        format[0] = sol.toString();
                        if (Arrays.stream(subSets)
                                .noneMatch(s::equals)) {
                            appendSet(new WGYNSubset(s, format, false));
                            if (s.length == 1 && s[0] == 3) {
                                int[] endFactorial = {3};
                                String[] endFactorialFormat = {format[0] + "!"};
                                appendSet(new WGYNSubset(endFactorial, endFactorialFormat, false));
                            }
                            if (s.length == 1 && s[0] == 4) {
                                int[] endFactorial = {24};
                                String[] endFactorialFormat = {format[0] + "!"};
                                appendSet(new WGYNSubset(endFactorial, endFactorialFormat, false));
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateFactorialSubsets() {
        for (int i = 0; i < set.length; i++) {
            if (((set[i].value() > 2 && set[i].value() < 10) || (set[i].value() == 0)) &&
                    (set[i].getFormat().length() < 2 || !this.set[i].getFormat().substring(set[i].getFormat().length() - 1).equals("!"))) {
                Solution[] newSubset = Arrays.copyOf(set, set.length);
                newSubset[i].setValue(factorial(newSubset[i].value()));

                newSubset[i].setFormat(newSubset[i].getFormat() + "!");
                appendSet(new WGYNSubset(newSubset, false));
            }
        }
    }

    private void generateConcatenatedSubsets() {
        appendSet(this);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int k = j < i ? j : j + 1;
                int l = !(i == 0 || k == 0) ? 0 : !(i == 1 || k == 1) ? 1 : 2;
                int m = !(i == 1 || k == 1 || l == 1) ? 1 : !(i == 2 || k == 2 || l == 2) ? 2 : 3;
                int concatinatedNum = Integer.parseInt(this.set[i].toString() + this.set[k].toString());
                int firstNum = (this.set[l].value());
                int secondNum = (this.set[m].value());
                appendSet(new WGYNSubset(new int[]{concatinatedNum, firstNum, secondNum}, false)); //one concat

                //two concat
                appendSet(new WGYNSubset(new int[]{concatinatedNum, Integer.parseInt(Integer.toString(firstNum) + secondNum)}, false));
                appendSet(new WGYNSubset(new int[]{concatinatedNum, Integer.parseInt(Integer.toString(secondNum) + firstNum)}, false));

                //triple concat
                appendSet(new WGYNSubset(new int[]{firstNum, Integer.parseInt(concatinatedNum + Integer.toString(secondNum))}, false));
                appendSet(new WGYNSubset(new int[]{secondNum, Integer.parseInt(Integer.toString(concatinatedNum) + firstNum)}, false));
            }
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

    private void appendSet(WGYNSubset s) {
        if (Arrays.asList(subSets).contains(s)) return;
        numSubsets++;
        if (numSubsets >= subSets.length) {
            WGYNSubset[] clone = new WGYNSubset[subSets.length * 2];
            System.arraycopy(subSets, 0, clone, 0, subSets.length);
            subSets = clone;
        }
        subSets[numSubsets - 1] = s;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (!(o instanceof WGYNSubset)) {
//            return false;
//        }
//        WGYNSubset s = (WGYNSubset) o;
//        return ((new HashSet<>(Arrays.stream(set).boxed().collect(Collectors.toList()))).equals(
//                new HashSet<>(Arrays.stream(s.set).boxed().collect(Collectors.toList()))));
//    }
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof WGYNSubset)) {
            return false;
        }
        WGYNSubset s = (WGYNSubset) o;

        return (Arrays.equals(set, s.set));
    }

    public boolean equals(Solution[] arr) {
        return Arrays.equals(set, arr);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Solution sol : set) {
            s.append(sol).append(", ");
        }
        return s.substring(0, s.length() - 2);
    }

    void printPossible() {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            if (solutions[i] != null) {
                System.out.println(i + 1 + " - " + solutions[i].getNumSolutions());
                if (solutions[i].getNumSolutions() > 0) count++;
            }
        }
        System.out.println("Found: " + count + "/100");
    }

    void printSolutions(int n) {
        solutions[n - 1].printSolutions(20);
    }

    public static void main(String[] args) {
        int[] set = {};
        WGYNSubset s = new WGYNSubset(set, true);
        s.printPossible();
        s.printSolutions(4);
    }
}