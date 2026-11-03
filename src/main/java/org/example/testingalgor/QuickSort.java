package org.example.testingalgor;

public class QuickSort implements SortingAlgorithm {

    @Override
    public String name() {
        return "Quick Sort";
    }

    @Override
    public void sort(int[] arr, Visualizer v) {
        v.setCode(new String[]{
                "quickSort(arr, low, high)",
                "if (low < high)",
                "   pivotIndex = partition(arr, low, high)",
                "   quickSort(left)",
                "   quickSort(right)"
        });

        quickSort(arr, 0, arr.length - 1, v);
    }

    private void quickSort(int[] arr, int low, int high, Visualizer v) {

        v.highlightLine(0); // function call
        v.delay();

        v.highlightLine(1); // if condition
        v.delay();

        if (low < high) {

            v.highlightLine(2); // partition
            v.delay();
            int pivotIndex = partition(arr, low, high, v);

            v.highlightLine(3); // left recursion
            v.delay();
            quickSort(arr, low, pivotIndex - 1, v);

            v.highlightLine(4); // right recursion
            v.delay();
            quickSort(arr, pivotIndex + 1, high, v);
        }
    }

    private int partition(int[] arr, int low, int high, Visualizer v) {

        int pivot = arr[high];

        int i = low - 1;

        for (int j = low; j < high; j++) {

            v.highlightLine(2); // inside partition loop
            v.delay();

            v.compare(j, high); // compare with pivot

            if (arr[j] < pivot) {
                i++;

                v.highlightLine(2);
                v.delay();

                v.swap(i, j);
            }
        }

        v.highlightLine(2);
        v.delay();

        v.swap(i + 1, high); // place pivot

        return i + 1;
    }
}