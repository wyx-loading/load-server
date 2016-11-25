package com.loading.server.config;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ServerConfig")
public class ServerConfig {
	
	/** Socket 绑定地址 */
	private String socketHost;
	/** Socket 绑定端口 */
	private int socketPort;
	
	/** 同一个ip上允许登录的用户数目 */
	private int maxUserPerIp;
	/** 禁止连接的ip列表，";"隔开 */
	private Set<String> invalidIps;
	/** 不受限制的ip列表，";"隔开 */
	private Set<String> noLimitIps;
	
	/** 基础读空闲超时秒 */
	private int commonReadTimeoutSeconds;
	
	public void setInvalidIps(String invalidIpsStr) {
		this.invalidIps = ImmutableSet.<String>builder()
				.add(invalidIpsStr.split(";"))
				.build();
	}

	public void setNoLimitIps(String noLimitIpsStr) {
		this.noLimitIps = ImmutableSet.<String>builder()
				.add(noLimitIpsStr.split(";"))
				.build();
	}
	
	
	public String getSocketHost() { return socketHost; }
	public int getSocketPort() { return socketPort; }

	public int getMaxUserPerIp() { return maxUserPerIp; }
	public Set<String> getInvalidIps() { return invalidIps; }
	public boolean invalidIp(String ip) {
		return invalidIps != null && invalidIps.contains(ip);
	}
	public Set<String> getNoLimitIps() { return invalidIps; }
	public boolean isNoLimitIp(String ip) {
		return noLimitIps != null && noLimitIps.contains(ip);
	}
	
	public int getCommonReadTimeoutSeconds() { return commonReadTimeoutSeconds; }

}
