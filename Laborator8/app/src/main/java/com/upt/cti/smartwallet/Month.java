package com.upt.cti.smartwallet;

public enum Month {
    January, February, March, April, May, June, July, August,September, October, November, December;

    public static int getMonthIndexFromName(Month month){
        return month.ordinal();
    }

    public static Month getMonthFromIndex(int index){
        return Month.values()[index];
    }

    public static int monthFromDate(String timestamp){
        int month = Integer.parseInt(timestamp.substring(5,7));
        return month - 1; //indexed from 0 to 11
    }
}
