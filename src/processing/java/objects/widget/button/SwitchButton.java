package processing.java.objects.widget.button;

import processing.core.PImage;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.Widget;
import processing.java.thread.BuildDataSet;
import processing.java.util.Color;

/**
 * //TODO author comment on this class
 *
 * @author
 * @version 0.0
 * @since   2020-04-10
 */
public class SwitchButton extends Button {
    private boolean isOn;
    public SwitchButton(Vector position, int w, int h, String name, String label, int code, Color color)
    {
        super(position, w, h, name, label, code, color);
        isOn = false;
    }

    public SwitchButton(Vector position, int w, int h, String name, String label, int code, Color color, PImage image) {
        super(position, w, h, name, label, code, color, image);
        isOn = true;
    }

    @Override
    public void buttonEventHandler() {

    }

    public void setState(int state)
    {
        if(state == Widget.CLICKED){
            buttonEventHandler();
            if (isOn)
            {
                super.setState(0);
                isOn = false;
            } else {
                super.setState(2);
                isOn = true;
            }
        }
        else if (state == Widget.MOUSE_OVER && !isOn)
        {
            //System.out.println("mouse over but not on");
            super.setState(1);
        }
        else if (state != Widget.MOUSE_OVER && !isOn)
        {
            super.setState(0);
        }
    }


    public boolean isOn()
    {
        return isOn;
    }
}
