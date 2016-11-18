package com.loading.server;

import java.util.concurrent.ConcurrentHashMap;

import com.loading.server.config.Configs;

public class IpFloodChecker {
	
	private static class SingletonHolder {
		private static IpFloodChecker instance = new IpFloodChecker();
	}
	
	public static IpFloodChecker getInstance() {
		return SingletonHolder.instance;
	}
	
	private ConcurrentHashMap<String, IpCount> ip_count = new ConcurrentHashMap<String, IpCount>();

	// a mutable value，修改时少查一次hash，用AtomInteger也可以。
	private static class IpCount {
		public short value = 1;
	}

	public synchronized boolean addIp(String ip) {
		if (Configs.instance().serverConfig().getMaxUserPerIp() <= 0) {
			return true;
		}

		if (Configs.instance().serverConfig().invalidIp(ip)) {
			return false;
		}

		if (isNoLimitAddress(ip)) {
			return true;
		}

		IpCount count = ip_count.get(ip);
		if (count == null) {
			ip_count.put(ip, new IpCount());
			return true;
		}

		if (count.value < Configs.instance().serverConfig().getMaxUserPerIp()) {
			count.value++;
			return true;
		} else {
			return false;
		}
	}

	synchronized void decreaseIpCount(String ip) {
		if (Configs.instance().serverConfig().getMaxUserPerIp() <= 0) {
			return;
		}

		IpCount count = ip_count.get(ip);
		if (count == null) {
			return;
		}

		count.value--;
		if (count.value <= 0) {
			ip_count.remove(ip);
		}
	}

	private boolean isNoLimitAddress(String ip) {
		// 先判断ip段，只支持xxx.xxx.xxx格式的ip端。
		int pos = ip.lastIndexOf(".");
		if (pos > 1) {
			String ipSegment = ip.substring(0, pos);
			return Configs.instance().serverConfig().isNoLimitIp(ipSegment);
		}
		return Configs.instance().serverConfig().isNoLimitIp(ip);
	}

}
