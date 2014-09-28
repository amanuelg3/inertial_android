package com.jms.software.inertial;

import java.security.spec.MGF1ParameterSpec;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.SensorEventListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link BroadcasterFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link BroadcasterFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class BroadcasterFragment extends Fragment{
	
	private static final String ARG_HOSTADDRESS = "host_addres";
	private static final String ARG_PORTNUMBER = "port_number";
	private static final String ARG_PERIOD = "period";

	private String mHostAddress;
	private int mPortNumber;
	private int mPeriod;
	
	
	private ClientThread ct;

	private OnFragmentInteractionListener mListener;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mGyro;
	private DataVector mAccelerationVect;
	private DataVector mAngularVeloVect;
	private long startTime;
	
	private String mData;
	


	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment BroadcasterFragment.
	 */
	public static BroadcasterFragment newInstance(String hostAddress, int portNumber, int period) {
		BroadcasterFragment fragment = new BroadcasterFragment();
		Bundle args = new Bundle();
		args.putString(ARG_HOSTADDRESS, hostAddress);
		args.putInt(ARG_PORTNUMBER, portNumber);
		args.putInt(ARG_PERIOD, period);
		fragment.setArguments(args);
		return fragment;
	}

	public BroadcasterFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mHostAddress = getArguments().getString(ARG_HOSTADDRESS);
			mPortNumber = getArguments().getInt(ARG_PORTNUMBER);
			mPeriod = getArguments().getInt(ARG_PERIOD);
			startTime = SystemClock.elapsedRealtime();
		}
				
		setRetainInstance(true);
		mSensorManager = (SensorManager)getActivity().getSystemService(Activity.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE); 
		mAccelerationVect = new DataVector();
		mAngularVeloVect = new DataVector();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mSensorManager.registerListener(accelerometerListener, mAccelerometer , Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(gyroListener, mGyro, Sensor.TYPE_GYROSCOPE);
	}

	@Override
	public void onPause() {
		//TODO Is it correct? Will be broadcasting without sensor.
		super.onPause();
		mSensorManager.unregisterListener(accelerometerListener);
		mSensorManager.unregisterListener(gyroListener);
	}

	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setText(R.string.hello_blank_fragment);
		return textView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	/*@Override
	public void onSensorChanged(SensorEvent event) {
		mAccelerationVect.setValues(event.values);
		prepareData();
		if(ct!=null)
			ct.setData(mData);
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing
		
	}
*/
	public void startBroadcasting(){
		if(ct==null){
			ct = new ClientThread(mHostAddress,mPortNumber,mPeriod);
			ct.start();
		}
	}
	
	public void stopBroadcasting(){
		if(ct!=null)
			ct.stopThread();
		ct = null;																																									
	}
	
	public void setConnectionParameters(String hostAddress, int portNumber, int period){
		mHostAddress = hostAddress;
		mPortNumber = portNumber;
		mPeriod = period;
	}
	
	private void prepareData(){
		mData =String.format("%3.3f %3.3f %3.3f %3.3f %3.3f %3.3f %3.3f",
									mAccelerationVect.getX(),
									mAccelerationVect.getY(),
									mAccelerationVect.getZ(),
									mAngularVeloVect.getX(),
									mAngularVeloVect.getY(),
									mAngularVeloVect.getZ(),
									getTimestamp());
	}
	
	private float getTimestamp(){
		return (float)(SystemClock.elapsedRealtime()-startTime)/1000;
		
	}
	
	
	


	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}
	
	
	
	private SensorEventListener accelerometerListener = new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {	
			mAccelerationVect.setValues(event.values);
		}
			
	};
	
	private SensorEventListener gyroListener = new SensorEventListener() {

		
		@Override
		public void onSensorChanged(SensorEvent event) {
			mAngularVeloVect.setValues(event.values);
			prepareData();
			if(ct!=null){
				ct.setData(mData);
			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};
	
	
	/**
	 * This class stores acceleration vector.
	 * @author Jakub
	 *
	 */
	private class DataVector{
		public float x,y,z;
		
		public DataVector(){}
		public DataVector(float[] values){
			setValues(values);
		}
		
		public void setValues(float[] values){
			x=values[0];
			if(values.length >= 2){
				y=values[1];
				if(values.length>=3){
					z=values[2];
				}
			}
		}
		
		public float getX(){
			return x;
		}
		public float getY(){
			return y;
		}
		public float getZ(){
			return z;
		}
		
	}

}
