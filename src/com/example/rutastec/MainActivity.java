package com.example.rutastec;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	String str;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ListView miListView = (ListView) findViewById(R.id.listView1);
		
		
		String[] rutas = new String[] {
											"Cumbres",
											"Lincoln",
											"Linda Vista",
											"San Jeronimo",
											"San Nicolas",
											"Valle 1",
											"Valle 2",
											"Valle 3",
											"Circuito Garza Sada (dia)",
											"Circuito A (noche)",
											"Circuito B (noche)",
											"Circuito C (noche)"};
		
		final ArrayList<String> listRutas = new ArrayList<String>();
		listRutas.addAll(Arrays.asList(rutas));
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.row, listRutas);
		miListView.setAdapter(listAdapter);
		
		OnItemClickListener regisroOnItemClickListener = new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				String rutaS = listRutas.get(position);
				
				Intent intent = new Intent(MainActivity.this, RutaActivity.class );
				intent.putExtra("ruta", rutaS);
				startActivity(intent);
				
				
			}
		};
		miListView.setOnItemClickListener(regisroOnItemClickListener);
		
		
	}

	
}
