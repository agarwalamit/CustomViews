package com.example.customviews;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{

	com.example.customviews.CustomHorizontalScrollView hView;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		hView = (com.example.customviews.CustomHorizontalScrollView)findViewById(R.id.hscrollview);
		
		//getImageResourceFromJson();
		hView.addStoreFrontImage(R.drawable.lifestyle_fashion,"LIFESTYLE", "&FASHION");
		hView.addStoreFrontImage(R.drawable.electronics_gadgets,"ELECTRO", "&GADGETS");		
		hView.addStoreFrontImage(R.drawable.books_media,"BOOKS", "&MEDIA");
	}

	


}
