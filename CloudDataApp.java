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

        if(mode == 2){
            seq_exec(args[0], args[1]+"_par.txt");
            par_exec(args[0], 0, args[1]+"_seq.txt");            
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
            int inputf_index = sc.nextInt();
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

            double[][] seq_par_data = new double[numinputf][3];
            double[][] cutoffs_data = new double[numseq_cutoffs][2];

            int i;
            System.out.println("\nSequential Testing Phase:\n");
            for(i = 0; i < numinputf; i++){
                double[] result = seq_exec(inputf[i], null);
                seq_par_data[i][0] = result[0];
                seq_par_data[i][1] = result[1];
            }

            System.out.println("\nParallel Testing Phase:\n");
            for(i = 0; i < numinputf; i++){
                double[] result = par_exec(inputf[i], seq_cutoffs[seq_cutoff_index], null);
                seq_par_data[i][2] = result[1];
            }
            writeFile(seq_par_data, "seq_par_" + arch + ".txt");

            System.out.println("\nSeq Cutoff Testing Phase:\n");
            for(i = 0; i < numseq_cutoffs; i++){
                double[] result = par_exec(inputf[inputf_index], seq_cutoffs[i], null);
                cutoffs_data[i][0] = seq_cutoffs[i];
                cutoffs_data[i][1] = result[1];
            }
            writeFile(cutoffs_data, "cutoffs_" + arch + ".txt");


            
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
                if(arrdata[i].length > 2){
                    printWriter.printf("%f; %f\n", arrdata[i][0], arrdata[i][1], arrdata[i][2]);
                }else{
                    printWriter.printf("%f; %f\n", arrdata[i][0], arrdata[i][1]);
                }
            }
           
           printWriter.close();
           fileWriter.close(); 
        }
        catch (IOException e){
            System.out.println("Unable to open output file "+fileName);
               e.printStackTrace();
        }
    }

    public static double[] seq_exec(String input_file, String fileName){ //Returns average run time & dim
        System.out.println("File Loading");
        data = new CloudData(input_file);
        System.out.println("File Loaded");

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
            if(fileName != null){
                break;
            }
        }
        if(fileName != null){
            data.writeData(fileName, ave);
            
        }
        System.out.println("Sequential Execution COMPLETE");
        int sum = 0;
        for (int i = 0; i < runs; i++){
            runtimes[i] = (times[i][1]-times[i][0]);
            if(i >= ignore_first){
                sum += runtimes[i];
            }
            System.out.println("Time " + i + ": " + runtimes[i] +"");
            if(fileName != null){
                break;
            }
        }
        System.out.println("------------------------------------------------------------------------");
        double[] return_arr = new double[2];
        return_arr[0] = (double) data.dim();
        return_arr[1] = sum/(double)(runs-ignore_first);        
        return return_arr;
    }

    public static double[] par_exec(String input_file, int seq_cutoff, String fileName){ //Returns average run time & dim
        System.out.println("File Loading");
        data = new CloudData(input_file);
        System.out.println("File Loaded");

        System.out.println("Parallel Execution BEGIN");
        int runs = 8; //Number of runs to do
        int ignore_first = 3; //Number of runs to ignore due to cache warming
        double[][] times = new double[runs][2];
        double[] runtimes = new double[runs];
        if(seq_cutoff > 0){
            ParCompute.SEQ_CUTOFF = seq_cutoff;
        }

        for(int j = 0; j < runs; j++){
            System.out.println("Test: " + j);
            System.gc();
            times[j][0] = System.currentTimeMillis();
            ave = new Vector();

            ParCompute parallel = new ParCompute(data, 0, data.dim());
            parallel.fork();
            ave.include(parallel.join());
            
            times[j][1] = System.currentTimeMillis();
            if(fileName != null){
                break;
            }

        }       
            
        if(fileName != null){
            data.writeData(fileName, ave);
        }
        System.out.println("Parallel Execution COMPLETE");
        
        int sum = 0;
        for (int i = 0; i < runs; i++){
            runtimes[i] = (times[i][1]-times[i][0]);
            if(i >= ignore_first){
                sum += runtimes[i];
            }
            System.out.println("Time " + i + ": " + runtimes[i] +"");
            if(fileName != null){
                break;
            }

        }
        System.out.println("------------------------------------------------------------------------");
        double[] return_arr = new double[2];
        return_arr[0] = (double) data.dim();
        return_arr[1] = sum/(double)(runs-ignore_first);        
        return return_arr;

       
    }
}