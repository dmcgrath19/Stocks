package processing.java.objects.widget;


import processing.core.PGraphics;
import processing.core.PImage;
import processing.java.ProgrammingProject;
import processing.java.event.Mouse;
import processing.java.objects.base.HitBox;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.util.Color;

/**
 * widget extends PObject to have states and events, description of ui.
 * by default acts as label.
 *
 * @author brian dunne, delia mcgrath
 * @version 0.0
 * @since   2020-04-10
 */
public class Widget extends PObject {
    public static final Vector RIGHT_CENTER = new Vector(ProgrammingProject.processing.RIGHT, ProgrammingProject.processing.CENTER);
    public static final Vector CENTER_CENTER = new Vector(ProgrammingProject.processing.CENTER, ProgrammingProject.processing.CENTER);
    public static final Vector LEFT_CENTER = new Vector(ProgrammingProject.processing.LEFT, ProgrammingProject.processing.CENTER);

    public static final int IDLE = 0;
    public static final int MOUSE_OVER = 1;
    public static final int CLICKED = 2;
    protected int state;
    public  String name;
    private Color textColour;
    protected Vector textAlignMode;
    private PImage whiteGear;


    public Widget(Vector position, int w, int h,String text, Color colour, PImage... pImages) {
        super(position, w, h, 1, colour, pImages);
        textColour = Color.flip(Color.add(getColour()!=Color.EMPTY? getColour():Color.WHITE,+50));
        whiteGear = ProgrammingProject.processing.loadImage("whitegear.png");
        state = 0;
        this.name = text;
        textAlignMode = CENTER_CENTER;
        setDragable(false);
    }
    public Widget(Vector position, int w, int h, String text, Vector textAlignMode, Color colour, Color textColour, PImage... pImages) {
        super(position, w, h, 1, colour, pImages);
        whiteGear = ProgrammingProject.processing.loadImage("whitegear.png");
        this.textColour = textColour;
        state = 0;
        this.name = text;
        this.textAlignMode = textAlignMode;
        setDragable(false);
    }



    /**
     * default check for event
     *
     * @author brian dunne
     */
    public void checkForEvent() {
        // do nothing by default
    }

    /**
     * default draw for widget
     * @param graphics graphic to draw on
     * @param vector local position
     *
     * @author brian dunne
     */
    public void draw(PGraphics graphics, Vector vector) {
        if(mouseOnGraphic(graphics,position.clone().add(vector.clone().mul(-1))))this.checkForEvent();// checks for event if mouse is over this
        Vector r = position;

        position = vector;
        show(graphics);
        position = r;
    }

    /**
     * default implementation
     *
     * @author brian dunne
     */
    public void draw() {
        draw(ProgrammingProject.processing.getGraphics(), position);
    }

    /**
     * gets information to pass to specific draw class
     * @param customization
     * @author Delia McGrath
     */
    public void drawCustomization(Customization customization){
        drawCustom(ProgrammingProject.processing.getGraphics(), position, customization);
    }

    /**
     * allows for the setting button to control the customization display
     * of the class -- if the setting button is turned on then it calls
     * on the draw class of customization to create the display
     * @param customization
     * @author Delia McGrath
     */
    public void drawCustom(PGraphics graphics, Vector vector, Customization customization) {
        ProgrammingProject.mouse.setOverObject(this);
        if(getState() == 2) {
            //setting the state manually here helps make it run more smoothly
            if(customization.display() == false){
                setState(4);
                customization.setDisplay(true);
            }
            else {
                setState(0);
                customization.setDisplay(false);
            }
        }
        Vector r = position;
        position = vector;
        show(graphics);
        position = r;
        if(customization.invert() && !customization.display()) {
            graphics.image(whiteGear, 5, 5);
        }

        if(customization.display()) {
            customization.drawDisplay(graphics);
        }
    }

    /**
     * check if mouse is over the graphic the this is bring drawn on
     * @param graphics graphics to check
     * @param gUpperLeft upper left of graphics
     * @return boolean
     *
     * @author brian dunne
     */
    public boolean mouseOnGraphic(PGraphics graphics, Vector gUpperLeft){
        Vector gPos = gUpperLeft.add(new Vector(graphics.width/2f, graphics.height/2f));
        HitBox gBox = new HitBox(gPos, graphics.width, graphics.height);
        return ProgrammingProject.mouse.setOverObject(gBox);
    }

    /**
     * override sprite to use state of object as index of image
     * @param graphics to draw on
     *
     * @author brian dunne
     */
    @Override
    public void sprite(PGraphics graphics) {
        graphics.image(getResource(state), -getW() / 2f, -getH() / 2f);
    }

    /**
     * override draw on to draw current state on the graphic
     * @param graphics graphic to draw on
     *
     * @author brian dunne
     */
    @Override
    public void drawOn(PGraphics graphics){
        if(getColour()!=Color.EMPTY) {
            graphics.fill(getColour().toInt());
            graphics.noStroke();
            graphics.rect(0, 0, getW(), getH());
        }

        drawText(graphics);

    }

    /**
     * draws the text on a graphic
     * @param graphics graphic to drat on
     * @author delia mcgrath
     */
    protected void drawText(PGraphics graphics){
        graphics.fill(textColour.toInt());
        graphics.textFont(ProgrammingProject.font);
        graphics.textSize(getH()-4);
        int x = (int) textAlignMode.X;
        int y = (int) textAlignMode.Y;
        graphics.textAlign(x,y);
        graphics.text(getText(), x==ProgrammingProject.processing.LEFT? 0: x==ProgrammingProject.processing.CENTER? getW()/2f : getW(),
                y==ProgrammingProject.processing.LEFT? 0: y==ProgrammingProject.processing.CENTER? getH()/2f : getH());
    }

    /**
     * default text to draw for widget
     * @return name of the widget
     *
     * @author delia mcgrath
     */
    public String getText() {
        return name;
    }

    @Override
    public String toString() {
        return "Widget:"+name;
    }

    /**
     * informs the widget of some mouse activity;
     * @param mouse mouse interacting with this
     */
    public void mouseActivity(Mouse mouse){
        //TODO move mouse selection of PObjects to mouse and PObject activity to PObjects
    }

    /**
     * public access to state
     *
     * @author brian dunne
     */
    public void setState(int state){
        this.state = state;
    }

    protected int getState(){
        return state;
    }
}
