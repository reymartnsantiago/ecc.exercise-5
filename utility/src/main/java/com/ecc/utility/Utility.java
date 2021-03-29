package com.ecc.utility;

/**
 * Hello world!
 *
 */

import java.util.Scanner;

public final class Utility {
    private Utility() {

    }

    public static String getStringValue(String text) {
        Scanner sc = new Scanner(System.in);

        System.out.printf(text);
        String input = sc.nextLine();

        while (input.isEmpty()) {

            System.out.printf("Variable cannot be empty.\nPlease entry any character: ");
            input = sc.nextLine();
        }
        return input;

    }

    public static String getStringValue() {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (input.isEmpty()) {

            System.out.printf("Variable cannot be empty.\nPlease enter any character: ");
            input = sc.nextLine();
        }
        return input;
    }

    public static int getIntegerInput(String text) {
        Scanner sc = new Scanner(System.in);

        System.out.printf(text);

        while (true) {
            try {

                int input = Integer.parseInt(sc.nextLine());

                return input >= 0 ? input : -1;

            } catch (NumberFormatException e) {
                System.out.printf("Input should only be a positive integer.\nPlease input another value: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean isAscending() {
        Scanner sc = new Scanner(System.in);
        int input = 0;

        do {
            try {
                System.out.printf("Press [1] - Ascending [2] - Descending");
                input = Integer.parseInt(sc.nextLine());

                return input == 1 ? true : false;
            } catch (NumberFormatException e) {
                System.out.printf("Input should only be a positive integer.\nPlease input another value: ");

            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        } while (input != 1 || input != 2);

        return false;
    }

    public static int getIntegerInput() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {

                int input = Integer.parseInt(sc.nextLine());

                return input >= 0 ? input : -1;

            } catch (NumberFormatException e) {
                System.out.printf("Input should only be a positive integer.\nPlease input another value: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

    }
}
