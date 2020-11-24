package processing.java.objects.widget.button;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.java.ProgrammingProject;
import processing.java.event.events.ScrollEvent;
import processing.java.objects.Explosion;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.Widget;
import processing.java.objects.widget.container.Container;
import processing.java.objects.widget.container.GraphContainer;
import processing.java.util.Color;
import processing.java.util.Util;

/**
 * Widget to manage button behaviour
 *
 * @author  brian dunne, delia mcgrath
 * @version 0.0
 * @since   2020-04-10
 */
public class Button extends Widget {
    public static final byte SCROLL_SET_X = 1;
    public static final byte SCROLL_SET_Y = 2;
    public static final byte SCROLL_SET_Z = 3;
    public static final byte GET_COMPANY = 4;
    public static final byte RETURN_COMPANY = 5;
    public static final byte EXPLODE = -1;

    /**
     * constructs a buttons code based on an action and weight,
     * @param action byte representing what action this code represents
     * @param code int representing the behaviour ogf the code
     * @return bsb 8 bits as action lsb 26 bits as code
     */
    public static int constructCode(Byte action, int code){
        byte overflow = (byte) (code>>26);
        if(overflow!=0&&overflow!=-1){
            System.out.println("warning code is too large, information lost");
        }
        return (action<<26)|(code&0x00ffffff);
    }

    public static final Button CLOSE_BUTTON = new Button(ProgrammingProject.middleOfScreen, 10, 10, "close", "X", constructCode(EXPLODE, -1), Color.RED, ProgrammingProject.processing.loadImage("face.png"));
    public static final Button MINIMISER = new Button(new Vector(-16, -16), 16, 16, "minimiser", "=", constructCode(SCROLL_SET_Z, 1), Color.GRAY, ProgrammingProject.processing.loadImage("face.png"));
    public static final Button SETTING_BUTTON = new Button(new Vector(10, 10), 10, 10, "setting", "O", 0, Color.GRAY, ProgrammingProject.processing.loadImage("gear.png"));

    protected String label;
    protected int context;
    protected final int code;

    public Button(Vector position, int w, int h, String name, String label, int code, Color color) {
        super(position, w, h,name, color);
        this.code = code;
        context = 0;
        this.label = label;
    }
    public Button(Vector position, int w, int h, String name, String label, int code, Color color, PImage image) {
        super(position, w, h,name, color, image);//, Util.getBorder(image)); // handle differently causes lag in load up
        this.code = code;
        context = 0;
        this.label = label;
    }

    /**
     * uses the button's code to decide what type of event it should issue
     * default implementation to handle basic common events, should be overridden for more custom events
     *
     * @author  brian dunne
     */
    public void buttonEventHandler(){
        byte eventType = (byte) (code>>26);
        int eventCode = code&0x00ffffff;
        switch(eventType){
            case(SCROLL_SET_X):
                GraphContainer.event = new ScrollEvent(ScrollEvent.SET_X, eventCode, context);
                break;
            case(SCROLL_SET_Y):
                GraphContainer.event = new ScrollEvent(ScrollEvent.SET_Y, eventCode, context);
                break;
            case(SCROLL_SET_Z):
                GraphContainer.event = new ScrollEvent(ScrollEvent.SET_Z, eventCode, context);
                break;
            case(GET_COMPANY):
                int width = (int) ProgrammingProject.middleOfScreen.X*2;
                int height = (int) ProgrammingProject.middleOfScreen.Y*2;
                byte radioEventType = 1;
                int radioEventCode = eventCode|(radioEventType<<26);
                //mainContainer = new Container(new Vector(middleOfScreen.X + width/10, middleOfScreen.Y - height/16), width*4/5,height*7/8,1, Color.RED,"Main Display");
                try{
                    RadioButtons radioButtons = new RadioButtons(new Vector(ProgrammingProject.middleOfScreen.X - width*2/5, ProgrammingProject.middleOfScreen.Y - height/16), width*1/5, height*7/8, radioEventCode, new Color(200, 200, 200), "Companies");
                    ProgrammingProject.processing.screens[0].addWidget(radioButtons);
                }catch (Exception e){
                    System.out.println("dataset isn't ready yet");
                }

                break;
            case(RETURN_COMPANY):
                int company = eventCode&0x0000ffff;
                int graphN = (eventCode&0x00ff0000)>>16;
                //for (Widget widget : ProgrammingProject.processing.screens[0].widgetList)
                for (int i = ProgrammingProject.processing.screens[0].widgetList.size()-1; i >= 0; i--)
                {
                    Widget widget = ProgrammingProject.processing.screens[0].widgetList.get(i);
                    if (widget instanceof Container)
                    {
                        for (PObject object : ((Container) widget).objects)
                        {
                            if (object instanceof GraphContainer)
                            {
                                ((GraphContainer) object).resetGraphs();
                                ((GraphContainer) object).set(graphN, company);
                            }
                        }
                    }
                    if (widget instanceof RadioButtons)
                    {
                        ProgrammingProject.processing.screens[0].widgetList.remove(i);
                    }
                }
                break;
            case(-1):
                PImage image = ProgrammingProject.processing.loadImage("face.png");
                image.resize(ProgrammingProject.processing.width/2, ProgrammingProject.processing.height/2);
                PObject object = new PObject(position, image.width, image.height,1,Color.BLUE, image);
                Explosion explosion = new Explosion(image.width, 200, object, 0.005f, 128*64);
                ProgrammingProject.processing.addObject(explosion);
                break;
        }
    }



