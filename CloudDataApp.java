//package cloudscapes;

public class CloudDataApp{
    public static void main(String[] args){
        CloudData data;
        int[] ind = new int[3];

        SeqCompute sequential;
        Vector ave = new Vector();

        //Benchmarking
        int runs = 3;
        double[][] seq_times = new double[runs][2];
        double[][] par_times = new double[runs][2];

        //SEQUENTIAL EXECUTION
        System.out.println("File Loading");
        data = new CloudData("largesample_input.txt");
        System.out.println("File Loaded");

        System.out.println("Sequential Execution BEGIN");

        for(int j = 0; j < runs; j++){
            System.out.println("Test: " + j);
            System.gc();
            seq_times[j][0] = System.currentTimeMillis();

            sequential = new SeqCompute(data);
            ave = new Vector();

            for(int i = 0; i < data.dim(); i++){
                data.locate(i, ind); //Linear position to 3D index conversion
                data.classification[ind[0]][ind[1]][ind[2]] = sequential.Classify(i); //Classify current index
                
            }
            ave = sequential.Average();
            seq_times[j][1] = System.currentTimeMillis();
        }        
        data.writeData("sequential_output.txt", ave);
        System.out.println("Sequential Execution finnished Sucessfully");
        for (int i = 0; i < runs; i++){
            System.out.println("Time " + i + ": " + (seq_times[i][1]-seq_times[i][0])+"");
        }


        //PARALLEL EXECUTION
        System.out.println("File Loading");
        data = new CloudData("largesample_input.txt");
        System.out.println("File Loaded");

        System.out.println("Parallel Execution BEGIN");


        for(int j = 0; j < runs; j++){
            System.out.println("Test: " + j);
            System.gc();
            par_times[j][0] = System.currentTimeMillis();
            ave = new Vector();

            ParCompute parallel = new ParCompute(data, 0, data.dim());
            parallel.fork();
            ave.include(parallel.join());

            par_times[j][1] = System.currentTimeMillis();
        }       
            
        data.writeData("parallel_output.txt", ave);
        System.out.println("Parallel Execution finnished Sucessfully");
        
        for (int i = 0; i < runs; i++){
            System.out.println("Time " + i + ": " + (par_times[i][1]-par_times[i][0])+"");
        }
    }
}