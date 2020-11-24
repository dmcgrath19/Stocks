package processing.java.objects.base.properties;

/**
 * component to physical objects
 *
 * @version 1.0
 * @since   2020-04-10
 */
public class Force implements Cloneable{
    public static final Force NONE = new Force(new Vector(0,0),0,0);
    public Vector vector;
    public float rpt, weight;
    public Force(Vector vector, float rpt, float weight){
        this.vector = vector;
        this.rpt = rpt;
        this.weight = Math.max(weight,1);
    }

    /**
     * adds another force to this one
     * @param other force to add to this
     *
     */
    public void add(Force other){
        this.vector.add(other.vector.mul(other.weight/weight));
        this.rpt += other.rpt*(other.weight/weight);
    }

    /**
     * implementation of the clone interface
     * @return copy of this
     *
     */
    @Override
    public Force clone(){
        Force clone = null;
        try{
            clone = (Force) super.clone();
            clone.vector = vector.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
