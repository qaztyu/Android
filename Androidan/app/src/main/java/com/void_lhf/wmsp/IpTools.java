package com.void_lhf.wmsp;

public  class IpTools
{
	public static String getDip() {
		String ip = "";
		int ipArray[] = new int[4];
		for(int i =0;i<4;i++) {
			ipArray[i] = (int) (Math.random()*255)+1;
			if(i!=3) {
				ip = ip + ipArray[i]+".";
			} else {
				ip = ip + ipArray[i];
			}

		}
		return ip;
	}
}
