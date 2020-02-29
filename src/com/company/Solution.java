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
        if (a.value() > 5000 || a.value() < -5000 || b.value() > 500 || b.value() < -500) return null;

        return new Solution(a.value() * b.value(), "(" + a.format + "*" + b.format + ")");
    }

    static Solution exponentiation(Solution a, Solution b) {
        if (b.value() > 0 && b.value() < 10 && a.value() < 4)
        return new Solution((int) Math.pow(a.value(), b.value()), "(" + a.format + "^" + b.format + ")");
        if (b.value() > 0 && b.value() < 6 && a.value() < 6)
            return new Solution((int) Math.pow(a.value(), b.value()), "(" + a.format + "^" + b.format + ")");
        if (b.value() > 0 && b.value() < 4 && a.value() < 20)
            return new Solution((int) Math.pow(a.value(), b.value()), "(" + a.format + "^" + b.format + ")");
        if (b.value() > 0 && b.value() < 3 && a.value() < 1000)
            return new Solution((int) Math.pow(a.value(), b.value()), "(" + a.format + "^" + b.format + ")");
        return null;

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

    static Solution factorial(Solution a) {
        if ((a.value() != 0 && a.value() < 3) || a.value() > 10) return null;
        if (a.value() == 0) return new Solution(1, a.format + "!");
        int n = 1;
        for (int i = 2; i <= a.value(); i++) {
            n *= i;
        }
        return new Solution(n, a.format + "!");
    }
}
