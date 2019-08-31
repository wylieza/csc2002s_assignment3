public class SeqCompute{
    CloudData data;

    public SeqCompute(CloudData data){
        this.data = data;
    }

    public int[][] SurroundingElements(int pos){
        int[] ind = {0,0,0};
        data.locate(pos, ind);
        int[][] surrounding = new int[8][2];
        int x = ind[1];
        int y = ind[2];
        //int t = ind[0];

        surrounding[0][0] = x;
        surrounding[0][1] = y+1;

        surrounding[1][0] = x;
        surrounding[1][1] = y-1;   

        surrounding[2][0] = x+1;
        surrounding[2][1] = y+1;

        surrounding[3][0] = x+1;
        surrounding[3][1] = y;

        surrounding[4][0] = x+1;
        surrounding[4][1] = y-1;  

        surrounding[5][0] = x-1;
        surrounding[5][1] = y+1;

        surrounding[6][0] = x-1;
        surrounding[6][1] = y;

        surrounding[7][0] = x-1;
        surrounding[7][1] = y-1;
        
        for (int i = 0; i < 8; i++){
            if (surrounding[i][0] < 0 || surrounding[i][0] >= data.dimx){
                surrounding[i][0] = -1;
                surrounding[i][1] = -1;
            }
            if (surrounding[i][1] < 0 || surrounding[i][1] >= data.dimy){
                surrounding[i][0] = -1;
                surrounding[i][1] = -1;
            }
        }     

        return surrounding;
    }

    public Vector LocalAverage(int pos){
        int count = 1;
        Vector sum = new Vector();
        int[][] surrounding = SurroundingElements(pos);
        int[] base_ind = {0,0,0};
        data.locate(pos, base_ind);
        int t = base_ind[0];

        sum.x += data.advection[t][base_ind[1]][base_ind[2]].x;
        sum.y += data.advection[t][base_ind[1]][base_ind[2]].y;

        for (int i = 0; i < 8; i++){
            if (surrounding[i][0] >= 0){
                count ++;
                sum.x += data.advection[t][surrounding[i][0]][surrounding[i][1]].x;
                sum.y += data.advection[t][surrounding[i][0]][surrounding[i][1]].y;
            }
        }

        return new Vector(sum.x/(float)count, sum.y/(float)count);
    }

}