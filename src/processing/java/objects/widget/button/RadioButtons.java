package processing.java.objects.widget.button;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.InternalError;
import processing.java.ProgrammingProject;
import processing.java.objects.base.PObject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.container.Container;
import processing.java.thread.data.stock.DataSet;
import processing.java.util.Color;

import java.util.ArrayList;

/**
 *
 *
 * @author
 * @version 0.0
 * @since   2020-04-10
 */
public class RadioButtons extends Container {

    private boolean clickedButtons[];
    private int amountClicked = 0;
    private int maxAllowedClicked = 4;

    public RadioButtons(Vector position, int w, int h, int code, Color colour, String name, ArrayList<Button> buttons) {
        super(position, w, h, code, colour, name);
        for (PObject object : buttons) {
            this.addRow();
            this.add(object);
        }
    }

    public RadioButtons(Vector position, int w, int h, int code, Color colour, String name) {
        super(position, w, h, code, colour, name);
        if ((code>>26) == 1)
        {
            maxAllowedClicked = 1;
            for (int i = 0; i < DataSet.companyNames.length; i++)
            {
                this.addRow();
                int sbCode = (Button.RETURN_COMPANY << 26)|((code&0x00ffffff)<<16)|i;
                this.add(new Button(new Vector(0, 0), ProgrammingProject.processing.width / 5 - 40, 25, DataSet.companyNames[i], DataSet.companyNames[i], sbCode, Color.GREEN));
            }
        }
    }

    /**
     * check for event
     * //TODO can author please comment this
     * @author
     */
//    public void checkForEvent(){
//        ProgrammingProject.mouse.setOverObject(this);
//        if (clickedButtons == null || clickedButtons.length == 0)
//        {
//            clickedButtons = new boolean[objects.length];
//            for (int i = 0; i < clickedButtons.length; i++)
//            {
//                clickedButtons[i] = false;
//            }
//        } else {
//            for (int i = 0; i < objects.length; i++) {
//                SwitchButton button = (SwitchButton) objects[i];
//                if (clickedButtons[i] && !button.isOn()) {
//                    clickedButtons[i] = false;
//                    amountClicked--;
//                } else if (!clickedButtons[i] && button.isOn()) {
//                    if (amountClicked < maxAllowedClicked) {
//                        clickedButtons[i] = true;
//                        amountClicked++;
//                    } else {
//                        button.turnOffButton();
//                    }
//                }
//            }
//        }
//
//
//    }

    public int currentEventCode(){
        return 0;
    }

}
