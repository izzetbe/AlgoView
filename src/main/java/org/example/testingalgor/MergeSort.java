package org.example.testingalgor;

public class MergeSort implements SortingAlgorithm {

    @Override
    public void sort(int[] arr, Visualizer v) {

        String[] code = {
                "mergeSort(arr, left, right)",
                "  if left < right",
                "    mid = (left+right)/2",
                "    mergeSort(left, mid)",
                "    mergeSort(mid+1, right)",
                "    merge(left, mid, right)"
        };

        v.setCode(code);

        mergeSort(arr,0,arr.length-1,v);

        v.highlightLine(-1);
    }

    private void mergeSort(int[] arr, int left, int right, Visualizer v) {

        v.highlightLine(0); // function call
        v.delay();
        v.highlightLine(1); // if condition
        v.delay();

        if (left < right) {
            int mid = (left + right) / 2;
            v.highlightLine(2); // calculate mid
            v.delay();
            v.highlightLine(3); // left recursion
            v.delay();
            mergeSort(arr, left, mid, v);
            v.highlightLine(4); // right recursion
            v.delay();
            mergeSort(arr, mid + 1, right, v);
            v.highlightLine(5); // merge step
            v.delay();
            merge(arr, left, mid, right, v);
        }
    }

    private void merge(int[] arr,int left,int mid,int right,Visualizer v){

        int n1 = mid-left+1;
        int n2 = right-mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for(int i=0;i<n1;i++)
            L[i] = arr[left+i];

        for(int j=0;j<n2;j++)
            R[j] = arr[mid+1+j];

        int i=0,j=0,k=left;

        while(i<n1 && j<n2){
            v.compare(left+i,mid+1+j);

            if(L[i] <= R[j]){
                v.overwrite(k,L[i]);
                i++;

            }else{
                v.overwrite(k,R[j]);
                j++;
            }
            k++;
        }

        while(i<n1){
            v.overwrite(k,L[i]);
            i++;
            k++;
        }

        while(j<n2){
            v.overwrite(k,R[j]);
            j++;
            k++;
        }
    }

    @Override
    public String name() {
        return "Merge Sort";
    }
}