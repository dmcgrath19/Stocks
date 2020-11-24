//Graph: abstract class - contains constructor and getters/setters for implementations.

package processing.java.objects.widget.container.graph;

import processing.core.PGraphics;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.Widget;
import processing.java.objects.widget.button.Button;
import processing.java.thread.data.stock.DataSet;
import processing.java.util.Color;

import java.util.Arrays;

/**
 * abstract graph containing vectors managing points
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public abstract class Graph{

    protected float maxX, maxY, minX, minY;
    protected Vector[] points;
    public final Button label;
    private final int thisIndex;

    protected Graph(DataSet dataSet, String xGraph, String yGraph, Color color,int w,int h, int i, int index) {
        if (dataSet.getSize()<2) {
            throw new IllegalArgumentException("dateRange for graph must include at least 2 elements");
        }
        points = dataSet.getPoints(xGraph, yGraph);
        float maxX = 0f;
        float maxY = 0f;
        float minX = 0f;
        float minY = 0f;
        for(Vector point: points){
            maxX = Math.max(maxX, point.X);
            maxY = Math.max(maxY, point.Y);
            //Should properly set minX and minY -E McDonald
            if(minX != 0f) {
                minX = Math.min(minX, point.X);
            }
            else {
                minX = point.X;
            }
            if(minY != 0f) {
                minY = Math.min(minY, point.Y);
            }
            else {
                minY = point.Y;
            }
        }
        this.maxX=maxX;
        this.maxY=maxY;
        this.minX=minX;
        this.minY=minY;
        try {
            Arrays.sort(points);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Arrays.sort(points);
        }
        //Button(Vector position, int w, int h, String name, String label, int code, Color color)
        thisIndex = index;
        label = new Button(new Vector(0,0),w,h,"company "+i, dataSet.name+":"+i, (Button.GET_COMPANY<<26)|thisIndex, color);
    }

    /**
     * access for maxX, maxY, minX, minY
     * @return corresponding attributes
     *
     * @author  brian dunne
     */
    public float getMaxX() {
        return maxX;
    }
    public float getMaxY() {
        return maxY;
    }
    public float getMinX() {
        return minX;
    }
    public float getMinY() {
        return minY;
    }

    public void resetMinMax()
    {
        maxX = 0;
        maxY = 0;
        minX = 0;
        minY = 0;
    }

    /**
     * abstract draw method, to be implemented by graph types
     * @param graphics to draw on
     * @param maxX maxX of graphic
     * @param maxY maxYof graphic
     *
     * @author  brian dunne
     */
    public abstract void draw(PGraphics graphics, float maxX, float maxY);
}
