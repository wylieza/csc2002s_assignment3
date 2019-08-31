//package cloudscapes;

public class CloudDataApp{
    public static void main(String[] args){
        CloudData data = new CloudData("largesample_input.txt");
        int[] ind = new int[3];

        Vector vec;
        SeqCompute sequential = new SeqCompute(data);
        for(int i = 0; i < data.dim(); i++){
            data.locate(i, ind);
            data.classification[ind[0]][ind[1]][ind[2]] = sequential.Classify(i);
            
            //data.locate(i,ind);
            //System.out.println(ind[0] + " " + ind[1] + " " + ind[2]);
            vec = sequential.LocalAverage(i);
            //System.out.println("x: " + vec.x + " y: " + vec.y + " Classify -> " + sequential.Classify(i));
        }
        data.writeData("output.txt", sequential.Average());
        System.out.println(sequential.Average());       
    }
}