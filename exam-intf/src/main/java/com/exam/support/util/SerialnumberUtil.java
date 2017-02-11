package com.exam.support.util;


/**
 * 消息流水
 * @author hong.ding
 *
 */
public class SerialnumberUtil {
	private static int alarmSerial;
	private static int realSignalSerial;
	private static int commonSerial;
	public static final int TYPE_HEARTBEAT=0;
	public static final int TYPE_ALARM=0;
	public static final int TYPE_REAL_SIGNAL=1;
	public static final int TYPE_COMMON=2;
	public  static int  getSerial(int type)
	{
		synchronized (SerialnumberUtil.class) {
			int result =0;
			switch (type) {
			case TYPE_ALARM:
				result =  alarmSerial;
				alarmSerial++;
				break;
			case TYPE_REAL_SIGNAL:
				result =  realSignalSerial;
				realSignalSerial++;
				break;
			case TYPE_COMMON:
				result =  commonSerial;
				commonSerial++;
				break;
			default:
				throw new FavorDataException("the type is wrong");
			}
			
			return result;
		}
		
	}
}
