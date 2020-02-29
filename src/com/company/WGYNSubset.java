package com.company;

import java.util.Arrays;
import java.util.stream.Stream;

class WGYNSubset {
    private Solution[] set;
    private WGYNSubset[] subSets = new WGYNSubset[1];
    private int numSubsets = 0;
    private SolutionSet[] solutions;
    private boolean canConcat;

    private WGYNSubset(Solution[] set, boolean canConcat) {
        this.solutions = generateInitialSolutionSetArray();
        this.set = cloneSolutionArray(set);
        this.canConcat = canConcat;
        Arrays.sort(this.set);
    }

    WGYNSubset(int[] set, boolean canConcat) {
        this.set = new Solution[set.length];
        this.solutions = generateInitialSolutionSetArray();
        this.canConcat = canConcat;
        Stream.iterate(0, i -> i + 1).limit(set.length).forEach(
                i -> this.set[i] = new Solution(set[i], Integer.toString(set[i]))
        );
        Arrays.sort(this.set);
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

    void generate(boolean canConcat) {
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
        if (canConcat) {
            generateConcatenatedSubsets();
        }
        if (set.length > 1) {
            generateBasicOperationSubsets();
            generateFactorialSubsets();
        }
        if (set.length == 1 && (set[0].value() == 3 || set[0].value() == 4)) {
            Solution[] finalStepFactorialSet = new Solution[]{Solution.factorial(set[0])};
            appendSet(new WGYNSubset(finalStepFactorialSet, false));
        }
        if (set.length >= 3) {
            generateFractionalExponentSubsets();
        }
        if (set.length == 4) {
            generateMultistepDenominatorSubsets();
        }
    }
    private void generateMultistepDenominatorSubsets() {
        int[][] permutations = new int[][] {
                {0,1,2,3},{0,1,3,2},{0,2,1,3},{0,2,3,1},{0,3,1,2},{0,3,2,1},
                {1,0,2,3},{1,0,3,2},{1,2,0,3},{1,2,3,0},{1,3,0,2},{1,3,2,0},
                {2,1,0,3},{2,1,3,0},{2,0,1,3},{2,0,3,1},{2,3,1,0},{2,3,0,1},
                {3,1,2,0},{3,1,0,2},{3,2,1,0},{3,2,3,0},{3,0,1,2},{3,0,2,1}
        };
        for (int[] permutation : permutations) {
            Solution a = set[permutation[0]];
            Solution b = set[permutation[1]];
            Solution c = set[permutation[2]];
            Solution d = set[permutation[3]];
            Solution multstepDenoninatorPlus = Solution.multistepDenominatorDivision(a,b,c,d,true);
            if (multstepDenoninatorPlus != null) {
                Solution[] newSubset = new Solution[] {multstepDenoninatorPlus};
                appendSet(new WGYNSubset(newSubset,false));
            }
            Solution multstepDenoninatorMinus = Solution.multistepDenominatorDivision(a,b,c,d,false);
            if (multstepDenoninatorMinus != null) {
                Solution[] newSubset = new Solution[] {multstepDenoninatorMinus};
                appendSet(new WGYNSubset(newSubset,false));
            }
        }
    }

    private void generateFractionalExponentSubsets() {
        int[][] permutations = new int[][]{{0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0}};
        if (set.length == 4) {
            for (int i = 0; i < set.length; i++) {
                for (int[] permutation : permutations) {
                    int a = permutation[0] >= i ? permutation[0] + 1 : permutation[0];
                    int b = permutation[1] >= i ? permutation[1] + 1 : permutation[1];
                    int c = permutation[2] >= i ? permutation[2] + 1 : permutation[2];
                    Solution fractionalExponent = Solution.fractionalExponent(set[a], set[b], set[c]);
                    if (fractionalExponent != null) {
                        Solution[] newSet = new Solution[]{fractionalExponent, new Solution(set[i])};
                        appendSet(new WGYNSubset(newSet, false));
                    }
                }
            }
        } else {
            for (int[] permutation : permutations) {
                int a = permutation[0];
                int b = permutation[1];
                int c = permutation[2];
                Solution fractionalExponent = Solution.fractionalExponent(set[a], set[b], set[c]);
                if (fractionalExponent != null) {
                    Solution[] newSet = new Solution[]{fractionalExponent};
                    appendSet(new WGYNSubset(newSet, false));
                }
            }
        }
    }

    private void generateBasicOperationSubsets() {
        Solution[] operands = new Solution[2]; //Will store solutions used as operands
        Solution[] unchangedSolutions = new Solution[set.length - 1]; //Will store other elements
        for (int i = 0; i < set.length; i++) {
            for (int j = 0; j < set.length - 1; j++) {
                int k = j < i ? j : j + 1;

                operands[0] = set[i];
                operands[1] = set[k]; //assigns all pairs of two elements from set to operands

                int unchangedSolutionCount = 0; //assigns other elements from set to unchangedElements
                for (int count = 0; count < set.length; count++) {
                    if (!(count == i || count == k)) {
                        unchangedSolutions[unchangedSolutionCount] = new Solution(set[count]);
                        unchangedSolutionCount++;
                    }
                }

                Solution[] operations = basicOperations(operands[0], operands[1]);

                for (Solution s : operations) {
                    unchangedSolutions[unchangedSolutions.length - 1] = s;
                    WGYNSubset newSet = new WGYNSubset(unchangedSolutions, false);
                    appendSet(newSet);
                }

            }
        }
    }

    private void generateFactorialSubsets() {
        for (int i = 0; i < set.length; i++) {
            if (((set[i].value() > 2 && set[i].value() < 10) || (set[i].value() == 0)) &&
                    (set[i].getFormat().length() < 2 || !this.set[i].getFormat().substring(set[i].getFormat().length() - 1).equals("!"))) {
                Solution[] newSubset = cloneSolutionArray(set);
                newSubset[i] = Solution.factorial(newSubset[i]);
                if (newSubset[i] != null) appendSet(new WGYNSubset(newSubset, false));
            }
            if (set[i].value() == 3) {
                Solution[] newSubset = cloneSolutionArray(set);
                newSubset[i] = Solution.factorial(Solution.factorial(newSubset[i]));
                appendSet(new WGYNSubset(newSubset, false));
            }
        }
    }

    private void generateConcatenatedSubsets() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int k = j < i ? j : j + 1;
                int l = !(i == 0 || k == 0) ? 0 : !(i == 1 || k == 1) ? 1 : 2;
                int m = !(i == 1 || k == 1 || l == 1) ? 1 : !(i == 2 || k == 2 || l == 2) ? 2 : 3;
                int concatenatedNum = Integer.parseInt(this.set[i].toString() + this.set[k].toString());
                int firstNum = (this.set[l].value());
                int secondNum = (this.set[m].value());
                appendSet(new WGYNSubset(new int[]{concatenatedNum, firstNum, secondNum}, false)); //one concat

                //two concat
                appendSet(new WGYNSubset(new int[]{concatenatedNum, Integer.parseInt(Integer.toString(firstNum) + secondNum)}, false));
                appendSet(new WGYNSubset(new int[]{concatenatedNum, Integer.parseInt(Integer.toString(secondNum) + firstNum)}, false));

                //triple concat
                appendSet(new WGYNSubset(new int[]{firstNum, Integer.parseInt(concatenatedNum + Integer.toString(secondNum))}, false));
                appendSet(new WGYNSubset(new int[]{secondNum, Integer.parseInt(Integer.toString(concatenatedNum) + firstNum)}, false));
            }
        }
    }

    private Solution[] basicOperations(Solution a, Solution b) {
        Solution[] operationList = new Solution[]{
                Solution.add(a, b),
                Solution.subtract(a, b),
                Solution.multiply(a, b),
                Solution.division(a, b),
                Solution.exponentiation(a, b),
                Solution.log(a, b)
        };
        //Creates a new array of solutions where null values are stripped.
        int count = 0;
        for (Solution s : operationList) {
            if (s != null) {
                count++;
            }
        }
        Solution[] operations = new Solution[count];
        count = 0;
        for (Solution s : operationList) {
            if (s != null) {
                operations[count] = s;
                count++;
            }
        }
        return operations;
    }

    private static Solution[] cloneSolutionArray(Solution[] arr) {
        Solution[] clone = new Solution[arr.length];
        Stream.iterate(0, n -> n + 1).limit(arr.length).forEach(n ->
                clone[n] = (arr[n] == null) ? null : new Solution(arr[n]));
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof WGYNSubset)) {
            return false;
        }
        WGYNSubset s = (WGYNSubset) o;
        if (s.set.length != set.length) return false;

        for (int i = 0; i < set.length; i++) {
            if (s.set[i].value() != set[i].value()) {
                return false;
            }
        }
        return true;
    }

    private boolean containsSubset(WGYNSubset s) {
        for (WGYNSubset ss : subSets) {
            if (ss != null && ss.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void appendSet(WGYNSubset s) {
        if (containsSubset(s)) return;
        s.generate(s.canConcat);
        numSubsets++;
        if (numSubsets >= subSets.length) {
            WGYNSubset[] clone = new WGYNSubset[subSets.length * 2];
            System.arraycopy(subSets, 0, clone, 0, subSets.length);
            subSets = clone;
        }
        subSets[numSubsets - 1] = s;
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

    //    private void printSubsets() { //Useful for debug purposes
//        for (WGYNSubset ss : subSets) if (ss != null) System.out.println(ss);
//    }
    public static void main(String[] args) {
        int[] set = new int[]{1, 2, 3, 4};
        WGYNSubset ss = new WGYNSubset(set, false);
        ss.generateFractionalExponentSubsets();
    }

}