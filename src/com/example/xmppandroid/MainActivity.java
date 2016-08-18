package com.example.xmppandroid;

import java.io.IOException;
import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{

	
	private AbstractXMPPConnection connection;
	
	private EditText userName;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		userName = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.register:
				break;

			case R.id.login:
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						doLogin();
					}
				}).start();
				break;

			default:
				break;
		}
	}

	private void doLogin()
	{
		
		if(null != connection)
		{
			Presence presence = new Presence(Presence.Type.unavailable);
			try
			{
				connection.disconnect(presence);
			}
			catch (NotConnectedException e)
			{
				e.printStackTrace();
			}	
		}
		
		
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setDebuggerEnabled(true)
                .setConnectTimeout(5*1000)
                .setSendPresence(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setCompressionEnabled(true)
//                .setHost("192.168.1.102")
                .setHost("192.168.1.127")
                .setServiceName("zhao-pc")
                .setPort(5222)
                .build();
         connection = new XMPPTCPConnection(config);
        try
		{
			connection.connect();
			connection.login(userName.getText().toString(), password.getText().toString());
//			connection.sendStanza(new Presence(Presence.Type.available));
//			connection.sendPacket(new Presence(Presence.Type.available));

			Roster roster = Roster.getInstanceFor(connection);
			
			
			/**
		     * 获取账户所有属性信息
		     * @return
		     */
			AccountManager accountManager = AccountManager.getInstance(connection);
			accountManager.getAccountAttributes();
			
			 /**
			  * 获取当前登录用户的所有好友信息
			  * @return
			  */
			Roster.getInstanceFor(connection).getEntries();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) 
			{
				System.out.println(entry);
			}
			  /**
		     * 获取指定账号的好友信息
		     * @param user	账号
		     * @return
		     */
			Roster.getInstanceFor(connection).getEntry("admin");
			
			/**
		     * 添加好友
		     * @param user         用户账号
		     * @param nickName	   用户昵称
		     * @param groupName    所属组名
		     * @return
		     */
//			Roster.getInstanceFor(connection).createEntry(user, nickName, new String[]{groupName});
			Roster.getInstanceFor(connection).createEntry("admin", "管理员", null);
			
			
			
		
			
			
			
			roster.addRosterListener(new RosterListener()
			{
				
				@Override
				public void presenceChanged(Presence arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void entriesUpdated(Collection<String> arg0)
				{
					
				}
				
				@Override
				public void entriesDeleted(Collection<String> arg0)
				{
					
				}
				
				@Override
				public void entriesAdded(Collection<String> arg0)
				{
					
				}
			});
			
			
			
		}
		catch (SmackException | IOException | XMPPException e)
		{
			e.printStackTrace();
		}
	}

}
