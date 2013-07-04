package com.example.customviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;

public class MainActivity extends Activity{

	com.example.customviews.CustomHorizontalScrollView hView;

	String url1,url2,url3;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		hView = (com.example.customviews.CustomHorizontalScrollView)findViewById(R.id.customhorizontalscrollview);
		String json = getJsonFromFile();
		hView.addStoreFrontImage(json, "line1Text","line2Text");
	}
	public String getJsonFromFile(){
		AssetManager am = this.getAssets();
		InputStream is = null;
		try {
			is = am.open("Json");
		} catch (IOException e) {
			System.out.println("problem reading file");
			e.printStackTrace();
		}
		String json="";
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new InputStreamReader(is));

			while ((sCurrentLine = br.readLine()) != null) {
				json = json + sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("here ioexception");
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return json;
	}



}
