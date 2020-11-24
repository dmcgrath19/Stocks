package processing.java.objects.widget;

import processing.core.PImage;
import processing.java.ProgrammingProject;
import processing.java.objects.widget.container.Container;
import processing.java.util.Color;
import processing.java.objects.widget.button.Button;
import processing.java.objects.base.properties.Vector;
import processing.java.util.Screen;
import processing.core.PGraphics;

import java.util.ArrayList;

/**
 *
 * @author Delia McGrath
 * @version 0.0
 * @since 2020-04-10
 */
public class Customization {
    //take in the containers to be modified
    private Container graphContainer;
    private Container mainContainer;
    private Screen screen;
    //private Container buttonsOfCompanies;

    //provides access to which button of arrays to present
    //and whether display should be drawn
    private int displayCode;
    private boolean display;

    private Vector firstRow;
    private Vector secondRow;
    private Vector thirdRow;
    private Vector fourthRow;

    //creates buttons for each type of container/screen to be modified
    private ArrayList<Button> graphColors = new ArrayList<Button>();
    private ArrayList<Button> mainColors = new ArrayList<Button>();
    private ArrayList<Button> screenColors = new ArrayList<Button>();
    //private ArrayList<Button> companyColors = new ArrayList<Button>();

    private ArrayList<Button> displayColors = new ArrayList<Button>();

    private PImage exitButton;

    private Color tabColor;
    private Button settingButton;
    private Color textColor;

    private int buttonAlignX;
    private int buttonHeight;
    private int buttonWidth;

    /**
     * takes in the Containers/screens and creates an array of buttons for each that
     * each hold different colors that the user will be able to choose which they would
     * like to change to
     * @author delia mcgrath
     */

    public Customization(Container dataContainer, Container mainContainer, /*RadioButtons companyButtons*/ Screen screen) {

        // sets the column index to correspond to each color change
        this.graphContainer = dataContainer;
        this.mainContainer = mainContainer;
       // this.buttonsOfCompanies = buttonsOfCompanies;
        this.screen = screen;

        displayCode = 0;
        display = false;

        tabColor = Color.BLUE;
        textColor = Color.WHITE;

        exitButton = ProgrammingProject.processing.loadImage("smallerexit.png");

        settingButton = Button.SETTING_BUTTON;
        Vector topLeft = settingButton.topLeft();

        int yOffset = 25;
        firstRow = new Vector(topLeft.X + 45, topLeft.Y + yOffset);
        yOffset = 50;
        secondRow = new Vector(topLeft.X + 45, firstRow.Y + yOffset);
        thirdRow = new Vector(topLeft.X + 35, secondRow.Y + yOffset);
        //fourthRow = new Vector(topLeft.X + 65, thirdRow.Y + yOffset);

        buttonAlignX = 170;
        buttonWidth = 80;
        buttonHeight = 16;


        graphColors.add(new Button(new Vector(buttonAlignX, firstRow.Y), buttonWidth, buttonHeight, "Gray", "Gray", 0, Color.GRAY));
        graphColors.add(new Button(new Vector(buttonAlignX, firstRow.Y+buttonHeight), buttonWidth, buttonHeight, "Red", "Red", 1, Color.RED));
        graphColors.add(new Button(new Vector(buttonAlignX, firstRow.Y+buttonHeight*2), buttonWidth, buttonHeight, "White", "White", 2, Color.WHITE));
        graphColors.add(new Button(new Vector(buttonAlignX, firstRow.Y-buttonHeight), buttonWidth, buttonHeight, "Cyan", "Cyan", 3, Color.CYAN));

        mainColors.add(new Button(new Vector(buttonAlignX, secondRow.Y), buttonWidth, buttonHeight, "Red", "Red", 0, Color.RED));
        mainColors.add(new Button(new Vector(buttonAlignX, secondRow.Y-buttonHeight), buttonWidth, buttonHeight, "Black", "Black", 1, Color.BLACK));
        mainColors.add(new Button(new Vector(buttonAlignX, secondRow.Y+buttonHeight), buttonWidth, buttonHeight, "White", "White", 2, Color.WHITE));


        screenColors.add(new Button(new Vector(buttonAlignX, thirdRow.Y), buttonWidth, buttonHeight, "Gray", "Gray", 0, Color.GRAY));
        screenColors.add(new Button(new Vector(buttonAlignX, thirdRow.Y - buttonHeight), buttonWidth, buttonHeight, "Black", "Black", 1, Color.BLACK));
        screenColors.add(new Button(new Vector(buttonAlignX, thirdRow.Y - 2*buttonHeight), buttonWidth, buttonHeight, "White", "White", 2, Color.WHITE));
        screenColors.add(new Button(new Vector(buttonAlignX, thirdRow.Y +buttonHeight), buttonWidth, buttonHeight, "Red", "Red", 3, Color.RED));


        //companyColors.add(new Button(new Vector(buttonAlignX, fourthRow.Y), buttonWidth, buttonHeight, "Green", "Green", 0, Color.GREEN));
        // companyColors.add(new Button(new Vector(buttonAlignX, fourthRow.Y-2*buttonHeight), buttonWidth, buttonHeight, "Black", "Black", 1, Color.BLACK));
        // companyColors.add(new Button(new Vector(buttonAlignX, fourthRow.Y-buttonHeight), buttonWidth, buttonHeight, "White", "White", 2, Color.WHITE));

        displayColors.add(new Button(new Vector(buttonAlignX, firstRow.Y), buttonWidth, buttonHeight, "dataColor", "Gray", 0, Color.GRAY));
        displayColors.add(new Button(new Vector(buttonAlignX, secondRow.Y), buttonWidth, buttonHeight, "mainColor", "Red", 0, Color.RED));
        displayColors.add(new Button(new Vector(buttonAlignX, thirdRow.Y), buttonWidth, buttonHeight, "screenColor", "Gray", 0, Color.GRAY));
        //displayColors.add(new Button(new Vector(buttonAlignX, fourthRow.Y), buttonWidth, buttonHeight, "companyColor", "Green", 0, Color.GREEN));


    }

