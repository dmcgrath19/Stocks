package processing.java;

import processing.core.PApplet;
import processing.core.PFont;
import processing.java.objects.base.HitBox;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Force;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.Customization;
import processing.java.objects.widget.button.RadioButtons;
import processing.java.objects.widget.container.Console;
import processing.java.objects.widget.container.Container;
import processing.java.objects.widget.container.GraphContainer;
import processing.java.util.Color;
import processing.java.util.Screen;
import processing.java.event.KeyBoard;
import processing.java.event.Mouse;
import processing.java.thread.BuildDataSet;

import java.util.ArrayList;

/**
 * this is the main
 *
 * @author brian dunne,
 * @version 0.0
 * @since   2020-04-10
 */
public class ProgrammingProject extends PApplet{
    public static final int MIN_HEIGHT = 400;   //Defining constants for resizing. - E McDonald
    public static final int MIN_WIDTH = 400;
    public static Vector middleOfScreen;        //Vector to maintain position when resizing. - E McDonald

    public static final String DATA_DIRECTORY_PATH= System.getProperty("user.dir")+"\\data";
    public static PFont font;
    public Container dataContainer;
    public Container mainContainer;
    //public RadioButtons radioButtons;
    public Screen[] screens;
    public int currentScreen = 0;
    public static ProgrammingProject processing;
    public static Mouse mouse;
    public static KeyBoard keyBoard;
    Customization customization;

    private ArrayList<PObject> objects;
    private Console mainConsole;

    public static void main(String... args){
        PApplet.main("processing.java.ProgrammingProject");
    }

    public void settings(){
        size(1000, 1000);
    }

    public void setup() {
        processing = this;
        surface.setResizable(true);
        middleOfScreen = new Vector(width/2f, height/2f);
        mouse = new Mouse(this);
        keyBoard = new KeyBoard(this);
        this.frameRate(30);
        screens = new Screen[1];
        screens[0] = new Screen(0,Color.GRAY/*, new Customization(screens[0])*/); // idk what's happening here
        dataContainer = new Container(middleOfScreen, width*4/5 - 20,(int) (height/3f),2, Color.CYAN,"Data");
        mainContainer = new Container(new Vector(middleOfScreen.X + width/10, middleOfScreen.Y - height/16), width*4/5,height*7/8,1, Color.RED,"Main Display");
        mainContainer.setDragable(true);
        mainContainer.addRows(2);
        mainContainer.add(dataContainer);
        GraphContainer graphContainer = new GraphContainer(new Vector(0,0),width*4/5 - 20,height*1/2,3,Color.GRAY,"Graph");
        mainContainer.add(graphContainer);
        screens[0].addWidget(mainContainer);
        customization = new Customization(graphContainer, mainContainer, screens[0]);
        screens[0].addCustomization(customization);
        //radioButtons = new RadioButtons(new Vector(middleOfScreen.X - width * 2/5, middleOfScreen.Y - height/16), width/5, height*7/8, 0, new Color(200, 200, 200), "Companies");
        //screens[0].addWidget(radioButtons);
        BuildDataSet.addBuilder(BuildDataSet.STOCKS);
        BuildDataSet.addBuilder(BuildDataSet.ONE_K);
        font = loadFont("ACaslonPro-Bold-48.vlw");
        textFont(font);


        background(0);

        objects = new ArrayList<>();
        mainConsole = new Console(new Vector(width/2f, height-(height/16f)), width, height/8,111, Color.add(Color.GRAY, -100), "Console");
    }

    public void draw() {
        checkForResize();
        background(255);

        screens[currentScreen].draw();  //Most drawing work shifted over to screen.draw().

        ArrayList<HitBox> toDelete = new ArrayList<>();
        for(PObject object: objects){
            object.tick(Force.NONE);
//            if(object.toDelete()){
//                object.
//            }TODO mend cleanup
        }
        mainConsole.draw();
    }

    /**
     * Updates screen height & width and middleOfScreen.
     *
     */
    private void checkForResize(){
        if(height < MIN_HEIGHT) {
            surface.setSize(width, MIN_HEIGHT);
        }
        if(width < MIN_WIDTH) {
            surface.setSize(MIN_WIDTH, height);
        }
        middleOfScreen.changePosition(width/2f, height/2f); //middleOfScreen always points to middle, regardless of size
    }

    /**
     *adds an object to the tick list
     * @param object object to add
     *
     */
    public void addObject(PObject object){
        objects.add(object);
    }

}
