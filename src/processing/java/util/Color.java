package processing.java.util;

/**
 * personalised representation of color, personalised methods
 *
 * @version 0.0
 * @since   2020-04-10
 */
public class Color{
    public static final Color BLACK = new Color(0,0,0);
    public static final Color WHITE = new Color(255,255,255);
    public static final Color GRAY = new Color(127,127,127);
    public static final Color RED = new Color(255,0,0,255);
    public static final Color GREEN = new Color(0,255,0);
    public static final Color BLUE = new Color(0,0,255);
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color EMPTY = new Color(0,0,0,0);

    /**
     * cycles through reg->green-> blue relative to an angle in radians
     * @param  angle in radians to cycle at
     * @return new color
     *
     * @author brian dunne
     */
    public static Color colorCircleAt(float angle){
        int i = 0;
        int [] color = Color.RED.get();
        for (int j = 0; j < ((angle/(Math.PI*2))*(255*3)); j++) {
            color[i]--;
            color[Util.rollInt(i,1,0,3)]++;
            if(color[Util.rollInt(i,1,0,3)]==255){
                i=Util.rollInt(i,1,0,3);
            }
        }
        return new Color(color[0],color[1],color[2]);
    }

    /**
     * sets alpha of color
     * @param percentAlpha percentage opacity
     * @param color color to modify
     * @return new color with same rgb and new alpha
     *
     */
    public static Color setTransparency(double percentAlpha, Color color){
        return new Color(color.red, color.green, color.blue, (color.alpha*255)*percentAlpha/100);
    }

    /**
     * adds a constant to a color
     * @param aug color to add to
     * @param add constant to add to color
     * @return new color with brightness offset by the constant
     *
     */
    public static Color add(Color aug, int add){
        return new Color(Math.min(Math.max(aug.red+add,0),255), Math.min(Math.max(aug.green+add,0),255), Math.min(Math.max(aug.blue+add,0),255));
    }

    /**
     * subtracts the color from white
     * @param aug color to subtract from white
     * @return new different color
     *
     */
    public static Color flip(Color aug){
        return new Color(Math.max(255-aug.red,0),Math.max(255-aug.green,0),Math.max(255-aug.blue,0));
    }

    /**
     * checks if the colors are similar in difference of components
     * @param colorA compare one color
     * @param colorB to the other
     * @return boolean whether this method finds the colors to be similar
     *
     */
    public static boolean similarColors(Color colorA, Color colorB) {
        int difARG = (int) Math.abs(colorA.red-colorA.green);
        int difBRG = (int) Math.abs(colorB.red-colorB.green);
        int difARB = (int) Math.abs(colorA.red-colorA.blue);
        int difBRB = (int) Math.abs(colorB.red-colorB.blue);
        int difAGB = (int) Math.abs(colorA.green-colorA.blue);
        int difBGB = (int) Math.abs(colorB.green-colorB.blue);
        int difAA = (int) Math.abs(colorA.alpha-colorB.alpha);
        int difRR = (int) Math.abs(colorA.red-colorB.red);
        int difGG = (int) Math.abs(colorA.green-colorB.green);
        int difBB = (int) Math.abs(colorA.blue-colorB.blue);

        int tolerance = 30;
        boolean test0 = difAA<=tolerance/2;
        boolean test1 = Math.abs(difARG-difBRG) <= tolerance;
        boolean test2 = Math.abs(difARB-difBRB) <= tolerance;
        boolean test3 = Math.abs(difAGB-difBGB) <= tolerance;
        boolean test4 = difRR <= tolerance*2;
        boolean test5 = difGG <= tolerance*2;
        boolean test6 = difBB <= tolerance*2;
        return test0&&test1&&test2&&test3&&test4&&test5&&test6;
    }





    private final double red, green, blue, alpha;

    public Color(double red, double green, double blue, double alpha) {
        this.red = red%256;
        this.green = green%256;
        this.blue = blue%256;
        this.alpha = (alpha%256)/255;
    }
    public Color(int colorAsInt){this((colorAsInt >> 16) & 0xff, (colorAsInt >> 8) & 0xff , (colorAsInt) & 0xff, (colorAsInt >> 24) & 0xff);}
    public Color(String hex) { this(Integer.parseInt(hex, 16));}
    public Color(double red, double green, double blue){ this(red,green,blue,255); }

    /**
     * converts the color to an int
     * @return int ARGB representation of color
     *
     */
    public int toInt() {
        int color = 0;
        color += (int) (this.alpha * 255) <<24;
        color += (int) (this.red ) <<16;
        color += (int) (this.green ) <<8;
        color += (int) (this.blue);
        return color;
    }

    /**
     * to string
     * @return string representation pf color
     *
     */
    @Override
    public String toString(){
        return "a:"+alpha+",r:"+red+",g:"+green+",b:"+blue;
    }

    /**
     * return color as an int array
     * @return integer array{red,green,blue}
     *
     */
    public int[] get(){
        return new int[]{(int) red, (int) green, (int) blue};
    }
}


