package com.example.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomHorizontalScrollView extends HorizontalScrollView{

	LinearLayout ll;
	Context context;

	public CustomHorizontalScrollView(Context context) {
		this(context, null);	
		this.context = context;
		
	}



	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context= context;
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		
		ll.setPadding(0,dpToPx(10),dpToPx(10), dpToPx(10));
		addView(ll);
		
		setLayoutParams(lp);
		setBackgroundResource(R.drawable.storefrontbackground);
	}

	//adding images
	public void addStoreFrontImage(int imageId,String line1Text,String line2Text){


		StoreFrontImage storeFrontImage = new StoreFrontImage(context, null);
		storeFrontImage.addStoreFrontImage(imageId,line1Text,line2Text);		
		storeFrontImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("asdjfajs");

			}
		});
		ll.addView(storeFrontImage);		
	}
	private int dpToPx(int dp)
	{
	    float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}

	private class StoreFrontImage extends LinearLayout {

		public StoreFrontImage(Context context, AttributeSet attrs) {
			super(context, attrs);
			setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					0,
					LayoutParams.WRAP_CONTENT,1f);			
			
			setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(10) );
			//setPadding(5, 5, 5, 10);
			lp.setMargins(10,0, 0,0);
			setLayoutParams(lp);
			setBackgroundColor(Color.WHITE);

		}
		private int dpToPx(int dp)
		{
		    float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
		    return Math.round((float)dp * density);
		}
		public void addStoreFrontImage(int imageId,String line1Text,String line2Text){
			LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams
					(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			ImageView iv=new ImageView(context);
			iv.setScaleType(ScaleType.CENTER);
			iv.setImageResource(imageId);
			addView(iv, imageParams);			
			
			TextView line1=new TextView(context);
			line1.setText(line1Text);
			line1.setTypeface(Typeface.DEFAULT_BOLD);
			addView(line1);

			TextView line2=new TextView(context);
			line2.setText(line2Text);
			line2.setTypeface(Typeface.DEFAULT_BOLD);
			addView(line2);
		}	
	}
}