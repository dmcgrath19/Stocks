package processing.java.objects.widget.container;


import processing.core.PGraphics;
import processing.core.PImage;
import processing.java.ProgrammingProject;
import processing.java.objects.widget.container.graph.Graph;
import processing.java.objects.widget.container.graph.ScatterPlot;
import processing.java.thread.data.stock.DataSet;
import processing.java.objects.base.properties.Vector;
import processing.java.thread.data.stock.Entry;
import processing.java.util.Color;
import processing.java.event.events.ScrollEvent;
import processing.java.thread.BuildDataSet;
import processing.java.util.DateTime;
import processing.java.util.Util;

/**
 * graph container, a crude extension to container to show graphs in stead of elements
 *
 * @author  brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class GraphContainer extends Container {
    private static final int RANGE_LIMIT = 4;
    public static ScrollEvent event;
    private DataSet dataSet;
    private Graph[] graphs;
    private int rangeOffset, xGraph, yGraph;
    private boolean flipFlop; //false changes x, true changes y
    private float mX, mY, lX, lY;


    public GraphContainer(Vector position, int w, int h, int code, Color colour, String name, PImage... pImages) {
        super(position, w, h, code, colour, name, pImages);
        graphs = new Graph[0];
        rangeOffset = 0;
        xGraph = 7; //dateTime
        yGraph = 5; //high
        flipFlop = false;

    }

    /**
     * handles scroll events sent to GraphContainers
     *
     * @author  brian dunne
     */
    @Override
    public void checkForEvent() {
        ProgrammingProject.mouse.setOverObject(this);
        if (dataSet == null)
        {
            dataSet = BuildDataSet.retrieveDataSet();
        }
        else if(event!=null){
            switch (event.action){
                case (ScrollEvent.Y_SCROLL):
                    scrollY(event.size);
                    break;
                case (ScrollEvent.X_SCROLL):
                    scrollX(event.size);
                    break;
                case (ScrollEvent.SET_Z):
                    scrollZ(flipFlop?(event.size-yGraph):event.size-xGraph);
                    break;
                case (ScrollEvent.SET_X):
                    graphs = new Graph[Math.max(Math.min(event.size,RANGE_LIMIT),0)];
                    refreshGraphs();
                    break;
            }
            event = null;
        }
    }

    /**
     * allows drawing of graphs in a container
     * @param graphics graphics to draw on
     * @param vector relative position
     *
     * @author  brian dunne
     */
    @Override
    public void draw(PGraphics graphics, Vector vector) {
        if (mouseOnGraphic(graphics, position.clone().add(vector.clone().mul(-1))))
            this.checkForEvent();// checks for event if mouse is over this


        Vector r = position;
        position = vector;
        drawBody(graphics);
        PGraphics localGraphic = ProgrammingProject.processing.createGraphics(getW() - 64, getH() - 128);
        localGraphic.beginDraw();
        localGraphic.background(250);
        localGraphic.textFont(ProgrammingProject.font);
        int i = 0;
        for (Graph graph : graphs) {
            position = r;
            Vector rTopLeft = topLeft();
            position = vector;
            Vector ePosition = topLeft().add(((((getW() - 8) / 4f) - 8) / 2f) + 8, 16 * 2).add(new Vector((16 * 0.5f) + (((getW() - 8) / 4f) - 8), 0).mul(i));
            graph.label.position = rTopLeft.add(topLeft().mul(-1)).add(ePosition);
            graph.label.draw(graphics, ePosition);
            graph.draw(localGraphic, mX, mY);
            i++;
        }
        localGraphic.fill(0);
        localGraphic.textSize(16 - 4);
        localGraphic.textAlign(ProgrammingProject.processing.LEFT, ProgrammingProject.processing.TOP);
        String stringMaxY, stringMinY, stringMaxX, stringMinX;
        if (Entry.getComponent(yGraph).equalsIgnoreCase("date")) //reformatting date as a readable date - E McDonald
        {
            stringMaxY = new DateTime(0, 1, 1, 1).addDays((int) mY).yyyyMmDd();
            stringMinY = new DateTime(0, 1, 1, 1).addDays((int) lY).yyyyMmDd();
        }
        else{
            stringMaxY = String.valueOf(mY);
            stringMinY = String.valueOf(lY);
        }
        if (Entry.getComponent(xGraph).equalsIgnoreCase("date")) //reformatting date as a readable date - E McDonald
        {
            stringMaxX = new DateTime(0, 1, 1, 1).addDays((int) mX).yyyyMmDd();
            stringMinX = new DateTime(0, 1, 1, 1).addDays((int) lX).yyyyMmDd();
        }
        else{
            stringMaxX = String.valueOf(mX);
            stringMinX = String.valueOf(lX);
        }
        localGraphic.text(stringMaxY, 0,0);
        localGraphic.textAlign(ProgrammingProject.processing.LEFT, ProgrammingProject.processing.BOTTOM);
        localGraphic.text("("+stringMinX+","+stringMinY+")", 0,localGraphic.height);
        localGraphic.textAlign(ProgrammingProject.processing.RIGHT, ProgrammingProject.processing.BOTTOM);
        localGraphic.text(stringMaxX, localGraphic.width,localGraphic.height);
        localGraphic.endDraw();
        graphics.image(localGraphic, topLeft().X+32, topLeft().Y + 64);
        graphics.fill(0);
        graphics.textSize(32-4);
        graphics.textAlign(ProgrammingProject.processing.CENTER, ProgrammingProject.processing.CENTER);
        graphics.text(Entry.getComponent(xGraph), topLeft().X+(getW()/2f), topLeft().Y+(getH()-(64-(32/2f))));
        graphics.pushMatrix();
        graphics.translate(topLeft().X+(32-(32/2f)), topLeft().Y+(getH()/2f));
        graphics.rotate((float) (-Math.PI/2));
        graphics.text(Entry.getComponent(yGraph),0,0);
        graphics.popMatrix();

        drawHeader(graphics);
        position = r;
    }

    /**
     * constructs graphs that will be drawn
     *
     * @author  brian dunne
     */
    private void refreshGraphs() {
        mX=mY=0;
        graphs = new Graph[graphs.length];
        for (int i = 0; i< graphs.length; i++){
            set(i,i);
        }
    }
    /**
     * implement scrollable y
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollY(int dir) {
        rangeOffset = Util.rollInt(rangeOffset, dir, 0, dataSet.nGroups());
        refreshGraphs();
    }
    /**
     * implement scrollable x
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollX(int dir) {
        Graph[] help = graphs.clone();
        graphs = new Graph[Math.max(Math.min(help.length+dir,RANGE_LIMIT),0)];
        for (int i = 0; i < graphs.length; i++) {
            if(i<help.length){
                graphs[i]=help[i];
            }
            else {
                set(i,i);
            }
        }
    }
    /**
     * implement scrollable z
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollZ(int dir) {
        if(flipFlop){
            yGraph = Util.rollInt(yGraph, dir, Entry.MIN, Entry.MAX);
        }else {
            xGraph = Util.rollInt(xGraph, dir, Entry.MIN, Entry.MAX);
        }
        flipFlop = !flipFlop;
        refreshGraphs();
    }


    public void resetGraphs()
    {
//        mX = 0;
//        mY = 0;
//        lX = 0;
//        lY = 0;
        rangeOffset = 0;
    }

    /**
     * set graph[graph][group]
     * @param graph graph index
     * @param group group index
     *
     * @author  brian dunne
     */
    public void set(int graph, int group){
        int subset = Util.rollInt(rangeOffset, group, 0, dataSet.nGroups());
        float angle = (float) (((float)subset/dataSet.nGroups())*(Math.PI*2));
        Color color = Color.colorCircleAt(angle);

        for (Graph graph1: graphs){
            if (graph1!=null&& Color.similarColors(graph1.label.getColour(), color)){
                color = Color.add(color, 100);
                color = Color.flip(color);
            }
        }
        graphs[graph] = new ScatterPlot(dataSet.getGroup(subset),Entry.getComponent(xGraph),Entry.getComponent(yGraph),color,((getW()-8)/4)-8, 16, subset, graph);

        mX = 0;
        mY = 0;
        for (Graph graph1 : graphs)
        {
            try {
                mX = Math.max(graph1.getMaxX(), mX);
                mY = Math.max(graph1.getMaxY(), mY);
            } catch (NullPointerException e) {}
        }
        if(lX == 0){    //Prevents lX from being 0 constantly
            lX = graphs[graph].getMinX();
        }
        else {
            lX = Math.min(graphs[graph].getMinX(), lX);
        }
        if(lY == 0){    //Prevents lY from being 0 constantly
            lY = graphs[graph].getMinY();
        }
        else {
            lY = Math.min(graphs[graph].getMinY(), lY);
        }
    }
}