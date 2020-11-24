package processing.java.thread.data.stock;


import processing.java.ProgrammingProject;
import processing.java.thread.BuildDataSet;
import processing.java.thread.data.Group;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.container.Container;
import processing.java.objects.widget.Widget;
import processing.java.objects.widget.button.Button;
import processing.java.util.Color;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * data set is local structured representation of the data set
 *
 * @author  brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class DataSet extends Thread{
    private static final int WAIT = 0;
    private static final int WRITE = 1;
    public static String companyNames[];

    private Entry[] entries;
    private final List<Group> groups;
    public final String name;

    private boolean writing;
    private File file;
    private int action = 0;

    /**
     * constructs a data set to a name and a entries
     * @param name name of data set
     * @param entries entries to store in data set
     *
     * @author  brian dunne
     */
    public DataSet(String name, Entry[] entries){//make DataSet be a thread that reads and writes to save in ram, content shouldn't be needed that often,
        this.name = name;
        this.entries = entries;
        groups = new ArrayList<>();
        int start = 0;
        name = entries[0].ticker;
        for(int i=0; i< getSize(); i++){//checks for groups
            if(!name.equals(this.entries[i].ticker)){
                groups.add(new Group(name, start, i-1));
                name = this.entries[i].ticker;
                start = i;
            }
        }
    }


    /**
     * experimental writing to .json file
     */
    @Override
    public void run() {
        //TODO implement
    }
    private void writeEntry(int entry, File file)throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName(), true));
        writer.append(' ');
        writer.append("test:");
        writer.append(String.valueOf(entry)).append("\n");
        writer.close();
    }


    /**
     * retrieves a specific entry
     * @param index of entry
     * @return entry@ index
     *
     * @author  brian dunne
     */
    public Entry get(int index) {
        if(entries!=null) {
            return entries[index].clone();
        }
        return null;
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line;
//            int entry = 0;
//            while ((line = reader.readLine()) != null&&entry!=index) {
//                Matcher matcher = Pattern.compile(".*\"id\": \"(\\d+)\".*" ).matcher(line);
//                if(matcher.matches()){
//                    entry = Integer.parseInt(matcher.group(1));
//                }
//            }
//          return new Entry(line);
//        } catch (IOException e) {
//           return null;
//        }
    }


    /**
     * extracts a subset between two limits
     * @param lower lower limit
     * @param higher higher limit
     * @return new dataSet containing entries between
     *
     * @author  brian dunne
     */
    public DataSet subset(int lower, int higher){
        if(lower<0||higher>getSize()||lower>higher){
            throw new IllegalArgumentException("Subset Range("+lower+"->"+higher+"), is not valid int set("+0+"->"+getSize()+")");
        }
        Entry[] data = new Entry[higher-lower];
        String name = get(lower).ticker;
        Group group = new Group(name, lower, higher);
        for(int i = lower; i<higher; i++){
            data[i-lower] = get(i);
        }
        return new DataSet(groups.contains(group)? name: this.name+"("+lower+"->"+higher+")", data);
    }

    /**
     * gets the subset corresponding with  a group
     * @param group group to look for
     * @return new dataSet corresponding with the group
     *
     * @author  brian dunne
     */
    public DataSet getGroup(Group group){
        return subset(group.start, group.end);
    }
    /**
     * get the subset of the group an index
     * @param index index of group
     * @return new data corresponding with the group index
     *
     * @author  brian dunne
     */
    public DataSet getGroup(int index){
        Group group = groups.get(index);
        return getGroup(group);
    }

    /**
     * gets the number of groups in the data set
     * @return number of groups
     *
     * @author  brian dunne
     */
    public int nGroups(){
        return groups.size();
    }

    @Override
    public String toString() {
        return name+":[["+getSize()+"]:["+ groups.size()+"]"+ groups +"]";
    }

    /**
     * gets length of entries
     * @return int entries.length
     *
     * @author  brian dunne
     */
    public int getSize() {
        return entries.length;
    }

    public Vector[] getPoints(String xGraph, String yGraph) {
        Vector[] out = new Vector[getSize()];
        for (int i = 0; i < out.length; i++) {
            Entry entry = get(i);
            out[i] = new Vector(entry.get(xGraph), entry.get(yGraph));
        }
        return out;
    }

    /**
     * populates a container with the content of the data set
     * @param container container to populate
     * @param depth nth sub container
     *
     * @author brian dunne
     */
    private void populateContainer(Container container, int depth){
        if(groups.size()<=1){
            container.addCols(9);
            Color c = container.getColour();
            container.add(new Widget(new Vector(-100,-100),20, 20, "", Color.EMPTY));
            for (Widget widget: Entry.getHeaderWidgets(c)){
                container.add(widget);
            }
            int i =0;
            container.addRows(entries.length);
            for (Entry entry: entries) {
                i++;
                container.add(new Widget(new Vector(-100,-100),20, 20, String.format("%1$"+((getSize()+"").length()+1)+ "s", (i)), Color.EMPTY));
                for (Widget widget: entry.getWidgets()){
                    container.add(widget);
                }
            }
        }
        else {
            container.addRows(nGroups());
            for (int i = 0; i < nGroups(); i++) {
                Container container1 = new Container(new Vector(0,0),container.getW()-32, container.getH()-64,container.getCode()+i*(int)(Math.pow(10,depth)), Color.add(container.getColour(), -75), groups.get(i).ticker);
                getGroup(i).populateContainer(container1, depth+1);
                container1 = Container.compressH(container1);
                container.add(container1);
            }
        }
    }

    /**
     * public access of populate container first clears it then
     * @param container container to populate
     *
     * @author brian dunne
     */
    public void populateContainer(Container container){
        container.clear();
        populateContainer(container, 1);
    }

    //Getters for stocks.csv details -E McDonald
    public String getExchange(String ticker)
    {
        if(entries[0].dateTime == null) {
            return(entries[BuildDataSet.tickerList.indexOf(ticker)].exchange);
        }
        return "";
    }

    public String getCompanyName(String ticker)
    {
        if(entries[0].dateTime == null) {
            int index;
            if((index=BuildDataSet.tickerList.indexOf(ticker)) >= 0) {
                return (entries[index].name);
            }
        }
        return "";
    }

    public String getSector(String ticker)
    {
        if(entries[0].dateTime == null) {
            return(entries[BuildDataSet.tickerList.indexOf(ticker)].sector);
        }
        return "";
    }

    public String getIndustry(String ticker)
    {
        if(entries[0].dateTime == null) {
            return(entries[BuildDataSet.tickerList.indexOf(ticker)].industry);
        }
        return "";
    }

    public String getTicker(int index)
    {
        return entries[index].ticker;
    }
}
