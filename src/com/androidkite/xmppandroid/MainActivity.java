package com.androidkite.xmppandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.androidkite.xmppandroid.util.XmppUtil;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;

public class MainActivity extends Activity implements OnClickListener
{

    private String TAG = getClass().getSimpleName();


	
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
        findViewById(R.id.create_account).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
		userName = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
	}


    private android.os.Handler mHandler = new android.os.Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(),msg.obj+"",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

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

            case R.id.send:
            {
                EditText contentEd = (EditText) findViewById(R.id.content);
                EditText toId = (EditText) findViewById(R.id.toid);
                String c = contentEd.getText().toString();
                String t = toId.getText().toString();

                ChatManager chatmanager = ChatManager.getInstanceFor(connection);
//                Chat mChat = chatmanager.createChat(t+"@192.168.1.127");
                Chat mChat = chatmanager.createChat(t + "@zhao-pc/Smack");
                try
                {
                    mChat.sendMessage(c);
                }
                catch (NotConnectedException e)
                {
                    e.printStackTrace();
                }
                break;
            }


            case R.id.create_account:
            {

                ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                chatmanager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean b) {
                        chat.addMessageListener(new ChatMessageListener() {
                            @Override
                            public void processMessage(Chat chat, Message message) {
                                String content=message.getBody();
                                if (content!=null){
                                    Log.e("TAG", "from:" + message.getFrom() + " to:" + message.getTo() + " message:" + message.getBody());
                                    android.os.Message message1= android.os.Message.obtain();
                                    message1.what=1;
                                    message1.obj="收到消息：" + message.getBody()+" 来自:"+message.getFrom();
                                    mHandler.sendMessage(message1);
                                }
                            }
                        });

                    }
                });
                break;
            }
			default:
				break;
		}
	}






    private class LoginAsyTask extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            return null;
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
		
		
//       connection = XmppUtil.newConnection("192.168.1.102",5222);
         connection = XmppUtil.newConnection("192.168.1.127",5222);
        try
		{
			connection.connect();
			connection.login(userName.getText().toString(), password.getText().toString());
//			connection.sendStanza(new Presence(Presence.Type.available));
//			connection.sendPacket(new Presence(Presence.Type.available));
            connection.addConnectionListener(new XmppConnectionListener());

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
            roster.getEntry("admin");
			
			/**
		     * 添加好友
		     * @param user         用户账号
		     * @param nickName	   用户昵称
		     * @param groupName    所属组名
		     * @return
		     */
//			Roster.getInstanceFor(connection).createEntry(user, nickName, new String[]{groupName});
            roster.createEntry("admin", "管理员", null);
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
