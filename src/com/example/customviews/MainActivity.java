package com.example.customviews;

import java.io.BufferedReader;
import java.io.FileReader;
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
		hView = (com.example.customviews.CustomHorizontalScrollView)findViewById(R.id.hscrollview);

		//getImageResourceFromJson();
		AssetManager am = this.getAssets();
		InputStream is = null;
		try {
			is = am.open("Json");
		} catch (IOException e) {
			System.out.println("problem reading file");
			e.printStackTrace();
		}
		String json = getJsonFromFile(is);
		url1="http://img6a.flixcart.com/image/t-shirt/s/n/g/wsmad-kb-t1-tee09turquoise-madagascar-400x400-imadkfvvwvmzvcjz.jpeg";
		url2="http://img6a.flixcart.com/image/t-shirt/s/n/g/wsmad-kb-t1-tee09turquoise-madagascar-400x400-imadkfvvwvmzvcjz.jpeg";
		url3="http://img6a.flixcart.com/image/t-shirt/s/n/g/wsmad-kb-t1-tee09turquoise-madagascar-400x400-imadkfvvwvmzvcjz.jpeg";
		//		hView.addStoreFrontImage(url1,"LIFESTYLE", "&FASHION");
		//		hView.addStoreFrontImage(url2,"ELECTRONICS", "&GADGETS");		
		//		hView.addStoreFrontImage(url3,"BOOKS", "&MEDIA");
		
		hView.addStoreFrontImage(json, "line1Text","line2Text");
	}
	public String getJsonFromFile(InputStream is){
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
