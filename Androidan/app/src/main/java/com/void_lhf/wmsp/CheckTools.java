package com.void_lhf.wmsp;

import android.os.*;
import android.text.*;
import android.util.*;
import java.net.*;
import java.util.*;
import android.content.*;

public class CheckTools
{
	public static boolean isVpnUsed() {  
		try {  
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();  
			if(niList != null) {  
				for (NetworkInterface intf : Collections.list(niList)) {  
					if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {  
						continue;  
					}
					if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){                          
						return true;
					}  
				}  
			}  
		} catch (Throwable e) {  
			e.printStackTrace();  
		}  
		return false;  
	} 
	
	public static boolean isWifiProxy(Context context){
		final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
		String proxyAddress;
		int proxyPort;
		if (is_ics_or_later) {
			proxyAddress = System.getProperty("http.proxyHost");
			String portstr = System.getProperty("http.proxyPort");
			proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
			System.out.println(proxyAddress + "~");
			System.out.println("port = " + proxyPort);
		}else {
			proxyAddress = android.net.Proxy.getHost(context);
			proxyPort = android.net.Proxy.getPort(context);
		}
		return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
	}
	
	//检测软件是否需要更新
	public static boolean needUpdate(long versionCode,Context context) {
		long currentVersionCode = APKVersionCodeUtils.getVersionCode(context);
		if(versionCode-currentVersionCode>0) {
			return true;
		}
		return false;
	}
}

