package org.example.testingalgor;

public class BubbleSort implements SortingAlgorithm {

    @Override
    public void sort(int[] arr, Visualizer v) {

        String[] code = {
                "for i = 0 to n-1",
                "  for j = 0 to n-i-1",
                "    if arr[j] > arr[j+1]",
                "       swap(arr[j], arr[j+1])"
        };

        v.setCode(code);
        int n = arr.length;
        for(int i=0;i<n;i++){
            v.highlightLine(0);
            for(int j=0;j<n-i-1;j++){
                v.highlightLine(1);
                v.delay();

                v.compare(j,j+1);

                v.highlightLine(2);
                v.delay();
                if(arr[j] > arr[j+1]){
                    v.highlightLine(3);
                    v.swap(j,j+1);

                }
            }
        }

        v.highlightLine(-1);
    }
    @Override
    public String name() {
        return "Bubble Sort";
    }
}