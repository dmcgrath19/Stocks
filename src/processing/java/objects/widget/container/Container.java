package processing.java.objects.widget.container;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.java.ProgrammingProject;
import processing.java.event.events.ScrollEvent;
import processing.java.objects.base.HitBox;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.IScrollable;
import processing.java.objects.widget.Widget;
import processing.java.objects.widget.button.Button;
import processing.java.objects.widget.button.Slider;
import processing.java.thread.BuildDataSet;
import processing.java.util.Color;

/**
 * container extends widget to resemble a htm table, it automatically compresses elements when initialised
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class Container extends Widget implements IScrollable {
    /**
     * compresses a container to reduce it's size if contents don't fill it
     * @param container container to compress;
     * @return new compressed container with the same contents as passed container
     */
    public static Container compressH(Container container) {
        Container newContainer = new Container(container.position.clone(),container.getW(),Math.min(container.totalHeight()+16,container.getH()),container.code,container.getColour(),container.name);
        newContainer.addRows(container.rowH.length);
        newContainer.addCols(container.colW.length-1);
        for (PObject o: container.objects) {
            newContainer.add(o);
        }
        return newContainer;
    }

    public static ScrollEvent event;
    public PObject[] objects;
    public final Slider[] sliders;
    private int[] colW, rowH;
    private boolean minimised;
    private final int code;

    private int viewX;
    private int viewY;

    public Container(Vector position, int w, int h,int code, Color colour, String name, PImage... pImages) {
        super(position, w, h, name, colour, pImages);
        objects = new PObject[0];
        sliders = new Slider[2];// sliders[0]x scroller, slider[1]y scroller
        colW = rowH = new int[0];
        minimised = false;
        this.code = code;
        viewX = viewY = 0;
    }

    /**
     * add object to container
     * @param object object to add
     * @return whether it successfully added or not
     *
     * @author  brian dunne
     */
    public boolean add(PObject object){
        for(int i=0;i<rowH.length; i++){
            for(int j=0; j<colW.length; j++){
                if(get(i,j)==null){
                    set(i, j, object);

                    float w;
                    if((w=totalWidth())>this.getW()){
                        sliders[0] = new Slider(position.clone(), getW()-16, 16,'x', "x_scroller", "",
                                0, this.getColour(), (w-getW())/getW());
                    }
                    float h;
                    if((h= totalHeight())>this.getH()){
                        sliders[1] = new Slider(position.clone(), 16, getH()-16,'y', "y_scroller", "",
                                0,this.getColour(), (h-getH())/getH());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * adds the width each column
     * @return int total width
     *
     * @author  brian dunne
     */
    private int totalWidth() {
        int total = 0;
        for (int value : colW) {
            total += value + 8;
        }
        return total+(sliders[1]!= null? sliders[1].getW():0)+getXSpace()/2;
    }

    /**
     * adds the height of each row
     * @return int total height
     *
     * @author  brian dunne
     */
    private int totalHeight() {
        int total = 0;
        for (int value : rowH) {
            total += value + 8;
        }
        return total+(sliders[0]!= null? sliders[0].getH():0)+getYSpace()/2;
    }

    /**
     * sets position [i][j] to object
     * @param i row
     * @param j col
     * @param object object to set
     *
     * @author  brian dunne
     */
    public void set(int i, int j, PObject object){
        get(i,j);
        rowH[i] = Math.max(rowH[i], object.getH());
        colW[j] = Math.max(colW[j], object.getW());
        objects[(i*colW.length)+j] = object;
    }

    /**
     * retrieves the object at [i][j]
     * @param i row
     * @param j col
     * @return object@[i][j]
     *
     * @author  brian dunne
     */
    public PObject get(int i, int j){
        if(i>=rowH.length){
            throw new IndexOutOfBoundsException(i+" with only "+rowH.length+" rows");
        }
        if(j>=colW.length){
            throw new IndexOutOfBoundsException(j+" with only "+colW.length+" columns");
        }
        try {
            return objects[(i * colW.length) + j];
        }catch (Exception e){
            return null;
        }
    }

    /**
     * add n rows to the container
     * @param n number of new rows
     *
     * @author  brian dunne
     */
    public void addRows(int n){
        int r = Math.max(rowH.length+n,1);
        int c = Math.max(colW.length,1);
        reSize( r, c);
    }

    /**
     * add one row to the container
     *
     * @author  brian dunne
     */
    public void addRow(){
        addRows(1);
    }

    /**
     * add n columns to the container
     * @param n number of new columns
     *
     * @author  brian dunne
     */
    public void addCols(int n){
        int r = Math.max(rowH.length,1);
        int c = Math.max(colW.length+n,1);
        reSize( r, c);
    }

    /**
     * add one column to the container
     *
     * @author  brian dunne
     */
    public void addCol(){
        addCols(1);
    }
    private void reSize(int r, int c){
        int hr = rowH.length;
        int hc = colW.length;
        PObject[] ho = objects.clone();
        objects = new PObject[r*c];
        rowH = new int[r];
        colW =new int[c];
        viewX = r;
        viewY = c;
        for(int i=0; i<hr;i++){
            for(int j = 0; j<hc;j++){
                try {
                    set(i,j,ho[(i*hc)+j]);
                }catch (Exception ignored){
                }
            }
        }
    }

    /**
     * implementation of toString
     * @return string representation of the container
     */
    @Override
    public String toString() {
        return name + ":["+rowH.length+", "+colW.length+"]";
    }

    /**
     * overrides check event to check for mouse interaction
     *
     * @author  brian dunne
     */
    @Override
    public void checkForEvent() {
        ProgrammingProject.mouse.setOverObject(this);
    }

    /**
     * overrides draw to include a header, body and contents.
     * allows scrollable content
     * @param graphics graphics to draw on
     * @param ePosition effective position to draw at
     *
     * @author  brian dunne
     */
    @Override
    public void draw(PGraphics graphics, Vector ePosition) {
        if(mouseOnGraphic(graphics,position.clone().add(ePosition.clone().mul(-1))))this.checkForEvent();// checks for event if mouse is over this
        graphics.stroke(0);
        Vector rPosition = position;
        Vector ePositionb = ePosition.clone();
        if(!minimised) {
            position = ePosition;
            drawBody(graphics);
            position = rPosition;

            PGraphics localGraphic = ProgrammingProject.processing.createGraphics(getW() - (sliders[1] != null ? sliders[1].getW() : 0), H - 16 - (sliders[0] != null ? sliders[0].getH() : 0));
            localGraphic.beginDraw();
            localGraphic.textFont(ProgrammingProject.font);
            localGraphic.textSize(48/3f);
            Vector start = new Vector(0, 0);
            start.add(new Vector(8, 8));
            int[] jLimits = new int[]{sliders[0]!=null? (int) ((colW.length-(viewX+1))*sliders[0].getPercent()/100) :0,colW.length};
            int[] iLimits = new int[]{sliders[1]!=null? (int) ((rowH.length-(viewY+1))*sliders[1].getPercent()/100) :0,rowH.length};
            for (int i = iLimits[0]; i < iLimits[1]; i++) {
                for (int j = jLimits[0]; j < jLimits[1]; j++) {
                    PObject object = get(i, j);
                    if (object instanceof Widget) {//check here if size is different than expected and react if needed
                        ePosition = new Vector(((start.X + getXoffset(j)) + object.getW()/2f), ((start.Y + getYoffset(i)) + object.getH()/2f));
                        Vector scroll = new Vector((sliders[0] != null ? (sliders[0].getPercent() / 100) * (totalWidth() - getW()) : 0),
                                (sliders[1] != null ? (sliders[1].getPercent() / 100) * (totalHeight() - localGraphic.height) : 0));

                        ePosition.add(scroll.mul(-1));
                        if ((ePosition.X > (-object.getW()/2f) && ePosition.X < (localGraphic.width + object.getW()/2f)) &&
                                (ePosition.Y > (-object.getH()/2f) && ePosition.Y < (localGraphic.height + object.getH()/2f))) {// if appears on screen draw
                            object.position = ePosition.clone().add(topLeft().clone().add(new Vector(0, 16)));
                            ((Widget) object).draw(localGraphic, ePosition);
                        }else { //apply limits to  loops, less effective when scrolling down use one or the other
                            if (ePosition.X > (localGraphic.width + object.getW()/2f)) {
                                jLimits[1] = j;
                                viewX = j - jLimits[0];
                            }
                            if ((ePosition.X < (-object.getW()/2f)))
                                jLimits[0] = j;
                            if (ePosition.Y > (localGraphic.height + object.getH()/2f)){
                                iLimits[1] = i;
                                viewY = i - iLimits[0];
                            }
                            if (ePosition.Y < (-object.getH()/2f))break;
                        }
                    }
                }
            }
            position = ePositionb;
            localGraphic.endDraw();
            graphics.image(localGraphic, topLeft().X, topLeft().Y + 16);//content graphic
            for (Slider slider : sliders) {//sliders
                if (slider != null && !minimised) {
                    if (slider.dir == 'x') {
                        slider.position = rPosition.clone().add(new Vector(-8, (float) ((getH() / 2) - slider.getH()/2)));
                        ePosition = ePositionb.clone().add(new Vector(-8, (getH()/2f) - slider.getH()/2f));
                    } else {
                        slider.position = rPosition.clone().add(new Vector((getW()/2f) - slider.getW()/2f, 8));
                        ePosition = ePositionb.clone().add(new Vector((getW()/2f) - slider.getW()/2f, 8));
                    }
                    slider.draw(graphics, ePosition);
                }
            }
        }
        position = ePositionb;
        drawHeader(graphics);
        position = rPosition;
        HitBox header = new HitBox(topLeft().add(getW()/2, getYSpace()/2),getW(),getYSpace());
        if(ProgrammingProject.mouse.setOverObject(header)){
            ProgrammingProject.mouse.setOverObject(this);
        }
        //TODO fix the following functions
        //drawMinimiser(graphics);
    }

    /**
     * public access to code
     * @return code
     *
     * @author  brian dunne
     */
    public int getCode() {
        return code;
    }

    /**
     * draw the header of a container
     * @param graphics graphics to draw on
     *
     * @author  brian dunne
     */
    protected void drawHeader(PGraphics graphics){
        graphics.stroke(0);
        graphics.fill(Color.add(getColour(),-50).toInt());
        graphics.rect(topLeft().X,topLeft().Y, getW(), getYSpace());
        graphics.textAlign(graphics.CENTER, graphics.CENTER);
        graphics.fill(Color.flip(getColour()).toInt());
        graphics.textSize(getYSpace()-(getYSpace()/8f));
        graphics.text(name, topLeft().X+getW()/2f, topLeft().Y+getYSpace()/2f);
    }

    /**
     * draw the body of a container
     * @param graphics graphics to draw on
     *
     * @author  brian dunne
     */
    protected void drawBody(PGraphics graphics){
        if (state==Widget.MOUSE_OVER) {
            graphics.fill(getColour().toInt());
        } else {
            graphics.fill(Color.add(getColour(), 50).toInt());
        }
        graphics.rect(topLeft().X, topLeft().Y, getW(), getH());
    }

    /**
     * draw the minimiser button with the context of this
     * @param graphics graphics to draw on
     */
    protected void drawMinimiser(PGraphics graphics){
        Vector ePosition = topLeft().add(getW()-Button.MINIMISER.getW()/2f,Button.MINIMISER.getH()/2f);
        Button.MINIMISER.setContext(code);
        Button.MINIMISER.draw(graphics, ePosition);
    }

    /**
     * implement scrollable y
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollY(int dir) {
        if (sliders[1]!=null){
            sliders[1].scrollY(dir);
        }
    }
    /**
     * implement scrollable x
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollX(int dir) {
        if (sliders[0]!=null){
            sliders[0].scrollY(dir);
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
        minimised = !minimised;
    }

    /**
     * implement scrollable set y
     * @param percentY percent to set y
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentY(float percentY) {
        if (sliders[1]!=null){
            sliders[1].setPercentY(percentY);
        }
    }

    /**
     * implement scrollable set x
     * @param percentX percent to set x
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentX(float percentX) {
        if (sliders[0]!=null){
            sliders[0].setPercentX(percentX);
        }
    }

    /**
     * implement scrollable set Z, if percent greater than 50 round up else round down
     * @param percentZ percent to set z
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentZ(float percentZ) {
        minimised = percentZ>50;
    }

    /**
     * get offset to x for column i
     * @param i col index of object
     * @return x offset for column
     *
     * @author  brian dunne
     */
    protected int getXoffset(int i){
        int out = 0;
        for (int j = 0; j < Math.min(i,colW.length); j++) {
            out+= colW[j]+getXSpace()/2f;
        }
        return out;
    }

    /**
     * get offset to x for row i
     * @param i row index of object
     * @return y offset for row
     *
     * @author  brian dunne
     */
    protected int getYoffset(int i){
        int out = 0;
        for (int j = 0; j < Math.min(i,rowH.length); j++) {
            out+= rowH[j]+getYSpace()/2;
        }
        return out;
    }

    /**
     * trying to add comparability for resizability
     * @return int regular button size
     *
     * @author  brian dunne
     */
    private int getXSpace(){
        return Button.MINIMISER.getW();
    }
    private int getYSpace(){
        return Button.MINIMISER.getH();
    }

    public void clear() {
        objects = new PObject[0];
        sliders[0] = null;
        sliders[1] = null;
        colW = rowH = new int[0];
        minimised = false;
        viewX = viewY = 0;
    }

    //Updates names of data containers to reflect company name instead of ticker
    public void updateNames()
    {
        for(PObject object : objects)
        {
            if(object instanceof Container)
            {
                Container container = (Container) object;
                container.updateName();
            }
        }
    }

    public void updateName()
    {
        if(!BuildDataSet.detailSet.getCompanyName(this.name).equals(""))
        {
            this.name = BuildDataSet.detailSet.getCompanyName(this.name);
        }
    }
}