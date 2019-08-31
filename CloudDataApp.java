//package cloudscapes;

public class CloudDataApp{
    public static void main(String[] args){
        CloudData data;
        int[] ind = new int[3];

        SeqCompute sequential;
        Vector ave;

        //SEQUENTIAL EXECUTION
        if(false){
            data = new CloudData("largesample_input.txt");
            sequential = new SeqCompute(data);
            ave = new Vector();

            System.out.println("Sequential Execution BEGIN");
            int runs = 3;
            double[][] times = new double[runs][2];
            for(int j = 0; j < runs; j++){
                System.out.println("Test: " + j);
                System.gc();
                times[j][0] = System.currentTimeMillis();
                for(int i = 0; i < data.dim(); i++){
                    data.locate(i, ind); //Linear position to 3D index conversion
                    data.classification[ind[0]][ind[1]][ind[2]] = sequential.Classify(i); //Classify current index
                    
                }
                ave = sequential.Average();
                times[j][1] = System.currentTimeMillis();
            }        
            data.writeData("sequential_output.txt", ave);
            System.out.println("Sequential Execution finnished Sucessfully");
            for (int i = 0; i < runs; i++){
                System.out.println("Time " + i + ": " + (times[i][1]-times[i][0])+"");
            }
        }

        //PARALLEL EXECUTION
        System.out.println("Parallel Execution BEGIN");
        data = new CloudData("largesample_input.txt");
        System.out.println("File Loaded");
        ave = new Vector();

        ParCompute parallel = new ParCompute(data, 0, data.dim());
        parallel.fork();       
        
        data.writeData("parallel_output.txt", parallel.join());
        System.out.println("Parallel Execution finnished Sucessfully");
    }
}