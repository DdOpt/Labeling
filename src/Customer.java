import java.util.ArrayList;
class Customer {
    int index;
    int[] timeWindows;
    int demand;
    int serviceTime;
    ArrayList<Label> labels;
    Customer(int index,int[] timewindows,int demand,int servicetime){
        this.index = index;
        this.timeWindows = timewindows;
        this.demand = demand;
        this.serviceTime = servicetime;
    }
}
