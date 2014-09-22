package com.jms.software.inertial;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements BroadcasterFragment.OnFragmentInteractionListener{

	private Button connectButton;
	private EditText addressEditText, portEditText, periodEditText;
	private String hostAddress;
	private int portNumber;
	private int period;
	private BroadcasterFragment broadcasterFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		connectButton = (Button)findViewById(R.id.connectButton);
		addressEditText = (EditText)findViewById(R.id.addressEditText);
		portEditText = (EditText)findViewById(R.id.portEditText);
		periodEditText = (EditText)findViewById(R.id.periodEditText);
		getConnectionSettings();
		
		FragmentManager fm = getSupportFragmentManager();
		broadcasterFragment = (BroadcasterFragment) fm.findFragmentByTag("broadcaster");
		
		if(broadcasterFragment == null){
			broadcasterFragment = BroadcasterFragment.newInstance(hostAddress, portNumber, period);
			fm.beginTransaction().add(broadcasterFragment, "broadcaster").commit();
		}
		
		
		
	}
	
	public void onConnectClicked(View view){
		getConnectionSettings();
		broadcasterFragment.setConnectionParameters(hostAddress, portNumber, period);
		broadcasterFragment.startBroadcasting();
	}

	public void onDisconnectClicked(View view){
		broadcasterFragment.stopBroadcasting();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
@Override
	public void onFragmentInteraction(Uri uri) {
		
		
	}

	/*private void startBroadcasting(){
		Intent broadcastIntent = new Intent(this,DataBroadcaster.class);
		Bundle b = new Bundle();
		b.putInt("port", portNumber);
		b.putString("address", hostAddress);
		broadcastIntent.putExtras(b);
		startService(broadcastIntent);
	}
	*/
	private void getConnectionSettings(){
		try{
			portNumber = Integer.parseInt(portEditText.getText().toString());
			hostAddress = addressEditText.getText().toString();
			period = Integer.parseInt(periodEditText.getText().toString());
		} catch(NumberFormatException e){
			Toast.makeText(this, "Port number and period must be a integer number!", Toast.LENGTH_LONG).show();
			portNumber = 8888;
			period = 20;
		}
		
	}


}
