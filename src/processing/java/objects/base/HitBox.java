package processing.java.objects.base;

import processing.core.PGraphics;
import processing.java.ProgrammingProject;
import processing.java.objects.base.properties.Vector;
import processing.java.util.Color;

/**
 * 2d hit box, collisions between 2d objects
 * superclass of all buttons/widgets/containers, by default, it's just a rectangle.
 *
 * @version 0.0
 * @since   2020-04-10
 */
public class HitBox implements Cloneable{
    protected int W, H;
    protected int IFrames;
    public Vector position;
    protected float angle;



    public HitBox(Vector position, int w, int h){
        this.position = position;
        this.angle = 0;
        this.W = w;
        this.H = h;
    }

    /** implementation of clone
     * @return copy of hit box
     *
     * @author  brian dunne
     */
    @Override
    public HitBox clone(){
        HitBox clone = null;
        try{
            clone = (HitBox) super.clone();
            clone.position = position.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * returns updated hitBox height based on screen height, called in every hitBox & hitBox subclass draw function
     * @return some h
     *
     */
    public int getH() {
        return (int) (H/1000f*ProgrammingProject.processing.height);
    }
    /**
     * returns updated hitBox width based on screen width, called in every hitBox & hitBox subclass draw function
     * @return some h
     *
     */
    public int getW() {
        return (int) (W/1000f*ProgrammingProject.processing.width);
    }

    /**
     * checks if hotbox has reached thw wall with 0 tolerance
     * @return int code representing what walls
     *
     */
    protected int wallHit(){return wallHit(0.0f);}

    /**
     * checks if hotbox has reached thw wall with a certain tolerance
     * @param tolerance proportion of the hit box that can exit the screen
     * @return code representing wall hit example below shows how the codes manifest, 00 being no contact
     * // 11 01 21
     * // 10 00 20
     * // 12 02 22
     *
     */
    protected int wallHit(float tolerance){

        int out = 0;
        if((position.X-W/2f)<(0-W*tolerance)){
            out += 10;
        }
        if((position.Y-H/2f)<(0-H*tolerance)){
            out += 1;
        }
        if((position.X+(W/2f))>(ProgrammingProject.processing.width+W*tolerance)){
            out += 20;
        }
        if((position.Y+(H/2f))>(ProgrammingProject.processing.height+H*tolerance)){
            out += 2;
        }
        return out;
    }

    /**
     * checks if this hit box touches another
     * @param other hit box to be checked
     * @return boolean whether it hits or not
     *
     */
    protected boolean collide(HitBox other){
        return topLeft().X < other.bottomRight().X &&
                bottomRight().X > other.topLeft().X &&
                topLeft().Y < other.bottomRight().Y &&
                bottomRight().Y > other.topLeft().Y
                && IFrames == 0 && other.IFrames == 0;
    }

    /**
     * gets the top left corner of a hit box
     * @return Vector of the top left corner
     *
     */
    public Vector topLeft(){
        return new Vector(position.X - ((W/2000f)*ProgrammingProject.processing.width), position.Y - ((H/2000f)*ProgrammingProject.processing.height));
    }
    /**
     * gets the top bottom right of a hit box
     * @return Vector of the bottom right corner
     *
     */
    public Vector bottomRight(){
        return new Vector(position.X + ((W/2000f)*ProgrammingProject.processing.width), position.Y + ((H/2000f)*ProgrammingProject.processing.height));
    }

    /**
     * default method of displaying a hit box, uses angle and position
     * @param graphics graphics to draw on
     *
     */
    public void show(PGraphics graphics){
        graphics.pushMatrix();
        graphics.translate(position.X, position.Y);
        graphics.rotate(angle);
        sprite(graphics);
        graphics.popMatrix();
    }

    /**
     * default image to draw for hit box
     * @param graphics to draw on
     *
     */
    public void sprite(PGraphics graphics){
        graphics.fill(200);
        graphics.rect(-getW()/2f, -getH()/2f, getW(), getH());
        graphics.fill(Color.setTransparency(10,Color.GREEN).toInt());
        graphics.rect((-getW()/2f) + 3, (-getH()/2f) + 3, getW()-6, getH()-6);
    }
}
