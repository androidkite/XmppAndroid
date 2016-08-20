package com.androidkite.xmppandroid;

import android.util.Log;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by zhao on 2016/8/20.
 */
public class XmppConnectionListener implements ConnectionListener
{

    private static  final String TAG = XmppConnectionListener.class.getSimpleName();

	@Override
	public void connected(XMPPConnection connection)
	{
        Log.d(TAG,"connected");
    }

	@Override
	public void authenticated(XMPPConnection connection, boolean resumed)
	{
        Log.d(TAG,"authenticated resumed = " + resumed);
	}

	@Override
	public void connectionClosed()
	{
        Log.d(TAG,"connectionClosed");

	}

	@Override
	public void connectionClosedOnError(Exception e)
	{
        Log.e(TAG,"connectionClosedOnError",e);

	}

	@Override
	public void reconnectionSuccessful()
	{
        Log.d(TAG,"reconnectionSuccessful");
	}

	@Override
	public void reconnectingIn(int seconds)
	{
        Log.d(TAG,"reconnectingIn seconds = " + seconds);
	}

	@Override
	public void reconnectionFailed(Exception e)
	{
        Log.e(TAG,"reconnectionFailed",e);
	}
}
