package com.tkt.cora1;

/**
 * Created by n1120697 on 14/7/17.
 */

public class PXIALGMOTION {

    public static native void Open(int HZ);		//	@brief Call this function to open algorithm
    public static native void Close();			//	@brief Call this function to close/reset algorithm
    public static native int GetReadyFlag();	//	@brief Call this funtion to get Ready_Flag
    public static native int GetMotionFlag(); 	//	@brief Call this funtion to get Motion_Flag
    public static native int GetVersion();		//	@brief Call this function to determine the version of the algorithm

//	public static native int DrvOpen();
//	public static native int DrvClose();
//	public static native int [] DrvReadAndProcess();
//	public static native float [] GetDisplayBuffer();
//	public static native int DrvGetStableTime();

    public static native int GetTouchFlag();
    /**
     * @brief When HRD and MEMS data are ready, call this function to do the algorithm processing
     *
     * @param[in] HRD_Data   Pointer to the buffer where HRD data (13 Bytes) is stored.
     * @param[in] MEMS_Data  Pointer to the buffer where MEMS data (3*sizeof(float) Bytes) is stored.
     *
     * @return Return one of the PXI_STATUS_FLAG types.
     */
    public static native int Process(char [] ppg_data, float [] mems_data);

//	public static native String OpenLogFile(String file);
//	public static native void CloseLogFile();


    /**
     * @brief Call this function to get PPG Signal Grade
     *
     * @param[out] grade	Pointer to a float variable where signal grade is stored.
     *
     * @return  Return 1 when Signal Grade is ready. Otherwise, return 0.
     */
    public static native float GetSigGrade();

    /**
     * @brief Call this function to set PPG Signal Grade Threshold
     *
     * @param[in] threshold	The PPG Signal Grade Threshold. Its value ranges from 0 to 100.
     *
     * @return 1 for success. 0 for failure.
     */
    public static native void SetSigGradeThrd(float thrd);

    /**
     * @brief Call this function to enable or disable fast output mode
     *
     * @param[in] en The flag of fast output mode.
     */
    public static native void EnableFastOutput(boolean enable);
    /**
     * @brief Call this function to notify algorithm the MEMS Scale of Motion Sensor
     *
     * @param[in] scale The MEMS Scale of Motion Sensor. Only 0(2G),1(4G~16G) are supported.
     *
     * @return 1 for success. 0 for failure.
     */
    public static native void SetMemsScale(int scale);
    /**
     * @brief Call this function to enable or disable motion mode
     *
     * @param[in] en The flag of motion mode.
     */
    public static native void EnableMotionMode(boolean enable);
    /**
     * @brief Call this function to enable or disable auto mode
     * @param[in] enable The flag of auto mode.
     */
    public static native void EnableAutoMode(boolean enable);

    public static native int GetHR();

    static {
        System.loadLibrary("paw8001motion");
    }
}
