package com.example.rutastec;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rutastec.MyService.LocalBinder;
import com.firebase.client.Firebase;


public class RutaActivity extends ActionBarActivity {
	Firebase ref;
	Firebase postRef;
	LocationManager mlocManager;
	LocationListener mlocListener;
	static String ruta;
	static String ruta2;
	TextView nombre;
	static TextView lonText;
	static TextView latText;
	static TextView lat;
	static TextView lon;
	static ToggleButton toggleButton1;
	Intent intent;
	String s;
	String serviceRuta;
	MyService mService;
    boolean mBound = false;
    Context cont;
    static boolean  service = false;
    Context c;
	
	   /** Defines callbacks for service binding, passed to bindService() */
    public ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d("ServiceConnection","connected");
            LocalBinder binder2 = (LocalBinder) binder;
            mService = binder2.getService();
            mBound = true;
        }
        //binder comes from server to communicate with method's of 

        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection","disconnected");
            mService = null;
            mBound = false;
        }
    };
    
    public void setInv(){
    	
    	
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruta);
		nombre = (TextView) findViewById(R.id.txtRuta);
		
		lat = (TextView) findViewById(R.id.tvlat);
		lon = (TextView) findViewById(R.id.tvlong);
		
		
		lonText = (TextView) findViewById(R.id.textview1);
		latText = (TextView) findViewById(R.id.textView4);
		
		//Button buton = (Button) findViewById(R.id.button1);
		Bundle datos = getIntent().getExtras(); 
		if(datos != null){
			ruta = datos.getString("ruta");
			nombre.setText(ruta);
		
		}	
		
		setTitle(ruta);
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		
		s = toggleButton1.getText().toString();	
		toggleButton1.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				s = toggleButton1.getText().toString();	
				if(s.equals("ON")){
					
					intent = new Intent(RutaActivity.this, MyService.class );
	
					intent.putExtra("ruta", ruta);
					
					
					if(!service)
					startService(intent);
					mBound = getApplicationContext().bindService( new Intent(getApplicationContext(), MyService.class), myConnection, Context.BIND_AUTO_CREATE );
					service = true;
					
					Toast.makeText( getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();
					
				}
				if(s.equals("OFF")){
					intent = new Intent(RutaActivity.this, MyService.class );
					lat.setText("0");
					lon.setText("0");
					
					
					if (mBound)
					    getApplicationContext().unbindService(myConnection);
					if(service)
						stopService(intent);
					service = false;
					 mBound = false;
					Toast.makeText( getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
				}		 
			}
		}); 
		
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if(mBound)
	    	 getApplicationContext().unbindService(myConnection);
	    mBound=false;
	}
	
	@Override
	public void onBackPressed() {
		
		if(s.equals("ON"))
			moveTaskToBack(true);
		else
			super.onBackPressed();
			
	}



	

}
