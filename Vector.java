//package cloudscapes;

import java.lang.Math; 

public class Vector{
    public float x;
    public float y;

    public Vector(){
        x = 0;
        y = 0;
    };

    public float len(){
        return (float) Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

}