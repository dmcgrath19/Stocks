package processing.java.event;


import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.java.objects.base.HitBox;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.IScrollable;
import processing.java.objects.widget.Widget;

/**
 * personalised mouse event handler
 *
 * @version 0.0
 * @since   2020-04-10
 */
public class Mouse extends HitBox {
    private PObject heldObject, overObject;
    private Vector offset;
    boolean locked;
    private int context;

    public Mouse(PApplet papp) {
        super(new Vector(papp.mouseX, papp.mouseY), 1, 1);
        papp.registerMethod("mouseEvent", this);
        locked = false;
        context = 0;
    }

    /**
     * retrieves the existing event
     * @param e processing key event
     *
     */
    public void mouseEvent(MouseEvent e) {
        switch(e.getAction()) {
            case MouseEvent.PRESS:
                mousePress(e);
                break;
            case MouseEvent.RELEASE:
                mouseRelease(e);
                break;
            case MouseEvent.DRAG:
                mouseDrag(e);
                break;
            case MouseEvent.MOVE:
                mouseMove(e);
                break;
            case MouseEvent.WHEEL:
                mouseWheel(e);
        }
    }

    /**
     * personalised response to a mouse wheel event, applies an appropriate scroll action on the overObject if it implements the scrollable interface.
     * @param e event in question, used to find the direction of scroll and additional function arguments 'ctrl', 'ctrl-alt'
     *
     */
    private void mouseWheel(MouseEvent e) {
        if(overObject instanceof IScrollable){
            if(e.isControlDown()){
                if(e.isAltDown()){
                    ((IScrollable) overObject).scrollZ(e.getCount());
                }else {
                    ((IScrollable) overObject).scrollX(e.getCount());
                }
            }else{
                ((IScrollable)overObject).scrollY(e.getCount());
            }
        }
    }

    /**
     * personalised response to a mouse move event, updates the position of this mouse
     * @param e event in question, used to find the position of the mouse as of event
     *
     */
    private void mouseMove(MouseEvent e) {
        position = new Vector(e.getX(),e.getY());
        if(overObject!=null&&!collide(overObject)){
            if(overObject instanceof Widget) {
                ((Widget) overObject).setState(Widget.IDLE); //inform change of state
            }
            overObject=null;
        }
    }

    /**
     * personalised response to a mouse dragend event, moves the held object to the same position relative to the mouse when the mouse picked it up
     * TODO allow the object to decide whether it wants to be moved or not
     * @param e event in question, finds the position of x as of event
     *
     */
    private void mouseDrag(MouseEvent e) {
        position = new Vector(e.getX(),e.getY());
        if(heldObject !=null && heldObject.isDragable()) {
            heldObject.position = position.clone().add(offset);
        }
    }

    /**
     * personalised response to a mouse pressed event, moves over object to heldObject, finds object position relative to mouse and informs said object that it has been clicked
     * @param e event in question, honestly not used in this event at the time of writing this cement
     *
     */
    private void mousePress(MouseEvent e) {
        locked = true;
        if(overObject!=null){
            heldObject=overObject;
            offset = heldObject.position.clone().add(position.clone().mul(-1));
            if (heldObject instanceof Widget) {
                ((Widget) heldObject).setState(Widget.CLICKED);
            }
        }
    }

    /**
     * personalised response to a mouse release event, resets heldObject, offset and locked, informs said object that it is no longer being held by the mouse
     * @param e event in question
     
     */
    private void mouseRelease(MouseEvent e) {
        if (heldObject instanceof Widget) {
            ((Widget) heldObject).setState(Widget.IDLE);
        }
        heldObject = null;
        offset = null;
        locked = false;
    }

    /**
     * issued by tne hitBox it's self, checks if the mouse currently not holding an hitBox then precedes to said hitBox infant is in contact with the mouse.
     * informs the previous over hitBox that it is no longer the over hitBox, informs current hitBox that it is the over hitBox,
     * experimental context such that global objects can used within some context TODO much more work to actually make this work.
     * @param hitBox hitBox checking if it should be the over hitBox this
     * @param context code/ parent/ relative position of the hitBox within some 'context' where it may appear in some another context, eg minimiser. currently disabled as context filter doesn't work
     *
     */
    public boolean setOverObject(HitBox hitBox, int context){
        if (heldObject==null&&!locked && collide(hitBox)){

            if (overObject!=null&&overObject instanceof Widget){
                ((Widget) overObject).setState(Widget.IDLE);
            }
            if(hitBox instanceof PObject) {
                overObject = (PObject) hitBox;
            }
            this.context = context;
            if (hitBox instanceof Widget) {
                ((Widget) hitBox).setState(Widget.MOUSE_OVER);
            }
            return true;
        }
        return false;
    }

    /**
     * old interface of setOverObject, allows use of function where context is irrelevant
     * @param hitBox cobjec checking to be over hitBox
     *
     */
    public boolean setOverObject(HitBox hitBox){
        return setOverObject(hitBox, 0);
    }

    /**
     * retrieves the current held object
     * @return heldObject
     *
     */
    public PObject getHeldObject() {
        return heldObject;
    }

    /**
     * retrieves dictated connect of most recent over object
     * @return int code representing context
     
     */
    public int getContext() {
        return context;
    }
}
