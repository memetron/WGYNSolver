package com.company;

class Solution implements Comparable<Solution> {
    //An object that contains an integer value and a string representing the mathematical expression to get that value
    private int n;
    private String format;

    Solution(int n, String format) {
        this.n = n;
        this.format = format;
    }

    @Override
    public String toString() {
        return (format);
    }

    int value() {
        return n;
    }

    String getFormat() {
        return format;
    }

    void setFormat(String format) {
        this.format = format;
    }

    void setValue(int value) {
        this.n = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Solution)) {
            return false;
        }
        Solution s = (Solution) o;
        return s.format.equals(this.format);
    }

    @Override
    public int compareTo(Solution s) {
        if (equals(s)) return 0;
        return (this.value() > s.value()) ? 1 : -1;
    }
}
