package com.jms.software.inertial;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataBroadcaster extends Service {
	public DataBroadcaster() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