    /**
     * draws the text on the screen in desired location
     * @param label is the text to be drawn
     * @param position is the vector position where it should be drawn
     * @author delia mcgrath
     */
    public void drawText(PGraphics graphics, String label, Vector position) {
        graphics.textSize(12);
        graphics.fill(textColor.toInt());
        graphics.text(label, position.X, position.Y);
    }
    /**
     * allows for a simple way to get the proper ArrayList of buttons that
     * should be displayed -- assumes corresponding number for list
     * @param type takes the numeric value of the desired ArrayList
     * @return ArrayList of correct buttons
     * @author delia mcgrath
     */
    public ArrayList<Button> getCurrentButtons(int type){

        if(type == 1)
            return graphColors;
        if(type == 2)
            return mainColors;
        if(type == 3)
            return screenColors;
        //if(type == 4)
         //   return buttonsOfCompanies;

        return displayColors;
    }
    /**
     * draws the interactive customization display
     * makes calls to surrounding methods to make changes
     * to display and retrieve correct ArrayList
     * @author delia mcgrath
     */
    public void drawDisplay(PGraphics graphics) {
        graphics.fill(new Color(100, 100, 100).toInt());
        graphics.rect(settingButton.topLeft().X -5, settingButton.topLeft().Y -5, 240, 160);
        drawText(graphics, "Graph Container", firstRow);
        drawText(graphics, "Main Container", secondRow);
        drawText(graphics, "Background", thirdRow);

        ArrayList<Button> buttons = getCurrentButtons(displayCode);
        if(displayCode == 0) {
            int index = 0;
            for (Button button : buttons) {
                button.draw();
                if (button.isOn()) {
                    displayCode = index + 1;
                    button.turnOffButton();
                }
                index++;
            }
        }
        else{
            for (Button button : buttons) {
                button.draw();
                if (button.isOn()) {
                    if(displayCode == 1)
                        graphContainer.setColour(button.getColour());
                    if(displayCode == 2)
                        mainContainer.setColour(button.getColour());
                    if(displayCode == 3)
                        screen.setScreenColor(button.getColour());
                    //if(displayCode == 4)
                     //   buttonsOfCompanies.setColour(button.getColour());

                    changeButtons(button);
                    button.turnOffButton();
                    displayCode = 0;
                }
            }
        }
         graphics.image(exitButton, 5, 5);
    }
    /**
     * deletes the old button in the array and replaces it with a new
     * button so that the console can accurately show the colors chosen
     * for the display
     * @param newButton used to copy the label and color chosen by the user
     * @author delia mcgrath
     */
    public void changeButtons(Button newButton){
        displayColors.remove(displayCode-1);
        if(displayCode == 1)
            displayColors.add(0, new Button(new Vector(buttonAlignX, firstRow.Y), buttonWidth, buttonHeight, "dataColor", newButton.getText(), 0, newButton.getColour()));
        if(displayCode == 2)
            displayColors.add(1,new Button(new Vector(buttonAlignX, secondRow.Y), buttonWidth, buttonHeight, "mainColor", newButton.getText(), 0, newButton.getColour()));
        if(displayCode == 3)
            displayColors.add(2, new Button(new Vector(buttonAlignX, thirdRow.Y), buttonWidth, buttonHeight, "screenColor", newButton.getText(), 0, newButton.getColour()));
        //if(displayCode == 4)
        //displayColors.add(3, new Button(new Vector(buttonAlignX, fourthRow.Y), buttonWidth, buttonHeight, "companyColor", newButton.getText(), 0, newButton.getColour()));
    }
    /**
     * returns the value of display to give
     *public access to private variables
     * @return whether display should be shown
     * @author delia mcgrath
     */
    public boolean display () {
        return display;
    }

    /**
     *gives easy access to turn the display on/off
     *from the setting button
     * @author delia mcgrath
     */
    public void setDisplay (boolean isOn){
        display = isOn;
        if(!isOn)
            displayCode = 0;
    }

    /**
     * returns whether the background color is
     * to know whether the color of black images
     * should be inverted
     * @author delia mcgrath
     */
    public boolean invert(){
        return Color.BLACK == displayColors.get(2).getColour();
    }

}
