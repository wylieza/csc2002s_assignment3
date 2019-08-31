import java.util.concurrent.RecursiveTask;

public class ParCompute extends RecursiveTask<Vector>{
    
    private static final long serialVersionUID = 1L; //Auto generated

    public static final int SEQ_CUTOFF = 5000;
    CloudData data;
    int upper;
    int lower;
    Vector average = new Vector();


    public ParCompute(CloudData data, int lower, int upper){
        this.data = data;
        this.upper = upper;
        this.lower = lower;
    }

    public Vector compute(){
        int mid = upper - lower;
        if(upper - lower > SEQ_CUTOFF){
            System.out.println("need to split");
            data.thread_counter ++;
            ParCompute left = new ParCompute(data, lower, lower + mid);
            left.fork();
            lower = mid;
            System.out.println("Split occured");
            average = (this.compute());
            average.include(left.join());

            return average;            
        }else{
            int[] ind = new int[3];
            Vector sum = new Vector();
            SeqCompute sequential = new SeqCompute(data);
            for(int i = lower; i < upper; i++){
                data.locate(i, ind); //Linear position to 3D index conversion
                data.classification[ind[0]][ind[1]][ind[2]] = sequential.Classify(i); //Classify current index   
                
                sum.x += data.advection[ind[0]][ind[1]][ind[2]].x;
                sum.y += data.advection[ind[0]][ind[1]][ind[2]].y;
            }
            average.x = sum.x/(float)mid;
            average.y = sum.y/(float)mid;
            return average;
        }

    }

}