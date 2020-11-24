package processing.java.thread;

import processing.java.ProgrammingProject;
import processing.java.objects.base.properties.Vector;
import processing.java.objects.widget.button.SwitchButton;
import processing.java.thread.data.stock.DataSet;
import processing.java.thread.data.stock.Entry;
import processing.java.util.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *reads and constructs a more structured representation of a DataSet in the background.
 *
 * @author  brian dunne
 * @version 0.0
 * @since   2020-04-10
 */
public class BuildDataSet extends Thread{
    public static final String ONE_K = "daily_prices1k.csv";
    public static final String TEN_K = "daily_prices10k.csv";
    public static final String HUNDRED_K = "daily_prices100k.csv";
    public static final String TWO_GB = "daily_prices2GB.csv";
    public static final String STOCKS = "stocks.csv";
    private static BuildDataSet[] dataSetBuilders = new BuildDataSet[2];//two extra threads for building data sets, I think it's generous enough
    public static DataSet detailSet;
    public static ArrayList<String> tickerList;
    /**
     * attempts to add builder to array of running builders
     * @param file containing data set to try adding
     * throws exception if builder array is currently full
     *
     * @author  brian dunne
     */
    public static void addBuilder(File file){
        for(int i=0; i< dataSetBuilders.length; i++){
            if(dataSetBuilders[i] == null){
                dataSetBuilders[i] = new BuildDataSet(file);
                dataSetBuilders[i].start();
                return;
            }
        }
        throw new InternalError("there are no more available (allocated) threads for dataSetBuilders");
    }
    /**
     * attempts to add builder to array of running builders
     * @param fileName name of file in default directory to try adding
     * throws exception if builder array is currently full
     *
     * @author  brian dunne
     */
    public static void addBuilder(String fileName){
        addBuilder(ProgrammingProject.DATA_DIRECTORY_PATH+"\\dataSets", fileName);
    }
    /**
     * attempts to add builder to array of running builders
     * @param parentDirectory directory of file
     * @param fileName name of file in default directory to try adding
     * throws exception if builder array is currently full
     *
     * @author  brian dunne
     */
    public static void addBuilder(String parentDirectory, String fileName){
        addBuilder(new File(parentDirectory, fileName));
    }
    /**
     * checks the array of builders to se if any are ready
     * @return returns ArrayList of data sets that were ready
     *
     * @author  brian dunne
     */
    public static DataSet retrieveDataSet() {
        for(int i=0; i< dataSetBuilders.length; i++){
            if(dataSetBuilders[i]!=null){
                DataSet dataSet;
                System.out.println(dataSetBuilders[i]);
                if((dataSet =dataSetBuilders[i].getDataSet())!=null && !dataSet.name.contains(STOCKS)){
                    DataSet out = dataSet;
                    dataSetBuilders[i] = null;
                    return out;
                }
            }
        }
        return null;
    }
    /**
     * runs access and display tests on data set. can be personalised
     * @param testSet set to run tests on
     *
     * @author  brian dunne
     */
    private static void basicTest(DataSet testSet){
        if(testSet.name.contains(STOCKS)){
            detailSet = testSet;
            System.out.println("detailSet initialized");
            tickerList = new ArrayList<String>();
            for(int i = 0; i < detailSet.getSize(); i++) {
                tickerList.add(detailSet.getTicker(i));
            }
            System.out.println("tickerList initialized");
            ProgrammingProject.processing.dataContainer.updateNames();  //Updates containers in dataContainer with company names as opposed to tickers -E McDonald
        }
        if(testSet.name.contains("daily_prices")) {      //Not the most elegant, but prevents basicTest from running on the stocks.csv dataSet -E McDonald
            testSet.populateContainer(ProgrammingProject.processing.dataContainer);
            ArrayList<String> companyNames = new ArrayList<String>();
            for (int i = 0; i < testSet.nGroups(); i++) {
                //ProgrammingProject.processing.radioButtons.addRow();
                //String label = testSet.getGroup(i).get(0).ticker;
                //ProgrammingProject.processing.radioButtons.add(new SwitchButton(new Vector(0, 0), ProgrammingProject.processing.width / 5 - 40, 25, label, label, i, Color.GREEN));
                companyNames.add(testSet.getGroup(i).get(0).ticker);
                ProgrammingProject.processing.dataContainer.updateNames();
            }
            DataSet.companyNames = companyNames.toArray(new String[0]);
        }
    }


    private DataSet dataSet;
    private File file;
    private boolean ready;
    private final double fileSize;
    private double fileRead;
    private final ArrayList<Entry> entries;

    private final Object pauseLock;
    private volatile boolean paused, locked;

    public BuildDataSet(File file){
       this.file = file;
        ready = false;
        fileSize = file.length();
        if(fileSize < 5){
            throw new InternalError("file size is quite small");
        }
        fileRead = 0;
        entries = new ArrayList<>();
        paused = false;
        locked = false;
        pauseLock = new Object();
    }

    /**
     * what the thread will work on
     * building the data set
     *
     * @author  brian dunne
     */
    @Override
    public void run() {
        System.out.println("preparing data from file <"+file.getName()+">");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {

                if(paused) {
                    try {
                        synchronized (pauseLock) {
                            locked = true;
                            pauseLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                fileRead += line.length();
                try {
                    Entry entry = new Entry(line);
                    entries.add(entry);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            Collections.sort(entries);
            Entry[] entries = new Entry[this.entries.size()];
            for(int i =0; i< entries.length; i++){
                entries[i] = this.entries.get(i);
            }
            dataSet = new DataSet(file.getName(), entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Bing, data set\n"+ dataSet+"\nis ready.");
        fileRead = fileSize;
        ready = true;
        basicTest(dataSet);
    }

    /**
     * getDataSet checks if the data set is ready
     * @return complete data set or null
     *
     * @author  brian dunne
     */
    public DataSet getDataSet() {
        return ready? dataSet: null;
    }

    @Override
    public String toString() {
        return (super.toString()+file.getName()+"("+probeProgress()+"%)");
    }

    /**
     * probeProgress calculates the red file over file size
     * @return float that represents percentage
     *
     * @author  brian dunne
     */
    public float probeProgress(){
        return (float) ((fileRead /fileSize)*100);
    }

    /**
     * pauses this thread and makes executing thread wait till this reached it's pause
     *
     * @author  brian dunne
     */
    public void pause(){
        paused = true;
        while(!locked);//forces probing processing.java.util.processing.java.thread to wait until builder processing.java.util.processing.java.thread is locked
    }

    /**
     * un-pauses this thread
     *
     * @author  brian dunne
     */
    public void unPause(){
        paused = false;
        synchronized (pauseLock) {
            pauseLock.notify();
        }
        locked = false;
    }

    /**
     * pauses this thread then builds a data set out of the known information and un-pauses this thread
     * @return portion of the data set that is ready
     *
     * @author  brian dunne
     */
    public DataSet getReadySet(){ // it's here only because i wanted to se if i could tbh, it slows the process down and dos'nt help much but if it has a uce go ahead
        this.pause();

        Collections.sort(this.entries);
        Entry[] entries = new Entry[this.entries.size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = this.entries.get(i);
        }
        this.unPause();
        return new DataSet(file.getName(), entries);
    }
}
