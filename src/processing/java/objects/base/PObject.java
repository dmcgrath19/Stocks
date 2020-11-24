package processing.java.objects.base;

import processing.core.PGraphics;
import processing.java.ProgrammingProject;
import processing.java.objects.base.properties.Force;
import processing.java.objects.base.properties.Vector;
import processing.core.PImage;
import processing.java.util.Color;

/**
 *2d physical object extends hatbox for image support and be subject to forces
 *
 * @version 0.0
 * @since   2020-04-10
 */
public class PObject extends HitBox {
    protected Force localForce;
    private PImage[] resources;
    private Color colour;
    private boolean dragable;

    public PObject(Vector position, int w, int h){
        this(position, w, h, 1, new Color(100,100,100,255));//default color
    }
    public PObject(Vector position, int w, int h, float weight, Color colour, PImage... pImages){
        super(position, w, h);
        localForce = new Force(new Vector(0,0), 0, weight);
        this.colour = colour;
        resources = pImages;
        for(PImage image: resources){
            image.resize(getW(),getH());
        }
        dragable = true;
    }

    public boolean isDragable() {
        return dragable;
    }

    public void setDragable(boolean dragable) {
        this.dragable = dragable;
    }

    /**
     * default method for ticking an object, used when an object has an action once per tick rather than once per draw
     * @param eForce to be applied to the object
     *
     */
    public void tick(Force eForce){
        ProgrammingProject.mouse.setOverObject(this);
        if(IFrames >0){
            IFrames--;
        }
        this.localForce.add(eForce);
        position.add(localForce.vector);
        angle += (2*Math.PI)*localForce.rpt;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    /**
     * override sprite to use images instead,
     * @param graphics to draw on
     *
     *
     */
    @Override
    public void sprite(PGraphics graphics){
        graphics.image(getResource(0),-getW()/2f,-getH()/2f);
    }

    /**
     * method of getting the objects image.
     * @param index index/state of the object
     * @return image corresponding to index
     
     */
    public PImage getResource(int index){
        try{
            PImage image = resources[index];
            if (image == null){
                throw new NullPointerException();
            }
            return  image;
        }
        catch (Exception e){
            PImage image = buildImage();
            if(e instanceof IndexOutOfBoundsException){
                PImage[] images = resources.clone();
                resources = new PImage[index+1];
                for (int i = 0; i < images.length; i++) {
                    if(images[i]!=null){
                        resources[i] = images[i];
                    }
                }
            }
            resources[index] = image;
            return image;
        }
    }

    /**
     * extracts the hit box out of the object, used in bug testing to see the hit boxes
     * @return hitbox
     *
     */
    public HitBox cloneHitBox(){
        return new HitBox(position,getW(),getH());
    }

    /**
     * public access to color
     * @return this.color
     *
     */
    public Color getColour(){
        return colour;
    }

    /**
     * method for constructing an image for a state where there isn't allredy one
     * @return image for that state
     *
     */
    private PImage buildImage(){
        PGraphics graphics = ProgrammingProject.processing.createGraphics(getW(),getH());
        graphics.beginDraw();
        drawOn(graphics);
        graphics.endDraw();
        return graphics.get();
    }

    /**
     * draws on a graphic the current state of the object, used in conjunction with build image to procedurally draw all the images needed
     * this is used because java struggles with font rendering and this helps with lag
     * @param graphics graphic to draw on
     *
     */
    public void drawOn(PGraphics graphics){
        graphics.fill(colour.toInt());
        graphics.stroke(0);
        graphics.rect(0,0,getW(),getH());
    }
    public void clearResorces(){
        resources = new PImage[0];
    }
}
