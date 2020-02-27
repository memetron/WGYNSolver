package com.company;

import java.util.Arrays;

public class Solution {
    private final int n;
    private final String format;

    public Solution(int n, String format) {
        this.n = n;
        this.format = format;
    }

    @Override
    public String toString() {
        return (format);
    }

    public void print() {
        System.out.println(n + " - " + format);
    }

    public int value() {
        return n;
    }
    public String getFormat() {return format;}

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Solution)) {
            return false;
        }
        Solution s = (Solution) o;

        if (s.format.equals(this.format)) {
            return true;
        }
        return false;
    }
}
