package processing.java.objects;

import processing.core.PGraphics;
import processing.java.ProgrammingProject;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Force;
import processing.java.objects.base.properties.Vector;
import processing.core.PImage;
import processing.java.util.Color;

/**
 * just a gimickey explosion effect
 * /TODO finish comenting
 * @version 0.0
 * @since   2020-04-10
 */
public class Explosion extends PObject {
//    private static final PImage[] explosion = new PImage[]{ProgrammingProject.processing.loadImage("boom_3.png"),
//            ProgrammingProject.processing.loadImage("boom_2.png"), ProgrammingProject.processing.loadImage("boom_1.png")};
    private static final PImage[] explosion = new PImage[]{ProgrammingProject.processing.loadImage("face.png")};
    private final float force;
    private final PObject[] partials;
    private final int s;
    private int duration;

    public Explosion(float aoe, int duration, PObject O, float force, int partialN){
        super(O.position.clone(), (int)aoe, (int)aoe,1, Color.BLACK);
        int root = (int)Math.sqrt(partialN);
        partialN = root*root;
        partials = new PObject[partialN];
        int pWidth = O.getW()/(partialN/root);
        int pHeight = O.getH()/(partialN/root);
        for(int i = 0; i< partials.length ; i++){
            partials[i] = new PObject(new Vector(O.topLeft().X+ ((i%root)*(pWidth))+(float)pWidth/root,O.topLeft().Y+((i/(float)root)*(pHeight))+(float)pHeight/root), pWidth,pHeight, 1,
                    getColour(),O.getResource(0).get((i%root)*(pWidth), (i/root)*(pHeight),O.getW()/(partialN/root),O.getH()/(partialN/root)));
        }
        this.force = force;
        this.duration = duration;
        s = duration;
    }

    @Override
    public void tick(Force eForce){
        super.tick(Force.NONE);
        show(ProgrammingProject.processing.getGraphics());
        for(PObject p: partials){
            if(p.position.abs(position)<W){
                float r;
                if((r=((position.X-p.position.X)-(position.Y-p.position.Y)))==0){
                    r = (position.X-p.position.X)+(position.Y-p.position.Y);
                }
                p.tick(new Force(new Vector((p.position.X-position.X)*force/2, (p.position.Y-position.Y)*force/2), (float) (0.01*((r)*force/2)),0));
            }else{
                p.tick(new Force(new Vector(0,0),0,0));
            }
            p.show(ProgrammingProject.processing.getGraphics());
        }
        this.duration--;
    }

    @Override
    public void sprite(PGraphics graphics){
//        int t = (duration/(s/explosion.length+1));
//        ProgrammingProject.processing.image(explosion[t], -W/2f, -H/2f);
        if(duration<=0){
            ProgrammingProject.processing.image(explosion[0],-W/2f, -H/2f);
        }
    }
}
