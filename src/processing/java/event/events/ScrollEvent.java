package processing.java.event.events;
/**
 * contains the information required recieve the event
 *
 * @author  brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class ScrollEvent {
    public static final int Y_SCROLL = 0;
    public static final int X_SCROLL = 1;
    public static final int Z_SCROLL = 2;
    public static final int SET_X = 3;
    public static final int SET_Y = 4;
    public static final int SET_Z = 5;
    private int time;

    public static final int UP = 1;
    public static final int DOWN = -1;
    public final int action, size, context;

    public ScrollEvent(int action, int size){this(action, size, 0);}
    public ScrollEvent(int action , int size, int context) {
        this.action=action;
        this.size = size;
        this.context = context;
        time= 10;
    }

    public byte checkRelevence(int context){
        if(context==this.context){
            return 1;
        }
        time--;
        if (time<=0){
            return -1;
        }
        return 0;
    }
}
