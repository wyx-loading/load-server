package com.loading.server.config;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ServerConfig")
public class ServerConfig {
	
	/** Socket 绑定地址 */
	private String socketHost;
	/** Socket 绑定端口 */
	private int sockPort;
	
	/** 同一个ip上允许登录的用户数目 */
	private int maxUserPerIp;
	/** 禁止连接的ip列表，";"隔开 */
	private Set<String> invalidIps;
	/** 不受限制的ip列表，";"隔开 */
	private Set<String> noLimitIps;
	
	public void setInvalidIps(String invalidIpsStr) {
		ImmutableSet<String> config = ImmutableSet.<String>builder()
				.add(invalidIpsStr.split(";"))
				.build();
		this.invalidIps = config;
	}

	public void setNoLimitIps(String noLimitIpsStr) {
		ImmutableSet<String> config = ImmutableSet.<String>builder()
				.add(noLimitIpsStr.split(";"))
				.build();
		this.noLimitIps = config;
	}
	
	
	public String getSocketHost() { return socketHost; }
	public int getSockPort() { return sockPort; }

	public int getMaxUserPerIp() { return maxUserPerIp; }
	public Set<String> getInvalidIps() { return invalidIps; }
	public boolean invalidIp(String ip) {
		return invalidIps != null && invalidIps.contains(ip);
	}
	public Set<String> getNoLimitIps() { return invalidIps; }
	public boolean isNoLimitIp(String ip) {
		return noLimitIps != null && noLimitIps.contains(ip);
	}

}
