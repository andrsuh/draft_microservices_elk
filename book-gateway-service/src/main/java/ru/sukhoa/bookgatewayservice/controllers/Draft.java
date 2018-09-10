package ru.sukhoa.bookgatewayservice.controllers;

import java.util.Arrays;

public class Draft {
    private static void partitioning(int[] a, int pivot_index) {
        int i = 0;
        int j = 0;
        int pivot = a[pivot_index];

        a[pivot_index] = a[0];
        a[0] = pivot;

        while (j < a.length - 1) {
            j++;
            if (a[j] <= pivot ) {
                int temp = a[i + 1];
                a[i + 1] = a[j];
                a[j] = temp;
                i++;
            }
        }
        a[0] = a[i];
        a[i] = pivot;
    }

    public static void main(String[] args) {
        int[] a = {1, 6, 2, 8, 4, 4, 5, 9, 8, 9, 4, 2, 4};
        // int[] a = {1, 2, 4, 4, 5, 4, 2, 4, 8, 9, 8, 6, 9};
        partitioning(a,  6);
        System.out.println(Arrays.toString(a));
    }
}
