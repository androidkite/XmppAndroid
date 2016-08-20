package com.androidkite.xmppandroid.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by zhao on 2016/8/20.
 */
public class XmppUtil
{
	/**
	 * 创建一个新的xmppConnection
	 * 
	 * @param server
	 *            服務器地址
	 * @param port
	 *            服务器端口
	 * @return {@link XMPPTCPConnection}
	 */
	public static XMPPTCPConnection newConnection(String server, int port)
	{
		XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
		builder.setServiceName(server);
		builder.setHost(server);
		builder.setPort(port);
		builder.setCompressionEnabled(false);
		builder.setDebuggerEnabled(true);
		builder.setSendPresence(true);
		builder.setConnectTimeout(10 * 1000);
		builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		XMPPTCPConnection connection = new XMPPTCPConnection(builder.build());
		return connection;
	}

}
