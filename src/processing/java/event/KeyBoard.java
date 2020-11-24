package processing.java.event;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.java.event.events.ScrollEvent;
import processing.java.objects.widget.container.GraphContainer;
import java.util.ArrayList;
/**
 * more personalised keyboard event handler
 *
 * @author  brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class KeyBoard {
    private ArrayList<Character> activeChars;
    private ArrayList<Integer> keyCodes;//TODO fix codes for function keys

    public KeyBoard(PApplet applet){
        activeChars = new ArrayList<>();
        keyCodes = new ArrayList<>();
        applet.registerMethod("keyEvent", this);
    }

    /**
     * retrieves the existing event
     * @param event processing key event
     *
     * @author  brian dunne
     */
    public void keyEvent(KeyEvent event){
        switch (event.getAction()){
            case KeyEvent.PRESS:
                keyPressed(event);
                break;
            case KeyEvent.RELEASE:
                keyReleased(event);
        }
    }

    /**
     * personalised action in response to a key released event
     * @param event event in question, used to find out what key was released
     *
     * @author  brian dunne
     */
    private void keyReleased(KeyEvent event) {
        char c = event.getKey();
        for (int i = 0; i < activeChars.size() ; i++) {
            if(activeChars.get(i)==c){
                activeChars.remove(i);
                specialKeyCheck(c);

            }
        }
//        int i = event.getKey();
//        for (int j = 0; j < keyCodes.size() ; j++) {
//            if(keyCodes.get(j)==i){
//                keyCodes.remove(i);
//            }
//        }
    }

    /**
     * personalise action in response to a key pressed event
     * @param event event in question, used to find out what key was pressed
     *
     * @author  brian dunne
     */
    private void keyPressed(KeyEvent event) {
        char c = event.getKey();
        if(((int)c)!=65535 && !activeChars.contains(c)){ //filter out invalid  and already noted chars
            activeChars.add(c);
            specialKeyCheck(c);
        }
//        int i = event.getKeyCode();
//        if(!keyCodes.contains(i)){
//            keyCodes.add(i);
//            System.out.print(i+"");
//        }

    }

    /**
     * checks if the key is currently in the list of keys that have flagged a pressed event but not a released
     * @param key char to check
     * @return boolean whether it is in the list or not
     *
     * @author  brian dunne
     */
    public boolean carActive(char key) {
        return activeChars.contains(key);
    }

    /**
     * returns currently active characters
     * @return
     */
    public Character[] activeChars(){
        Character[] out = new Character[activeChars.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = activeChars.get(i);
        }
        return out;
    }

    /**
     * edge detector called on either edge of a key event, target edge id filtered out here
     *
     * @param c character to check
     *
     * @author  brian dunne
     */
    private void specialKeyCheck(char c){// add special key event here, maby changable with settings
        switch (c){
            case ('p'):
                if(activeChars.contains('p')) GraphContainer.event = new ScrollEvent(ScrollEvent.Y_SCROLL, ScrollEvent.DOWN);
                break;
            case ('n'):
                if(activeChars.contains('n'))GraphContainer.event = new ScrollEvent(ScrollEvent.Y_SCROLL, ScrollEvent.UP);
            case ('l'):
                if(activeChars.contains('l'))GraphContainer.event = new ScrollEvent(ScrollEvent.X_SCROLL, ScrollEvent.DOWN);
                break;
            case ('k'):
                if(activeChars.contains('k'))GraphContainer.event = new ScrollEvent(ScrollEvent.X_SCROLL, ScrollEvent.UP);
        }
    }
}
