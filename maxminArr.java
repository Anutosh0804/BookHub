package Arrays;
//2nd
public class maxminArr {
    public static void main(String[] args) {
        int max=Integer.MIN_VALUE;
        int min=Integer.MAX_VALUE;
        int arr[]={1,2,3,4,5,6};
        for(int i:arr){
            max=i>max?i:max;
            min=i<min?i:min;
        }
        System.out.println("the max is "+max+"and the min is "+min);
    }
    
}
