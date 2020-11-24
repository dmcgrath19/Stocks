package processing.java.thread.data.stock;

import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.Widget;
import processing.java.objects.widget.button.Button;
import processing.java.util.Color;
import processing.java.util.DateTime;
import processing.java.util.Util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * entry represents an entry within the data set
 *
 * @author  brian dunne
 * @version 1.0
 * @since   2020-04-10
 */
public class Entry implements Comparable<Entry>, Cloneable{
    private static final String[] COMPONENTS = new String[]{"Ticker","Open Price","Close Price","A. Close","Low","High","Volume","Date"};
    public static final int MAX = COMPONENTS.length;
    public static final int MIN = 1;
    public static String getComponent(int i){
        return COMPONENTS[i];
    }

    /**
     * array of widgets representing an entries component titles, gives action behaviour to graphable components
     * @param colors group of colors to be used on the widgets, defaults to empty
     * @return array of widgets
     *
     * @author  brian dunne
     */
    static Widget[] getHeaderWidgets(Color... colors){
        if(colors.length==0){
            colors = new Color[]{Color.EMPTY};
        }
        Widget[] out = new Widget[COMPONENTS.length];
        out[0] = new Widget(new Vector(-100,-100),100, 20, "ticker", Color.add(colors[0], -50));
        for (int i = 1; i < out.length; i++) {
            String name = COMPONENTS[i]+"_button";
            String label = COMPONENTS[i];
            out[i] = new Button(new Vector(-100,-100),100, 20, name, label, Button.constructCode(Button.SCROLL_SET_Z,i), colors[Util.rollInt(0,i,0,colors.length)]);
        }
        return out;
    }

    public final String ticker, exchange, name, sector, industry;
    public final float open_price, close_price, adjusted_close, low, high;
    public final long volume;
    public final DateTime dateTime;

    /**
     * Entry constructor, throws an exception if the string doesn't match
     * @param entry builds an entry from a string
     *
     * @author  brian dunne
     */
    public Entry(String entry){
        Matcher dataMatcher = Pattern.compile( "(?:([A-Z]+),)(?:"+ Util.R_F+",)(?:"+ Util.R_F+",)(?:"+ Util.R_F+",)(?:"+ Util.R_F+",)(?:"+ Util.R_F+",)(?:([+-]?\\d+),)" +
                "(?:((?:\\d{4})[-/](?:\\d{1,2})[-/](?:\\d{1,2}))|((?:\\d{1,2})[-/](?:\\d{1,2})[-/](?:\\d{4}))).*").matcher(entry);
        //Matcher detailMatcher = Pattern.compile("(?:([^,]+),)(?:([^,]+),)(?:([^,]+),)(?:([^,]+),)(?:([^,]+),)").matcher(entry);
        if(dataMatcher.matches()){
            ticker = dataMatcher.group(1);
            open_price = Float.parseFloat(dataMatcher.group(2));
            close_price = Float.parseFloat(dataMatcher.group(3));
            adjusted_close = Float.parseFloat(dataMatcher.group(4));
            low = Float.parseFloat(dataMatcher.group(5));
            high = Float.parseFloat(dataMatcher.group(6));
            volume = Long.parseLong(dataMatcher.group(7));
            dateTime = new DateTime((dataMatcher.group(8).equals("")?dataMatcher.group(9):dataMatcher.group(8)));
            exchange = name = sector = industry = null;
        }
        else { //if(detailMatcher.matches()){       //If the entry doesn't match the dataMatcher, it is from the stocks.csv file, and contains the below details - E McDonald
            Scanner detailScanner = new Scanner(entry); //I used Scanners here because matchers scare and confuse me - E McDonald
            detailScanner.useDelimiter(",");
            ticker = detailScanner.next();
            exchange = detailScanner.next();
            String tempName = detailScanner.next();
            if(tempName.startsWith("\""))
            {
                tempName += "," + detailScanner.next();
                tempName = tempName.substring(1, tempName.length()-1);
            }
            name = tempName;
            sector = detailScanner.next();
            industry = detailScanner.next();
            open_price = close_price = adjusted_close = low = high = 0;
            volume = 0;
            dateTime = null;
        }
        //else {
        //    throw new IllegalArgumentException("unable to extract entries information from <"+entry+">");
        //}
    }

    /**
     * gets an array of widgets corresponding to the components
     * @return new array of widgets
     * @param colors group of colors to apply to the widgets, defaults to empty
     *
     * @author  brian dunne
     */
    Widget[] getWidgets(Color... colors){
        if(dateTime!=null) {
            if (colors.length == 0) {
                colors = new Color[]{Color.EMPTY};
            }
            Widget[] out = new Widget[COMPONENTS.length];
            for (int i = 0; i < out.length; i++) {
                out[i] = new Widget(new Vector(-100, -100), 100, 20, i == 0 ? ticker : i == out.length - 1 ? dateTime.yyyyMmDd() : get(getComponent(i)) + "", colors[Util.rollInt(0, i, 0, colors.length)]);
            }
            return out;
        }
        return null;
    }

    /**
     * greats a string matching the structure provided to create this object
     * @return string formatted with delimiter ',\n'
     *
     * @author  brian dunne
     */
    @Override
    public String toString() {
        return ticker+","+open_price+","+close_price+","+adjusted_close+","+low+","+high+","+volume+","+dateTime.yyyyMmDd();
    }

    /**
     * name -> date heavy comparison, compares one entry to another, starting with name then it's date
     * @param o other entry
     * @return numeric value that describes the difference between this and o
     *
     * @author  brian dunne
     */
    @Override
    public int compareTo(Entry o) {
        int out = ticker.compareTo(o.ticker);
        if(out == 0) {
            out = dateTime.compareTo(o.dateTime);
        }
        return out;
    }

    /**
     * create an identical copy od the entry
     * @return new entry
     *
     * @author  brian dunne
     */
    @Override
    public Entry clone(){
        Entry clone = null;
        try{
            clone = (Entry) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     *  gets a numeric value for a component
     * @param component string name of component
     * @return float representing the magnitude of the component
     *
     * @author  brian dunne
     */
    public float get(String component) {
        if(dateTime!=null) {
            switch (component.toLowerCase()) {
                case ("ticker"):
                    return ticker.hashCode();
                case ("open price"):
                    return open_price;
                case ("close price"):
                    return close_price;
                case ("a. close"):
                    return adjusted_close;
                case ("low"):
                    return low;
                case ("high"):
                    return high;
                case ("volume"):
                    return volume;
                case ("date"):
                    return dateTime.daysBetween(new DateTime(0, 1, 1, 1)); //days since start of common 1/1/0001
                default:
                    return 0;
            }
        }
        else
            return -1;
    }
}
