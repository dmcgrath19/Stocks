package processing.java.objects.widget.button;

import processing.core.PGraphics;
import processing.java.ProgrammingProject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.IScrollable;
import processing.java.objects.widget.Widget;
import processing.java.util.Color;

/**
 * Sliding button, for stuff that has behaviour relative to a percent
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class Slider extends Button implements IScrollable{
    private final Button button;
    private float percent;
    public final char dir;

    public Slider(Vector position, int w, int h, char dir, String name, String label, int code,  Color color, float percent) {
        super(position, w, h, name, "", code, Color.add(color, -40));
        this.percent = 0;
        this.button = new Button(new Vector(0,0),
                dir=='x'?(int)Math.min(Math.max(w*percent,getH()),getW()/2f):getW(), dir=='y'? (int)Math.min(Math.max(h/percent, getW()),getH()/2f):getH(),name, label, code, Color.add(color, 100));
        button.setDragable(true);
        this.dir=dir;

    }

    /**
     * access for percent
     * @return percent
     *
     * @author  brian dunne
     */
    public float getPercent(){
        return percent;
    }

    /**
     * sets the button's position relative to percent
     *
     * @author  brian dunne
     */
    private void setButton() {
        int x = (int) ((topLeft().X+button.getW()/2)+(percent/100)*(getW()-button.getW()));
        int y = (int) ((topLeft().Y+button.getH()/2)+(percent/100)*(getH()-button.getH()));
        button.position = new Vector(x,y);
    }

    /**
     * override draw to allow the drawing of sub button to be drawn as well
     * @param graphics graphics to draw on
     * @param ePosition effective position
     *
     * @author  brian dunne
     */
    @Override
    public void draw(PGraphics graphics, Vector ePosition) {
        Vector rPosition = position;
        if(state == Widget.MOUSE_OVER) {
            graphics.fill(Color.add(getColour(), 10).toInt());
            if(ProgrammingProject.mouse.getHeldObject()==this) {
                if (dir == 'x') {
                    int range = button.getW()/4;
                    scrollX(ProgrammingProject.processing.mouseX-button.position.X>range?1:ProgrammingProject.processing.mouseX-button.position.X<-range?-1:0);
                } else {
                    int range = button.getH()/4;
                    scrollY(ProgrammingProject.processing.mouseY-button.position.Y>range?1:ProgrammingProject.processing.mouseY-button.position.Y<-range?-1:0);
                }
            }
        }
        ProgrammingProject.mouse.setOverObject(button);
        if(ProgrammingProject.mouse.getHeldObject()==button) {
            if (dir == 'x') {
                percent = ((((button.position.X) - (topLeft().X + button.getW()/ 2f))) / (getW() - button.getW())) * 100;
            } else {
                percent = ((((button.position.Y) - (topLeft().Y + button.getH() / 2f))) / (getH() - button.getH())) * 100;
            }
            applyPercentLimit();
        }
        setButton();
        position = rPosition;
        super.draw(graphics,ePosition);
        button.draw(graphics, button.position.clone().add((rPosition.clone().add(ePosition.clone().mul(-1))).mul(-1)));

    }

    /**
     * override draw on to draw the background slider
     * @param graphics graphics to draw on
     *
     * @author  brian dunne
     */
    @Override
    public void drawOn(PGraphics graphics){
        checkForEvent();
        graphics.stroke(10);
        graphics.fill(getColour().toInt());
        graphics.rect(0,0, this.getW(), this.getH());
    }

    /**
     * limit percent to 0 to 100
     *
     * @author  brian dunne
     */
    private void applyPercentLimit(){
        percent = Math.max(Math.min(percent,100),0);//force percentage limit
    }

    /**
     * implement scrollable y
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollY(int dir) {
        float p = (float)button.getH()/getH()*100;
        percent+=dir*p*0.25;
        applyPercentLimit();
    }

    /**
     * implement scrollable x
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollX(int dir) {
        float p = (float)button.getW()/getW()*100;
        percent+=dir*p*0.25;
        applyPercentLimit();
    }
    /**
     * implement scrollable z
     * @param dir direction of scroll
     *
     * @author  brian dunne
     */
    @Override
    public void scrollZ(int dir) {
        // slider has no z component
    }

    /**
     * implement scrollable set y
     * @param percentY percent to set y
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentY(float percentY) {
        percent = percentY;
    }

    /**
     * implement scrollable set x, same as above simply sets the percent
     * @param percentX percent to set x
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentX(float percentX) {
        setPercentY(percentX);
    }

    /**
     * implement scrollable set z
     * @param percentZ percent to set z
     *
     * @author  brian dunne
     */
    @Override
    public void setPercentZ(float percentZ) {
        // slider has no z component
    }
}
