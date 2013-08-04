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

public class CategoryStoreTwoCardWidget extends LinearLayout{

	private final int SCREEN_LEFT_PADDING_DP = 5;
	private final int SCREEN_TOP_PADDING_DP = 10;
	private final int SCREEN_RIGHT_PADDING_DP = 5;
	private final int SCREEN_BOTTOM_PADDING_DP = 10;

	private final int CONTENT_LEFT_PADDING_DP = 5;
	private final int CONTENT_TOP_PADDING_DP = 5;
	private final int CONTENT_RIGHT_PADDING_DP = 5;
	private final int CONTENT_BOTTOM_PADDING_DP = 5;

	
	private final int IMAGE_LEFT_MARGIN_DP = 5;
	private final int IMAGE_RIGHT_MARGIN_DP = 5;

	private final int TEXT_VIEW_MARGIN_TOP_DP = 5;
	private final int FONT_SIZE_SP = 12;

	Context context;
	ArrayList<WidgetItem> items;
	public CategoryStoreTwoCardWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOrientation(HORIZONTAL);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
	
		setLayoutParams(lp);
		setBackgroundResource(R.drawable.storefrontbackground);
		setPadding(dpToPx(SCREEN_LEFT_PADDING_DP),dpToPx(SCREEN_TOP_PADDING_DP),dpToPx(SCREEN_RIGHT_PADDING_DP),dpToPx(SCREEN_BOTTOM_PADDING_DP));
		

	}


	public CategoryStoreTwoCardWidget(Context context, ArrayList<WidgetItem> widgetItems) {
		super(context);
		setOrientation(HORIZONTAL);
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

				System.out.println("scwidth"+screenWidth);
				JSONObject image = value.getJSONObject("image");
				String line1Text = value.getString("line1Text");

				String urlString = image.getString(screenWidth);
				StoreContent storeContent = new StoreContent(context, null);
				storeContent.addContent(urlString,line1Text);		
				storeContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						System.out.println("2card");
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
			System.out.println("asdfmnas");
			try{
				storeContent = new StoreContent(context, null);
				storeContent.addContent(imageUrlList.get(i),line1Text);		

				addView(storeContent);		
				storeContent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("two card");
					}
				});
			}catch (Exception e) {
				System.out.println("smbfashb"+e);
				if(storeContent == null)
					System.out.println("nulllllllllllll");
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

		int contentWidthPx;
		public StoreContent(Context context, AttributeSet attrs) {
			super(context, attrs);

			DisplayMetrics metrics = this.getResources().getDisplayMetrics();
			int screenWidth = metrics.widthPixels;
			System.out.println("scwidth"+screenWidth+" den="+metrics.densityDpi+" den="+metrics.density+" scaled den="+metrics.scaledDensity);
			int totalPaddingDp = 2*(IMAGE_LEFT_MARGIN_DP + IMAGE_RIGHT_MARGIN_DP) + SCREEN_LEFT_PADDING_DP + SCREEN_RIGHT_PADDING_DP;
			contentWidthPx = (screenWidth-dpToPx(totalPaddingDp))/2;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					contentWidthPx,
					LayoutParams.WRAP_CONTENT);	
			lp.setMargins(dpToPx(IMAGE_LEFT_MARGIN_DP),0,dpToPx(IMAGE_RIGHT_MARGIN_DP),0);
			setPadding(dpToPx(CONTENT_LEFT_PADDING_DP), dpToPx(CONTENT_TOP_PADDING_DP),
					dpToPx(CONTENT_RIGHT_PADDING_DP),dpToPx(CONTENT_BOTTOM_PADDING_DP));
			
			setLayoutParams(lp);
			setOrientation(LinearLayout.VERTICAL);
			setBackgroundColor(Color.WHITE);
			//setBackgroundColor(0xAAAAAAAA);
		}

		public void addContent(String imageUrl,String line1Text){
			
			try{
				int imageHeightPx = (int)(((float)320/(float)720)*134);
				System.out.println("ht="+imageHeightPx);
				LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams
						(contentWidthPx - dpToPx(CONTENT_LEFT_PADDING_DP + CONTENT_RIGHT_PADDING_DP), imageHeightPx);

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
				textViewParams.setMargins(dpToPx(CONTENT_LEFT_PADDING_DP),dpToPx(CONTENT_LEFT_PADDING_DP),0,0);
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
		System.out.println("dptopx density="+density);
		return Math.round((float)dp * density);
	}
}
