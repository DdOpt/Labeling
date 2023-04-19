import java.io.IOException;
import java.util.ArrayList;
public class main {
    public static void main(String[] args)throws IOException {
        double st = System.nanoTime();

        Input input = new Input("data/c101.txt",100);
        double[][] distance = input.getDis();
        int capacity = input.capacity;
        ArrayList<Customer> customers = input.getCustomer();
        SPPRC spprc = new SPPRC(capacity,customers,distance,distance);
        ArrayList<Label> labels = spprc.solve();
        System.out.println(labels.size());

        System.out.println((System.nanoTime()-st)/1e9);
        Label label = labels.get(1960);
        ArrayList<Integer> route = label.path();
        System.out.println();
        for(int c:route) System.out.print(c+" ");

   }
}





