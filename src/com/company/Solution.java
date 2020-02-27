package com.company;


public class Solution {
    private final int n;
    private final String format;

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

    public String getFormat() {
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
}
