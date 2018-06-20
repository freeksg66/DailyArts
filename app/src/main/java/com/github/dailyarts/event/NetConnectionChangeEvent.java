package com.github.dailyarts.event;

/**
 * Created by legao005426 on 2018/6/20.
 */

public class NetConnectionChangeEvent {
    public final static int UNIDENTIFIED = 1;
    public final static int G2 = 2;
    public final static int G3 = 3;
    public final static int G4 = 4;
    public final static int WIFI = 5;
    public final static int UNKNOWN = 6;
    public final static int DISABLED = 7;
    public final static int NET_UNKNOWN = 8;

    public boolean isAvailable;
    public int networkState;
}
