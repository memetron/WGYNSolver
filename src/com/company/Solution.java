package com.company;

class Solution implements Comparable<Solution> {
    //An object that contains an integer value and a string representing the mathematical expression to get that value
    private int n;
    private String format;

    Solution(int n, String format) {
        this.n = n;
        this.format = format;
    }

    Solution(Solution s) {
        this.n = s.value();
        this.format = s.getFormat();
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

    static Solution add(Solution a, Solution b) {
        return new Solution(a.value() + b.value(), "(" + a.format + "+" + b.format + ")");
    }

    static Solution subtract(Solution a, Solution b) {
        return new Solution(a.value() - b.value(), "(" + a.format + "-" + b.format + ")");
    }

    static Solution multiply(Solution a, Solution b) {
        return new Solution(a.value() * b.value(), "(" + a.format + "*" + b.format + ")");
    }

    static Solution exponentiation(Solution a, Solution b) {
        return (b.value() > 0 && b.value() < 10) ?
                new Solution((int) Math.pow(a.value(), b.value()), "(" + a.format + "^" + b.format + ")") :
                null;
    }

    static Solution division(Solution a, Solution b) {
        if (b.value() == 0) return null;
        return (a.value() % b.value() == 0) ?
                new Solution(a.value() / b.value(), "(" + a.format + "/" + b.format + ")") :
                null;
    }

    static Solution log(Solution a, Solution b) {
        if (a.value() < 2 || b.value() < 1) return null;
        double l = (Math.log(b.value()) / Math.log(a.value()));
        return (Math.floor(l) == l) ? new Solution((int) l, "(Log_" + a.format + " " + b.format + ")") : null;
    }
}
