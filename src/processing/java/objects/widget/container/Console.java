package processing.java.objects.widget.container;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.java.event.events.ScrollEvent;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.TextBox;
import processing.java.objects.widget.Widget;
import processing.java.util.Color;
import processing.java.util.Util;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * container of labels where the head behaves as a text box
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class Console extends Container {

    private final int bufferSize;
    private final TextBox textBox;
    private int pointer;


    public Console(Vector position, int w, int h, int code, Color colour, String name, PImage... pImages) {
        super(position, w, h, code, colour, name, pImages);
        bufferSize = 25;
        textBox = new TextBox(position, w-32, 16, ">>>", Color.flip(colour));
        add(textBox);
        pointer = 0;
    }


    /**
     * overrides draw to check if anything was typed or it the text box was set
     * @param graphics graphics to draw on
     * @param ePosition effective position to draw at
     *
     * @author brian dunne
     */
    @Override
    public void draw(PGraphics graphics, Vector ePosition) {
        checkTextBox();
        super.draw(graphics, ePosition);

    }

    /**
     * overrides add to enforce a hard limit and cycles if full
     * @param object object to add
     * @return if the object successfully added
     *
     * @author brian dunne
     */
    @Override
    public boolean add(PObject object) {
        if(objects.length<bufferSize+1){//to include the text box
            addRow();
        }
        else{
            Util.rollArray(objects,-1);
            objects[objects.length-1] = null;
        }
        pointer = objects.length-1;
        return super.add(object);
    }

    /**
     * checks activity in the text box
     * creates new console line if the textBox content was entered
     *
     * @author brian dunne
     */
    private void checkTextBox(){
        if (getState()>=0&&textBox.readKeyboard()){
            setPercentY(100);
        }
        if(textBox.entered()){
            String command = textBox.getContent();
            if(executeCommand(command)) {
                set(objects.length - 1, 0, new Widget(textBox.position, W - 32, 16, textBox.getContent(), Widget.LEFT_CENTER, Color.EMPTY, Color.flip(getColour())));
                add(textBox);
            }
            textBox.clear();
        }
    }

    @Override
    public void scrollZ(int dir) {
        objects[pointer].setColour(Color.EMPTY);
        objects[pointer].clearResorces();
        pointer+=dir;
        pointer = Math.max(Math.min(pointer, objects.length-1),0);
        textBox.setText(pointer==objects.length-1? "": ((Widget)objects[pointer]).getText());
        if(pointer<objects.length-1) {
            objects[pointer].setColour(Color.GRAY);
            objects[pointer].clearResorces();
        }
    }

    private boolean executeCommand(String command){
        if(command.length()==0){
            return false;
        }
        ArrayList<String> components = new ArrayList<>();
        Scanner scanner = new Scanner(command);
        scanner.useDelimiter(" ");
        while (scanner.hasNext()){
            components.add(scanner.next());
        }
        try {
            switch (components.get(0)) {
                case ("previous"):
                    if (components.size() > 1) {
                        throw new IllegalArgumentException("too many arguments");
                    }
                    GraphContainer.event = new ScrollEvent(ScrollEvent.Y_SCROLL, ScrollEvent.DOWN);
                    return true;
                case ("next"):
                    if (components.size() > 1) {
                        throw new IllegalArgumentException("too many arguments");
                    }
                    GraphContainer.event = new ScrollEvent(ScrollEvent.Y_SCROLL, ScrollEvent.UP);
                    return true;
                case ("display"):
                    if (components.size() > 2) {
                        throw new IllegalArgumentException("too many arguments");
                    }
                    if (components.size() < 2) {
                        throw new IllegalArgumentException("too few arguments");
                    }
                    GraphContainer.event = new ScrollEvent(ScrollEvent.SET_X, Integer.parseInt(components.get(1)));
                    return true;
                default:
                    System.out.println("there is no such command: " + command);
                    return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
