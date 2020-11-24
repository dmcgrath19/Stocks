package processing.java.objects.base.properties;

/**
 *personalised 2d vector
 *
 * @author  brian dunne, delia mcgrath
 * @version 1.0
 * @since   2020-04-10
 */
public class Vector implements Comparable<Vector>, Cloneable{
    public float X, Y;
    public Vector(float x, float y){
        X=x;
        Y=y;
    }

    /**
     * adds another vector to this
     * @param other vector to add to this
     * @return this
     *
     * @author  brian dunne
     */
    public Vector add(Vector other){
        this.X += other.X;
        this.Y += other.Y;
        return this;
    }

    /**
     * adds x,y offsets to this
     * @param a adds to x
     * @param b adds to y
     * @return this
     * @author delia mcgrath
     */
    public Vector add(float a, float b){
        X += a;
        Y += b;
        return this;
    }

    /**
     * multiplies this by float
     * @param i to multiply across all values in this
     * @return this
     *
     * @author  brian dunne
     */
    public Vector mul(float i){
        X *= i;
        Y *= i;
        return this;
    }

    /**
     * multiplies this by another vector
     * @param v vector to multiply this by
     * @return this
     *
     * @author  brian dunne
     */
    public Vector mul(Vector v){
        X *= v.X;
        Y *= v.Y;
        return this;
    }

    /**
     * implementation of the clone interface
     * @return copy of this
     *
     * @author  brian dunne
     */
    @Override
    public Vector clone(){
        Vector clone = null;
        try{
            clone = (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * rotates the vector along 0,0 with an angle
     * @param angle to rotate by
     * @return this
     *
     * @author  brian dunne
     */
    Vector rotate(float angle){
        float hX= (float) (Math.cos(angle)*X-Math.sin(angle)*Y);
        Y= (float) (Math.sin(angle)*X+Math.cos(angle)*Y);
        X = hX;
        return this;
    }

    /**
     * absolute distance form 0,0
     * @return float distance
     *
     * @author  brian dunne
     */
    float abs(){
        return (float) Math.sqrt((X*X)+(Y*Y));
    }

    /**
     * absolute distance form anothe vector
     * @param ov other vector to get distance from this
     * @return float distance form ov
     *
     * @author  brian dunne
     */
    public float abs(Vector ov){
        return new Vector(this.X-ov.X,this.Y-ov.Y).abs();
    }

    /**
     * calculate the angle between tow vectors
     * @param ov other vector
     * @return float angle in rads
     *
     * @author  brian dunne
     */
    float getAngle(Vector ov){
        float op = ov.Y-Y;
        float ad = ov.X-X;
        return (float) Math.atan(ad/op);
    }

    /**
     * implementation of toString
     * @return a string representation of the vector
     *
     * @author  brian dunne
     */
    @Override
    public String toString() {
        return "Vector("+X+","+Y+")";
    }

    /**
     * x heavy implementation of compare, useful for graph
     * @param o other vector
     * @return int representing the difference
     *
     * @author  brian dunne
     */
    @Override
    public int compareTo(Vector o) {
        int out = Float.compare(X, o.X);
        if(out==0){
            out = Float.compare(Y, o.Y);
        }
        return out;
    }

    /**
     * set x,y
     * @param x new x
     * @param y new y
     * @author delia mcgrath
     */
    public void changePosition(float x, float y) {  //Little function for changing x and y.
        X=x;
        Y=y;
    }
}
