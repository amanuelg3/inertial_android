package com.jms.software.inertial;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
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
public class BroadcasterFragment extends Fragment implements SensorEventListener {
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
		}
				
		setRetainInstance(true);
		mSensorManager = (SensorManager)getActivity().getSystemService(Activity.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	
	public void startBroadcasting(){
		if(ct==null){
			ct = new ClientThread(mHostAddress,mPortNumber,mPeriod);
			ct.start();
		}
	}
	
	public void stopBroadcasting(){
		ct.stopThread();
		ct = null;																																									
	}
	
	public void setConnectionParameters(String hostAddress, int portNumber, int period){
		mHostAddress = hostAddress;
		mPortNumber = portNumber;
		mPeriod = period;
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



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

}
