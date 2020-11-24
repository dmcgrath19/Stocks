package processing.java.util;

import processing.java.ProgrammingProject;
import processing.java.objects.widget.Customization;
import processing.java.objects.widget.button.Button;
import processing.java.objects.widget.Widget;

import java.util.ArrayList;

import static processing.java.objects.widget.button.Button.SETTING_BUTTON;

/**
 * Screen: Contains ArrayList of widgets, different screens can contain/display different ArrayLists.
 * @version 0.0
 * @since   2020-04-10
 */
public class Screen {
    public ArrayList <Widget> widgetList;
    private Color backgroundColor;
    private Customization customization;

    public Screen(int screenCode, Color backgroundColor){
        widgetList = new ArrayList<>();
        widgetList.add(Button.CLOSE_BUTTON);
        customization = null;
        this.backgroundColor = backgroundColor;
    }

    /**
     * puts background first and then draws everything on top of it
     *so it can be seen
     */

    public void draw() {
        ProgrammingProject.processing.background(backgroundColor.toInt());
        for (Widget widget : widgetList) {
            widget.draw();
        }
        if(customization != null)
            SETTING_BUTTON.drawCustomization(customization);

    }
    /**
     * be able to add widgets onto the main screen
     * so that the code can run smoothly and be more readable
     *
     */
    public void addWidget(Widget newWidget)
    {
        widgetList.add(newWidget);
    }

    /**
     * allow to modify the private variable color
     *
     */
    public void setScreenColor(Color color){
        backgroundColor = color;
    }

    /**
     * be able to put a customization onto any screen
     *
     */
    public void addCustomization(Customization customization){this.customization = customization;}

}

