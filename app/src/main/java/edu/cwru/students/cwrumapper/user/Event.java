package edu.cwru.students.cwrumapper.user;

public class Event implements Comparable<Event>{
    private Location location;
    private int length;
    private int roomNumber;
    private int hour;
    private int min;
    private int sec;
    private int endHour;
    private int endMin;
    private int endSec;

    public Event(Location location, int length, int roomNumber, int hour, int min, int sec) {
        this.location = location;
        this.length = length;
        this.roomNumber = roomNumber;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.endHour = hour + (sec+length)/3600;
        this.endMin = min + ((sec+length)%3600)/60;
        this.endSec = ((sec+length)%60);
    }

    public int compareTo(Event other){
        int hourT = Integer.compare(this.hour, other.getHour());
        if(hourT != 0){
            return hourT;
        } else{
            int minT = Integer.compare(this.hour, other.getMin());
            if(minT != 0) {
                return minT;
            } else {
                int secT = Integer.compare(this.hour, other.getSec());
                if(secT != 0) {
                    return secT;
                }else{
                    return this.location.getName().compareTo(other.getLocation().getName());
                }
            }
        }
    }

    public boolean isConflict(Event other){
        if(!(this.isRealTime() && other.isRealTime())){
            return true;
        }
        int tempThisStartTime = this.hour*3600+this.min*60+this.sec;
        int tempOtherStartTime = other.getHour()*3600+other.getMin()*60+other.getSec();
        int tempThisEndTime = this.endHour*3600+this.endMin*60+this.endSec;
        int tempOtherEndTime = other.getEndHour()*3600+other.getEndMin()*60+other.getEndSec();
        return ((tempOtherEndTime > tempThisStartTime && tempOtherEndTime < tempThisEndTime)||(tempOtherStartTime > tempThisStartTime && tempOtherStartTime < tempThisEndTime));
    }

    public boolean isRealTime(){
        boolean condition1 =  length>0;
        boolean condition2 = -1 < hour && hour < 24 && endHour < 24;
        boolean condition3 = sec >= 0 &&  min >= 0 && sec < 60 && min < 60;
        return condition1 && condition2 && condition3;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public int getEndSec() {
        return endSec;
    }

    public Location getLocation() {
        return location;
    }

    public double getEventLength() {
        return length;
    }

    public double getRoomNumber() {
        return roomNumber;
    }

}
