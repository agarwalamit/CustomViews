package com.example.customviews;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryStoreThreeItemWidget extends LinearLayout{

	private final int SCREEN_LEFT_PADDING_DP = 2;
	private final int SCREEN_TOP_PADDING_DP = 5;
	private final int SCREEN_RIGHT_PADDING_DP = 2;
	private final int SCREEN_BOTTOM_PADDING_DP = 5;

	private final int IMAGE_LEFT_MARGIN_DP = 2;
	private final int IMAGE_RIGHT_MARGIN_DP = 2;


	Context context;
	ArrayList<WidgetItem> items;
	public CategoryStoreThreeItemWidget(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.context = context;
		setOrientation(HORIZONTAL);
		setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		setBackgroundResource(R.drawable.storefrontbackground);
		setLayoutParams(lp);
		setPadding(dpToPx(SCREEN_LEFT_PADDING_DP),dpToPx(SCREEN_TOP_PADDING_DP),dpToPx(SCREEN_RIGHT_PADDING_DP),
				dpToPx(SCREEN_BOTTOM_PADDING_DP));		
	}


	public CategoryStoreThreeItemWidget(Context context, ArrayList<WidgetItem> widgetItems) {
		super(context);
		this.context = context;
		setOrientation(HORIZONTAL);
		setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		setBackgroundResource(R.drawable.storefrontbackground);
		setLayoutParams(lp);
		setPadding(dpToPx(SCREEN_LEFT_PADDING_DP),dpToPx(SCREEN_TOP_PADDING_DP),dpToPx(SCREEN_RIGHT_PADDING_DP),
				dpToPx(SCREEN_BOTTOM_PADDING_DP));		
		this.items = widgetItems;
	}
	//to be used with widgetsitem arraylist
	public void drawContent(){
		for(int i=0;i<items.size();i++){
			JSONObject value = items.get(i).getValue();
			try{

				//screen size
				DisplayMetrics metrics = this.getResources().getDisplayMetrics();
				int width = metrics.widthPixels;
				String screenWidth = ""+width;

				JSONObject image = value.getJSONObject("image");
				String line1Text = value.getString("line1Text");

				String urlString = image.getString(screenWidth);
				StoreContent storeContent = new StoreContent(context, null);
				storeContent.addContent(urlString,line1Text);		
				storeContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
					}
				});
				addView(storeContent);	

			}
			catch (JSONException e) {
				System.out.println("json exception");
			}


		}
	}


	public void addContent(String json,String line1Text){

		ArrayList<String> imageUrlList = new ArrayList<String>();
		getImageUrlListFromJson(json,imageUrlList);
		StoreContent storeContent = null;
		for(int i=0;i<imageUrlList.size();i++){
			try{
				storeContent = new StoreContent(context, null);
				System.out.println("sfjf");
				storeContent.addContent(imageUrlList.get(i),line1Text);		

				addView(storeContent);		
				storeContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						System.out.println("three item");
					}
				});
			}catch (Exception e) {
			}
		}
	}
	public void getImageUrlListFromJson(String json, ArrayList<String> imageUrlList ){
		try{
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){				
				JSONObject obj = jsonArray.getJSONObject(i);				
				JSONObject value = obj.getJSONObject("value");				
				JSONObject image = value.getJSONObject("image");

				//screen size
				DisplayMetrics metrics = this.getResources().getDisplayMetrics();
				int width = metrics.widthPixels;
				String screenWidth = ""+width;
				String urlString = image.getString(screenWidth);	
				imageUrlList.add(urlString);
			}
		}catch (JSONException e) {
			System.out.println("json exception" + e);
		}
	}

	private class StoreContent extends LinearLayout {

		public StoreContent(Context context, AttributeSet attrs) {
			super(context, attrs);

			DisplayMetrics metrics = this.getResources().getDisplayMetrics();
			int screenWidth = metrics.widthPixels;
			int totalPaddingDp = SCREEN_LEFT_PADDING_DP + SCREEN_RIGHT_PADDING_DP + 3*(IMAGE_LEFT_MARGIN_DP + IMAGE_RIGHT_MARGIN_DP);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					(screenWidth-dpToPx(totalPaddingDp))/3,
					LayoutParams.WRAP_CONTENT);	
			lp.setMargins(dpToPx(IMAGE_LEFT_MARGIN_DP),0,dpToPx(IMAGE_RIGHT_MARGIN_DP),0);


			setLayoutParams(lp);
			setOrientation(LinearLayout.VERTICAL);
			setBackgroundColor(0x00000000);
			//setBackgroundColor(0xAAAAAAAA);
		}

		public void addContent(String imageUrl,String line1Text){
			try{
				LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams
						(dpToPx(95), dpToPx(117));

				//imageview
				ImageView iv=new ImageView(context);
				iv.setBackgroundColor(0x00000000);
				iv.setScaleType(ScaleType.FIT_CENTER);
				//this.imageUrl = imageUrl;
				setBitMapFromUrl(imageUrl , iv);
				addView(iv, imageParams);			

				//textview1
				LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams
						(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				textViewParams.setMargins(dpToPx(10),0,0,0);
				//textViewParams.setMargins(0, dpToPx(TEXT_VIEW_MARGIN_TOP_DP), 0, 0);
				TextView line1=new TextView(context);
				line1.setText(line1Text.toUpperCase()+"");
				line1.setTypeface(Typeface.DEFAULT_BOLD);
				line1.setLayoutParams(textViewParams);		

				//line1.setTextSize(TypedValue.COMPLEX_UNIT_SP,FONT_SIZE_SP);
				addView(line1);
			}catch (Exception e) {
				System.out.println("ashdbfasjndfasdas");
			}

		}
		public void setBitMapFromUrl(String url,ImageView iv){			
			CustomImageDownloader customImageDownloader = new CustomImageDownloader(context);
			customImageDownloader.download(url, iv);
		}
	}
	private int dpToPx(int dp)
	{
		float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
		return Math.round((float)dp * density);
	}
}
