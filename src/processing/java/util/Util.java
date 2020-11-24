package processing.java.util;

import processing.core.PImage;

/**
 * functions
 *
 * @author  brian dunne
 * @version 1.0
 * @since   2020-04-10
 */
public class Util {
    public static final String R_F = "([+-]?\\d+.?\\d*(?:e-\\d+)?)";

    /**
     * draws an image to make passed image darker
     * @param image image reference
     * @return shadow of original image with alpha 0x4F
     *
     * @author brian dunne
     */
    public static PImage getfader(PImage image){
        PImage shadow = image.copy();
        for(int w=0; w< shadow.width;w++){
            for(int h=0; h<shadow.height;h++){
                if(image.get(w,h)!=0){
                    shadow.set(w,h,0x4F0A0A0A);
                }
            }
        }
        return shadow;
    }

    /**
     * draws a thick line around the border outline of the image
     * @param image reference image
     * @return traced outline of the image
     *
     * @author brian dunne
     */
    public static PImage getBorder(PImage image) {
        PImage border = image.copy();
        for(int w=0; w< border.width;w++){
            for(int h=0; h<border.height;h++){
                if(borderPixel(w,h,10*image.width/100,image)){
                    border.set(w,h,0xFFAAAAAA);
                }
                else{
                    border.set(w,h,0);
                }
            }
        }
        return border;
    }

    /**
     * finds out if this pixel is at the edge of the image
     * @param x x position of pixel
     * @param y y position of pixel
     * @param borderSize how far away a transparent pixel can be for it to be considered a border pixel
     * @param image reference image
     * @return boolean if the picel is close to a transparent t pixel or not
     *
     * @author brian dunne
     */
    private static boolean borderPixel(int x, int y, int borderSize, PImage image){
        if(image.get(x,y)==0){
            return false;
        }
        int r = borderSize/2;
        for(int layer = 0; layer< r; layer++){
            if((x-layer<0||x+layer>image.width)||(y-layer<0||y+layer>image.height)){
                return true;
            }
            for(int i=1; i<(layer*2);i++){
                if(((Math.sqrt(Math.pow((-layer)+i,2)+Math.pow(-layer,2))<=r) && (image.get((x-layer)+i,y-layer)==0))
                        ||((Math.sqrt(Math.pow(layer,2)+Math.pow(-layer+i,2))<=r) && (image.get(x+layer,(y-layer)+i)==0))
                        ||((Math.sqrt(Math.pow((layer)-i,2)+Math.pow(layer,2))<=r) && (image.get((x+layer)-i,y+layer)==0))
                        ||((Math.sqrt(Math.pow((-layer),2)+Math.pow(layer-i,2))<=r) && (image.get(x-layer,(y+layer)-i)==0))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * shifts elements across and fills the caps
     * @param array array to role
     * @param roll amount to role
     *
     * @author brian dunne
     */
    public static void rollArray(Object[] array, int roll) {
        Object prevoius = null;
        for (int i = 1; i <= array.length; i++) {
            int i1 = rollInt(0, (i * roll) + (roll > 0 ? -1 : 0), 0, array.length);
            Object next = array[i1];
            if (prevoius == null) {
                prevoius = array[rollInt(0, i1 - roll, 0, array.length)];
            }
            array[i1] = prevoius;
            prevoius = next;
        }
    }
    /**
     * moves an int i a certain amount within a range, if the edge is reached i is set to the other side
     * @param i int being changed
     * @param amount amount to change int
     * @param lRange lower limit of range
     * @param uRange upper limit of range
     * @return new i after translation
     *
     * @author brian dunne
     */
    public static int rollInt(int i, int amount, int lRange, int uRange){
        if(lRange>uRange){
            throw  new IllegalArgumentException("range ("+lRange+","+uRange+"), is not a valid range");
        }
        if(lRange==uRange)return lRange;
        int offset = lRange;
        uRange = uRange-lRange;
        lRange = 0;
        i+=amount;
        i-=offset;
        while (!(i>=lRange&&i<uRange)) {
            if (i >= uRange) {
                i = i - uRange;
            }
            if (i < lRange) {
                i = uRange + i;
            }
        }
        return i+offset;
    }
}
