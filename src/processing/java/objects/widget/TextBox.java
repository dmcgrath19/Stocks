package processing.java.objects.widget;


import processing.core.PGraphics;
import processing.java.ProgrammingProject;
import processing.java.objects.base.properties.Vector;
import processing.java.util.Color;

public class TextBox extends Widget{

    private Character[] previous;
    private Character[] difference;
    private boolean writable;
    private boolean active;
    private byte timer1;
    private String text;

    public TextBox(Vector position, int w, int h, String name, Color textColor) {
        super(position, w, h, name, Widget.LEFT_CENTER, Color.EMPTY, textColor);
        previous = new Character[0];
        active = false;
        clear();
    }

    /**
     * overrides sprite to always use index 0;
     * @param graphics to draw on
     *
     * @author brian dunne
     */
    @Override
    public void sprite(PGraphics graphics) {
        graphics.image(getResource(0), -getW() / 2f, -getH() / 2f);
        active = false;
    }

    /**
     * overrides get text to have a blinky corser and a hint
     * @return string of characters to display
     *
     * @author brian dunne
     */
    @Override
    public String getText() {
        return name+text+((timer1<=0&&timer1>-10&&active)?"|":"");
    }

    /**
     * checks the difference in active character in the keyboard and adds to the buffer if there is a difference and if
     * that the result of the difference contains characters, modeled after intellij console behaviour, timers are also ticked here
     * @return true if something was wrote to the text box, false if not
     *
     * @author brian dunne
     */
    public boolean readKeyboard(){
        active = true;
        boolean hasActivity = false;
        if(writable){
            Character[] next = ProgrammingProject.keyBoard.activeChars();
            if(next.length!=previous.length) {
                if (next.length > previous.length) {
                    difference = new Character[next.length - previous.length];
                } else{
                    difference = new Character[Math.max(difference.length-(previous.length-next.length),0)];
                }
                for (int i = 0; i < difference.length; i++) {
                    difference[i] = next[next.length - (1 + i)];
                }
                hasActivity = addDifference();
                timer1 = 11;
            }
            if(timer1 >0){
                timer1--;
            }
            else{
                hasActivity = addDifference();
            }
            if (timer1==0){
                timer1 = -20;
            }
            if (timer1<0){
                timer1++;
            }
            previous = next;
            clearResorces();
        }
        return hasActivity;
    }

    /**
     * adds the new characters to the buffer
     * @return whether or not anything was actually added
     *
     * @author brian dunne
     */
    private boolean addDifference() {
        if(difference.length>0) {
            for (Character c : difference) {
                if (c.equals('\b')) {
                    if (text.length() >= 1) {
                        text = text.substring(0, text.length() - 1);
                    }
                } else if (c.equals('\n')) {
                    writable = false;
                    return true;
                }else {
                    text = text + c;
                }
            }
            timer1=-20;
            return true;
        }
        return false;
    }

    /**
     * retrieve the entered content of the textBox
     * @return string content of the box, what was typed in
     *
     * @author brian dunne
     */
    public String getContent(){
        return text;
    }

    /**
     * checks if the text box is currently writable
     * @return compliment of writable
     *
     * @author brian dunne
     */
    public boolean entered(){
        return !writable;
    }

    /**
     * resets the textBox and re enables it.
     *
     * @author brian dunne
     */
    public void clear(){
        writable = true;
        difference = new Character[0];
        timer1 = 0;
        text = "";
        clearResorces();
    }


    @Override
    public String toString() {
        return "TextBox:"+text;
    }

    public void setText(String s) {
        text = s;
    }
}
