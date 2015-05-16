package com.rezzcomm.reservation.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rezzcomm.reservation.model.Path;

public class DownloadImageBitmap {

	public Bitmap bitmap;

	public DownloadImageBitmap(String profile_image) {
		try {
			InputStream is = (InputStream) new URL(Path.THUMBNAIL_URL + profile_image).getContent();
			Bitmap bm = BitmapFactory.decodeStream(is);
			double width = bm.getWidth()/(bm.getWidth()/100);
			double height = bm.getHeight()/(bm.getHeight()/100);

			bitmap = Bitmap.createScaledBitmap(bm, (int)width, (int)height, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