    /**
     * overrides draw on to draw a button instead
     * @param graphics graphics to draw on
     *
     * @author  brian dunne, delia mcgrath
     */
    @Override
    public void drawOn(PGraphics graphics){
        Vector rPosition = position;

        graphics.stroke(10);
        float weight = graphics.strokeWeight;
        if(state == 1){
            graphics.strokeWeight(graphics.strokeWeight+1);
        }
        if (state == 2) {
            graphics.fill(Color.add(getColour(),-40).toInt());
        } else {
            graphics.fill(getColour().toInt());
        }
        graphics.rect(0, 0, this.getW()-1, this.getH()-1);
        graphics.textAlign(graphics.CENTER, graphics.CENTER);
        graphics.textSize(getH()-2);
        if(getColour()== Color.GRAY || getColour() == Color.RED||getColour()==Color.CYAN)
            graphics.fill(Color.BLACK.toInt());
        else
            graphics.fill(Color.flip(Color.add(getColour(),+50)).toInt());
        graphics.text(label, getW()/2f, (getH()-2)/2f);
        position = rPosition;
        graphics.strokeWeight(weight);
        graphics.noStroke();
    }

    /**
     * crude event check, tells mouse to consider it as over object
     *
     * @author  brian dunne
     */
    public void checkForEvent(){
        ProgrammingProject.mouse.setOverObject(this, code);
    }

    /**
     * overrides setState to issue the button clicked event when it's state is set to clicked
     * @param state state to set this to
     *
     * @author  brian dunne
     */
    @Override
    public void setState(int state) {
        if(state == Widget.CLICKED){
            buttonEventHandler();
        }
        super.setState(state);
    }

    /**
     * checks if the button on
     * @return boolean
     * provides more readable code
     * @author delia mcgrath
     */
    public boolean isOn(){
        return (super.getState() == 2);
    }

    /**
     * turns the button off
     *(defines state 0)
     * @author delia mcgrath
     */
    public void turnOffButton(){
        super.setState(0);
    }

    /**
     * attempt at filtering out activity of the button in a different context, defaulting to 0 in different context
     * @return int corresponding to state
     * @author delia mcgrath
     */
    @Override
    public int getState() {
        if(ProgrammingProject.mouse.getContext()==code) {
            return super.getState();
        }
        return 0;
    }

    /**
     * implementation of toString
     * @return string representation of button
     *
     * @author  brian dunne
     */
    @Override
    public String toString() {
        return name+":["+label+"]";
    }

    /**
     * sets the context of the button
     * @param context next context
     *
     * @author  brian dunne
     */
    public void setContext(int context) {
        this.context = context;
    }
}