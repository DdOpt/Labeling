import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

class Label {
    Customer customer;//the label to mark a feasible path (depot,...,label.customer).
    double cost;//cost after the vehicle depart the label.customer along the label.
    double load;//load after the vehicle depart the label.customer along the label.
    double time;//arrival time when the vehicle arrive the label.customer from depot along the label.
    Label pre; //The previous label used to backtrace the path.
    boolean dominated; //a boolean var, when it is True,the label is dominated by others.
    //mark the customers this label can not extend to them.
    HashSet<Customer> unreachable_cs = new HashSet<Customer>();


    Label(){};
    Label(Customer customer,double cost,double load,double time){
        this.customer = customer;
        this.cost = cost;
        this.load = load;
        this.time = time;
        this.dominated = false;
        this.unreachable_cs.add(customer);
    }

    public boolean dominates(Label label){
        return (this.cost <= label.cost && this.load <= label.load && this.time <= label.time)&&
                label.unreachable_cs.containsAll(this.unreachable_cs);
    }

    public boolean is_dominated(){
        for(Label label:this.customer.labels){
            if(label.dominates(this)) return true;
        }
        return false;
    }
    void filter_dominated(){
        ArrayList<Label> labels = new ArrayList<Label>();
        for(Label label:this.customer.labels){
            if (this.dominates(label)) label.dominated = true;
            else labels.add(label);
        }
        this.customer.labels = labels;
    }

    public ArrayList<Integer> path(){
        Label label = this;
        ArrayList<Integer> route = new ArrayList<Integer>();
        while(label.pre.customer.index != 0){
            route.add(0,label.pre.customer.index);
            label = label.pre;
        }
        return route;
    }
}

public class SPPRC{
    int capacity;
    ArrayList<Customer> customers;
    double[][] costs;
    double[][] times;
    Customer depot;
    double[] duals;
    int numofcustomer;
    public SPPRC(){};

    public SPPRC(int capacity,ArrayList<Customer> customers,double[][] costs,double[][] times){
        this.capacity = capacity;
        this.customers = customers;
        this.costs = costs;
        this.times = times;
        this.depot = customers.get(0);
        this.numofcustomer = customers.size();
        this.duals = new double[this.numofcustomer];
        this.duals[0] = 0;
        for(int i = 1;i<this.duals.length;i++){
            this.duals[i] = 2 * costs[0][i];
        }
    }
    public Label depot_label(){
        Label label = new Label(this.depot,0,0,0);
        label.unreachable_cs.clear();
        return label;
    }

    public ArrayList<Label> feasible_labels_from(Label from_label){
        ArrayList<Label> to_labels = new ArrayList<Label>();
        //subtour like (i,j,i) is forbidden
        if(from_label.pre != null){
            if(from_label.pre.customer.index != 0){
                from_label.unreachable_cs.add(from_label.pre.customer);
            }
        }
        for(Customer to_cus :this.customers){
            if(from_label.unreachable_cs.contains(to_cus)) continue;
            if(to_cus.equals(from_label.customer)) continue;
            Label to_label = this.extened_label(from_label,to_cus);
            if(to_label.time==0){
                from_label.unreachable_cs.add(to_cus);
            }else{
                to_labels.add(to_label);
            }
        }
        return to_labels;
    }

    //to extend the lebel of from_label.customer
    public Label extened_label(Label from_label,Customer to_cus){
        //check the load of label
        double load = from_label.load + to_cus.demand;
        if (load > this.capacity) return new Label();
        //check the time of label
        Customer from_cus = from_label.customer;
        double time = Math.max(from_label.time + from_cus.serviceTime + this.times[from_cus.index][to_cus.index], to_cus.timeWindows[0]);
        if (time > to_cus.timeWindows[1]) return new Label();
        //calculate the revised cost
        double cost = (from_label.cost+this.costs[from_cus.index][to_cus.index] - this.duals[from_cus.index]);
        Label label = new Label(to_cus,cost,load,time);
        //marked the previous label
        label.pre = from_label;
        return label;
    }

    public ArrayList<Label> solve(){
        for(Customer cus : this.customers){
            cus.labels = new ArrayList<Label>();
        }
        LinkedList<Label> to_be_extended = new LinkedList<Label>();
        to_be_extended.add(this.depot_label());
        while(to_be_extended.size() > 0){
            Label from_label = to_be_extended.removeFirst();
            if(from_label.dominated) continue;
            ArrayList<Label> to_labels = this.feasible_labels_from(from_label);
            for(Label to_label:to_labels){
                Customer to_cus = to_label.customer;
                if (to_cus.index != 0){
                    to_label.unreachable_cs.addAll(from_label.unreachable_cs);
                    if(to_label.is_dominated()) continue;
                    to_label.filter_dominated();
                    to_be_extended.add(to_label);
                }
                to_cus.labels.add(to_label);
            }
        }
        //after solve,all feasible labels' customer is the depot
        return this.depot.labels;
    }
}
