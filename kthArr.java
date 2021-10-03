package Arrays;

import java.util.Collections;
import java.util.PriorityQueue;
//3rd
public class kthArr {
    public static void main(String[] args) {
      int arr[]={1,2,3,4,5,6,7};
      int k=2;
      getKth(arr,k);
    }

    private static void getKth(int[] arr,int k) {
        PriorityQueue<Integer> minheap
            = new PriorityQueue<Integer>();
            PriorityQueue<Integer> maxheap
            = new PriorityQueue<Integer>(
                Collections.reverseOrder());
        for(int i:arr){
            minheap.add(i);
            maxheap.add(i);
        }
        for(int i=1;i<k;i++){
            minheap.poll();
            maxheap.poll();
        }
        System.out.println("the kth max element is "+maxheap.peek()+" and the minimum is "+minheap.peek());
    }
    
}
