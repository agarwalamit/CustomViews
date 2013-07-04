package com.example.customviews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class CustomImageDownloader {

	static LruCache<String, Bitmap> imageCache;

	Map<String, BitmapDownloaderTask> ongoingDownloads = null;

	Map<String, BitmapDownloaderTask> ongoingFileLoads = null;

	OnAllDownloadCompletedListner allDownloadCompletedListner = null;

	public CustomImageDownloader(Context context){
		//imageCache = new HashMap<String, SoftReference<Bitmap>>();

		int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 8;
		if(imageCache == null){
			imageCache = new LruCache<String, Bitmap>(cacheSize);
		}
		ongoingDownloads = new HashMap<String, BitmapDownloaderTask>();
	}

	public void download(String url, Context context){
		String filename = String.valueOf(url.hashCode());
		File f = new File(getCacheDirectory(context), filename);

		if(imageCache.get(f.getPath()) == null){
			BitmapDownloaderTask downloadTask = new BitmapDownloaderTask(context);
			downloadTask.execute(url);
			ongoingDownloads.put(url, downloadTask);
		}
	}

	public void download(String url, Context context, OnDownloadCompletedListner listner){
		String filename = String.valueOf(url.hashCode());
		File f = new File(getCacheDirectory(context), filename);

		if(imageCache.get(f.getPath()) == null){
			BitmapDownloaderTask downloadTask = new BitmapDownloaderTask(context, listner);
			downloadTask.execute(url);
			ongoingDownloads.put(url, downloadTask);
		}
	}

	public void download(String url,final ImageView imageView) {
		if(url == null || url.length() == 0) return;

		if(isDownloadingAlready(url, imageView)){
			return;
		}

		String filename = String.valueOf(url.hashCode());
		File f = new File(getCacheDirectory(imageView.getContext()), filename);

		Bitmap bitmap = imageCache.get(f.getPath());

		if(bitmap == null){
			OnDownloadCompletedListner listner = new OnDownloadCompletedListner() {

				@Override
				public void onDownloadCompleted(Bitmap bitmap) {
					//System.out.println(" downloaded completed!! ");
					imageView.setImageBitmap(bitmap);
				}
			};

			imageView.setImageDrawable(new DownloadedDrawable(url));

			if(ongoingDownloads.containsKey(url)){
				BitmapDownloaderTask task = ongoingDownloads.get(url);
				task.setOnDownloadCompletedListner(listner);
			}
			else{
				BitmapDownloaderTask downloadTask = new BitmapDownloaderTask(imageView.getContext(), listner);
				if(Build.VERSION.SDK_INT >= 11){
					downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
				}
				else{
					downloadTask.execute(url);
				}
				ongoingDownloads.put(url, downloadTask);
			}
			//System.out.println("Ongoing downloads count is " + ongoingDownloads.size());
		}
		else{
			imageView.setImageBitmap(bitmap);
		}
	}

	public void setOnAllDownloadsCompletedListner(OnAllDownloadCompletedListner listner){
		this.allDownloadCompletedListner = listner;
	}

	public int getDownloadPoolSize(){
		return ongoingDownloads.size();
	}

	public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

		OnDownloadCompletedListner listner = null;

		Context context = null;

		String url = null;

		public BitmapDownloaderTask(Context context){
			this.context = context;
		}

		public BitmapDownloaderTask(Context context, OnDownloadCompletedListner listner){
			this.listner = listner;
			this.context = context;
		}

		public void setOnDownloadCompletedListner(OnDownloadCompletedListner listner){
			this.listner = listner;
		}

		@Override
		protected void onPostExecute(final Bitmap bitmap) {
			System.out.println("Download completed!!...");
			if(listner != null){
				listner.onDownloadCompleted(bitmap);
			}
			else{
				System.out.println("listner is null");
			}
			ongoingDownloads.remove(url);
			//System.out.println("Ongoing downloads count is " + ongoingDownloads.size());

			if(ongoingDownloads.size() == 0){
				if(allDownloadCompletedListner != null){
					allDownloadCompletedListner.onAllDownloadCompleted();
				}
			}
		}


		@Override
		protected Bitmap doInBackground(String... arg0) {
			//System.out.println("Download started");
			Bitmap bitmap = null;

			this.url = arg0[0];

			String filename = String.valueOf(url.hashCode());
			File f = new File(getCacheDirectory(context), filename);

			if(f.exists()){
				bitmap = BitmapFactory.decodeFile(f.getPath());
				if(bitmap != null){
					imageCache.put(f.getPath(), bitmap);
				}
				else{
					System.out.println(f.getPath() + " "+ bitmap);
				}
			}
			else{
				bitmap = downloadBitmap(url, context);
			}

			return bitmap;
		}
	}

	private boolean isDownloadingAlready(String url, ImageView imageView){
		Drawable drawable = imageView.getDrawable();

		if(drawable instanceof DownloadedDrawable){
			DownloadedDrawable downloadDrawable = (DownloadedDrawable)drawable;
			if(downloadDrawable.getUrl().equals(url)){
				return true;
			}
			else{
				if(ongoingDownloads.containsKey(downloadDrawable.getUrl())){
					//System.out.println("ongoing download for url");
					BitmapDownloaderTask task = ongoingDownloads.get(downloadDrawable.getUrl());
					task.setOnDownloadCompletedListner(null);
				}
			}
		}
		return false;
	}

	private File getCacheDirectory(Context context){
		String sdState = android.os.Environment.getExternalStorageState();
		File cacheDir;

		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();  
			cacheDir = new File(sdDir,"data/fka/images");
		}
		else
			cacheDir = context.getCacheDir();

		if(!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir;
	}

	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(f);
			if(bmp == null){
				//System.out.println("bmp is nullllllllllllllllll");
			}
			bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally { 
			try { if (out != null ) out.close(); }
			catch(Exception ex) {} 
		}
	}

	private Bitmap downloadBitmap(String url, Context context) {

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);

		HttpClient client = new DefaultHttpClient(params);
		
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) { 
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
				return null;
			}
			
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					System.out.println("downn...");

					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					if(bitmap != null){
						//SoftReference<Bitmap> bitmapRef = new SoftReference<Bitmap>(bitmap);
						String filename = String.valueOf(url.hashCode());
						File f = new File(getCacheDirectory(context), filename);
						imageCache.put(f.getPath(), bitmap);

						BitmapFileWriterTask fileWriterTask = new BitmapFileWriterTask(bitmap, f);
						fileWriterTask.execute();
					}
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();  
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
		} finally {
			if (client != null) {
				//client.close();
			}
		}
		return null;
	}

	public class BitmapFileWriterTask extends AsyncTask<String, Void, Boolean> {

		private File file;
		private Bitmap bitmap;

		public BitmapFileWriterTask(Bitmap bitmap, File f){
			this.file = f;
			this.bitmap = bitmap;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try{
				writeFile(bitmap, file);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	class DownloadedDrawable extends ColorDrawable {

		private String url = null;

		public DownloadedDrawable(String url) {
			super(0xFFAAAAAA);
			this.url = url;
		}

		public String getUrl() {
			return url;
		}
	}


	private interface OnDownloadCompletedListner{

		public void onDownloadCompleted(Bitmap bitmap);		

	}

	public interface OnAllDownloadCompletedListner{

		public void onAllDownloadCompleted();

	}

}
