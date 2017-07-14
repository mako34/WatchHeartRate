package com.tkt.cora1;

/**
 * Created by n1120697 on 14/7/17.
 */

public class HeartRateDetection {

    static{
        System.loadLibrary("xinmeilai_pixart_heartrate");
    }

    public static native int OpenPixart();
    public static native int ClosePixart(int fd);
    public static native char[] GetPpgData(int fd);
    public static native float[] GetMemsData(int fd);
    public static native float GetHeartRate();

}
