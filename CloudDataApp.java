import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

//package cloudscapes;

public class CloudDataApp{
    static CloudData data;
    static int[] ind = new int[3];

    static SeqCompute sequential;
    static Vector ave;

    static int mode = 0;
    public static void main(String[] args){
        if(args == null){
            help();
            System.exit(0);
        }
        mode = args.length;

        if(mode == 1){
            benchmark(args[0]);
        }

       
    }

    public static void help(){
        String help_message = "Cloud Data App\n";
        help_message += "Arguments [1]: <benchmarking script file name>";
        help_message += "Arguments [2]: <input data file name> <output data file name>";
        System.out.println("");
    }

    public static void benchmark(String fileName){
       
        try{ 
			Scanner sc = new Scanner(new File(fileName), "UTF-8");
			
			int numinputf = sc.nextInt();
            int numseq_cutoffs = sc.nextInt();
            int seq_cutoff_index = sc.nextInt();
			String arch = sc.next();
			
			String[] inputf = new String[numinputf];
            int[] seq_cutoffs = new int[numseq_cutoffs];
            
				for(int i = 0; i < numinputf; i++){
                    inputf[i] = sc.next();
						
                }
                for(int i = 0; i < numseq_cutoffs; i++){
                    seq_cutoffs[i] = sc.nextInt();
                }
            sc.close();

            double[][] seq_data = new double[numinputf][2];
            double[][] parallel_data = new double[numinputf][2];


            
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}

    }

    public static void writeFile(double[][] arrdata, String fileName){
        try{ 
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            for(int i = 0; i < arrdata.length; i++){
                    printWriter.printf("%f, %f\n", arrdata[i][0], arrdata[i][1]);
            }
           
           printWriter.close();
           fileWriter.close(); 
        }
        catch (IOException e){
            System.out.println("Unable to open output file "+fileName);
               e.printStackTrace();
        }
    }

    public static double[] seq_exec(String input_file, String output_file){ //Returns average run time & dim
        data = new CloudData(input_file);
        sequential = new SeqCompute(data);
        ave = new Vector();

        System.out.println("Sequential Execution BEGIN");
        int runs = 8; //Number of runs to do
        int ignore_first = 3; //Number of runs to ignore due to cache warming
        double[][] times = new double[runs][2];
        double[] runtimes = new double[runs];
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
        //data.writeData(output_file, ave);
        System.out.println("Sequential Execution finnished Sucessfully");
        int sum = 0;
        for (int i = 0; i < runs; i++){
            runtimes[i] = (times[i][1]-times[i][0]);
            if(i >= ignore_first){
                sum += runtimes[i];
            }
            System.out.println("Time " + i + ": " + runtimes[i] +"");
        }
        double[] return_arr = new double[2];
        return_arr[0] = (double) data.dim();
        return_arr[1] = sum/(double)(runs-ignore_first);        
        return return_arr;
    }

    public static double[] par_exec(String input_file, String output_file){
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