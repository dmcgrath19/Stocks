package processing.java.thread.data;

import processing.java.thread.BuildDataSet;

/**
 * group describes reasonable sub divisions of the data set
 *
 * @version 1.0
 * @since   2020-04-10
 */
public class Group implements Comparable<Group>{
    public String ticker, exchange, name, sector, industry;
    public final int start, end;
    //TODO store relevant information from stocks.csv here

    public Group(String ticker, int start, int end){
        this.ticker = ticker;
        if(BuildDataSet.detailSet!=null) {
            getDetails(ticker);
        }
        else
        {
            exchange = name = sector = industry = null;
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return ticker +"["+(end-start)+"]";
    }

    /**
     * name -> start -> end heavy compare
     * @param o other group
     * @return int representing difference
     *
     */
    @Override
    public int compareTo(Group o) {
        int out = ticker.compareTo(o.ticker);
        if(out == 0){
            out = start - o.start;
        }
        if(out == 0){
            out = end - o.end;
        }
        return out;
    }

    /**
     * implementation of equals to see if two groups are the same
     * @param obj other object
     * @return boolean true if the object is a group and it compares to having no difference
     *
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && compareTo((Group) obj)==0;
    }

    public void getDetails(String ticker)
    {
        exchange = BuildDataSet.detailSet.getExchange(this.ticker);
        name = BuildDataSet.detailSet.getCompanyName(this.ticker);
        sector = BuildDataSet.detailSet.getSector(this.ticker);
        industry = BuildDataSet.detailSet.getIndustry(this.ticker);
    }
}
