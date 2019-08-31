public class SeqCompute{
    CloudData data;

    public SeqCompute(CloudData data){
        this.data = data;
    }

    public int[][] SurroundingElements(int pos){
        int[] ind;
        data.locate(pos, ind);
        int[][] surrounding = new int[9][2];
        int x = ind[1];
        int y = ind[2];

        surrounding[0][0] = x;
        surrounding[0][1] = y+1;

        surrounding[1][0] = x+1;
        surrounding[1][1] = y+1;

        surrounding[2][0] = x+1;
        surrounding[2][1] = y1;
        if(x > 1){
            surrounding[3][0] = x-1;
            surrounding[3][1] = y;

            surrounding[4][0] = x-1;
            surrounding[4][1] = y+1;
        }else{
            surrounding[5] = null;

            surrounding[6]= null;

        }
        if(y > 1){
            surrounding[7][0] = x;
            surrounding[7][1] = y-1;            

            surrounding[8][0] = x+1;
            surrounding[8][1] = y-1;            
        }else{
            surrounding[7] = null;

            surrounding[8]= null;

        }
            surrounding[9][0] = x;
            surrounding[9][1] = y;
        return surrounding;
    }

    public Vector LocalAverage(int pos){
        int count = 0;
        Vector sum = new Vector();
        int[][] surrounding = SurroundingElements(pos);

        for (int i = 0; i < 9; i++){
            if (surrounding[i] != null){
                count ++;
                sum.x += surrounding[i][0];
                sum.y += surrounding[i][1];
            }
        }

        return new Vector(sum.x/count, sum.y/count);

    }

}