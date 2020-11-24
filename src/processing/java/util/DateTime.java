package processing.java.util;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *personalised date time because java calender doesn't have much positive feed back
 *
 * @version 0.0
 * @since   2020-04-10
 */
public class DateTime implements Comparable<DateTime>{
    private final long time;
    private final int day, month, year;

    public DateTime(long time, int day, int month, int year) {
        this.time = time;
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public DateTime(String dateTime){
        LocalDateTime dateTimeNow = java.time.LocalDateTime.now();
        time = dateTimeNow.getNano();
        Matcher matcher = Pattern.compile("(?:(?:(\\d{4})[-\\s/](\\d{1,2})[-\\s/](\\d{1,2}))|(?:(\\d{1,2})[-\\s/](\\d{1,2})[-\\s/](\\d{4})))[.\\r\\n]*").matcher(dateTime);
        if(matcher.matches()){
            if (!matcher.group(1).equals("")){
                day = Integer.parseInt(matcher.group(3));
                month = Integer.parseInt(matcher.group(2));
                year = Integer.parseInt(matcher.group(1));
            }else{
                day = Integer.parseInt(matcher.group(4));
                month = Integer.parseInt(matcher.group(5));
                year = Integer.parseInt(matcher.group(6));
            }
        }else{
            day = dateTimeNow.getDayOfMonth();
            month = dateTimeNow.getMonthValue();
            year = dateTimeNow.getYear();
        }
    }

    /**
     * gets the date in the form yyy-mm-dd
     * @return string representation of date
     */
    public String yyyyMmDd() {
        return String.format("%1$4s", year).replace(' ', '0')+"-"+
                String.format("%1$2s", month).replace(' ', '0')+"-"+
                String.format("%1$2s", day).replace(' ', '0');
    }

    /**
     * year ->month -> day heavy compare, gets the earlier date as smaller
     * @param o other DateTime
     * @return int representing difference
     *
     */
    @Override
    public int compareTo(DateTime o) {
        int out = year - o.year;
        if(out ==0){
           out = month - o.month;
        }
        if (out == 0){
            out = day - o.day;
        }
        if (out == 0){
            out = (int) (time - o.time);
        }
        return out;
    }

    /**
     * counts the days between two dates, gets absolute difference
     * @param o other date to count from
     * @return int number of days
     *
     */
    public int daysBetween(DateTime o) { //returns number of days between two DateTimes
        int days = 0;
        if(compareTo(o) == 0) return days;
        if(compareTo(o) > 0) return o.daysBetween(this);
        for (int y = year; y < o.year; y++) {
            days += 365;
            if(y%400==0||((y%4==0)&&y%100!=0)){
                days++;
            }
        }
        int dif = o.month-month;
        int dir = dif==0? 0:(dif)/Math.abs(dif);
        for (int m = Math.min(month,o.month); m < Math.max(month, o.month); m++) {
            days += daysInMonth(m,o.year)*dir;
        }

        dif = o.day-day;
        dir = dif==0? 0:(dif)/Math.abs(dif);
        days+= Math.abs(dif)*dir;
        return days;
    }

    /**
     * switch statement dictating the days in each month
     * @param month month of the year
     * @param year year, to account for february
     * @return int days in the month
     *
     */
    public static int daysInMonth(int month, int year){
        switch (month){
            case (4):
            case (6):
            case (9):
            case (11):
                return 30;
            case (2):
                if (year%400==0||((year%4==0)&&year%100!=0)) return 29;
                return 28;
            default:
                return 31;
        }
    }

    public DateTime addDays(int days)
    {
        int newDay = this.day;
        int newMonth = this.month;
        int newYear = this.year;
        while(days != 0) {
            if (newDay + 1 <= daysInMonth(newMonth, newYear)) {
                newDay += 1;
                days -= 1;
            } else if (newMonth + 1 <= 12) {
                newDay = 1;
                newMonth += 1;
                days -= 1;
            }
            else {
                newDay = 1;
                newMonth = 1;
                newYear += 1;
                days -= 1;
            }
        }
        return new DateTime(0, newDay, newMonth, newYear);
    }
}
