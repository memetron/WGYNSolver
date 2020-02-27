package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s;
        int n;
        int[] numberSet = new int[4];
        while (true) {
            boolean goodRotation = true;
            System.out.println("Input number set: ");
            s = in.nextLine().replaceAll(" ", "");
            if (!(isInt(s) && Integer.parseInt(s) >= 0 && Integer.parseInt(s) < 10000))
                System.out.println("Please Enter A valid Number Set.");
            else break;
        }
        int[] set = new int[]{Integer.parseInt(s) % 10,
                (Integer.parseInt(s) % 100 - Integer.parseInt(s) % 10) / 10,
                (Integer.parseInt(s) % 1000 - Integer.parseInt(s) % 100) / 100,
                (Integer.parseInt(s) - Integer.parseInt(s) % 1000) / 1000};
        WGYNSet w = new WGYNSet(set);
        w.printPossible();


        while (true) {
            System.out.println("Input a number to see solutions!");
            s = in.nextLine();
            if (!(isInt(s) && Integer.parseInt(s) > 0 && Integer.parseInt(s) <= 100))
                System.out.println("Please Enter A valid Number (From 1 To 100).");
            else {
                n = Integer.parseInt(s);
                w.printSolutions(n);
            }
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
