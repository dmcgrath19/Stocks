package processing.java.objects.widget;

/**
 * basic interface for classes that implement scrolling.
 *
 * @author brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public interface IScrollable {

    void scrollY(int dir);
    void scrollX(int dir);
    void scrollZ(int dir);
    void setPercentY(float percentY);// no effective use for these methods yet so
    void setPercentX(float percentX);
    void setPercentZ(float percentZ);
//    float getYViewPercentage();
//    float getAViewPercentage();
    //zoom?
}
