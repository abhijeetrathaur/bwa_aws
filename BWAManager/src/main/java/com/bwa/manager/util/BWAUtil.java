package com.bwa.manager.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The Class BWAUtil.
 */
public class BWAUtil {
	
	/**
	 * Sleep.
	 *
	 * @param time the time
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getIpAddress() {
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipAddress;
	}
}
