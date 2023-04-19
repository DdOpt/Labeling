import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Input {
    int vehicle;
    int nums;
    int capacity;
    double[][] distance;
    int[] posx, posy, demand, serivece;
    int[] ready_time,due_time;
    ArrayList<Customer> customers = new ArrayList<Customer>();
    public Input(String filename,int nums) throws IOException {
        // filename which file would be read in
        // nums the number of customers
        this.nums = nums;
        System.out.println(this.nums);
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = new String();
            //get the vehicle number and capacity
            for (int i = 0; i < 5; i++) {
                line = br.readLine();
            }
            String[] tokens = line.split("\\s+");
            vehicle = Integer.parseInt(tokens[1]);
            capacity = Integer.parseInt(tokens[2]);

            // useless lines
            for (int i = 0; i <4 ; i++) {
                line = br.readLine();
            }
            //star to read customers(include depot) information
            posx = new int[nums + 1];
            posy = new int[nums + 1];
            demand = new int[nums + 1];
            serivece = new int[nums + 1];
            ready_time = new int[nums + 1];
            due_time = new int[nums + 1];
            for (int i = 0; i < nums+1; i++) {
                line = br.readLine();
                tokens = line.split("\\s+");
                posx[i] = Integer.parseInt(tokens[2]);
                posy[i] = Integer.parseInt(tokens[3]);
                int index = Integer.parseInt(tokens[1]);
                int[] tw = {Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6])};
                int demand = Integer.parseInt(tokens[4]);
                int st = Integer.parseInt(tokens[7]);
                Customer customer = new Customer(index, tw, demand, st);
                customers.add(customer);
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    public double[][] getDis(){
        System.out.println("num of customers: " + this.nums);
        distance = new double[this.nums+1][this.nums+1];
        for(int i = 0;i < this.nums + 1;i ++){
            for(int j = 0; j < this.nums+1;j++){
                distance[i][j] = Math.sqrt(
                        (posx[i]-posx[j])*(posx[i]-posx[j])+
                                (posy[i]-posy[j])*(posy[i]-posy[j])
                );
                //System.out.println(distance[i][j]);
            }
        }
        //System.out.println("num of customers: " + this.nums);
        return distance;
    }

    public ArrayList<Customer> getCustomer(){
        return customers;
    }


}
