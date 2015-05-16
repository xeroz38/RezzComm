package com.rezzcomm.reservation.controller.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.rezzcomm.reservation.model.BundleInformation.FavoriteColumns;
import com.rezzcomm.reservation.util.AutoCloseCursorFactory;

public class RezzCommProvider extends ContentProvider
{
	private final String TABLE_FAVORITE = "favorite";
	private final int REZZCOMM_DB_VERSION = 1;

	public class REZZCOMMSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public REZZCOMMSQLiteOpenHelper(Context context, SQLiteDatabase.CursorFactory cursorFactory) {
			super(context, "REZZ COMM", cursorFactory, REZZCOMM_DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITE + " ("
					+ FavoriteColumns.DOCTOR_ID + " TEXT PRIMARY KEY,"
					+ FavoriteColumns.SPECIALTY_ID + " TEXT,"
					+ FavoriteColumns.SPECIALTY_NAME + " TEXT,"
					+ FavoriteColumns.REASON_FOR_VISIT_ID + " TEXT,"
					+ FavoriteColumns.DATE + " TEXT,"
					+ FavoriteColumns.SPECIALTY + " TEXT,"
					+ FavoriteColumns.DOCTOR_NAME + " TEXT,"
					+ FavoriteColumns.DOCTOR_PROFILE_IMAGE + " TEXT,"
					+ FavoriteColumns.DOCTOR_PROFFESIONAL_STATEMENT + " TEXT,"
					+ FavoriteColumns.DOCTOR_SPECIALTY + " TEXT,"
					+ FavoriteColumns.PLACE_ID + " TEXT,"
					+ FavoriteColumns.PLACE_ADDRESS + " TEXT,"
					+ FavoriteColumns.PLACE_NAME + " TEXT,"
					+ FavoriteColumns.TIMESLOT_ID + " TEXT,"
					+ FavoriteColumns.DOCTOR_EDUCATION + " TEXT,"
					+ FavoriteColumns.DOCTOR_AWARDS + " TEXT,"
					+ "UNIQUE (" + FavoriteColumns.DOCTOR_ID + ") ON CONFLICT REPLACE)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITE + " ("
					+ FavoriteColumns.DOCTOR_ID + " TEXT PRIMARY KEY AUTOINCREMENT,"
					+ FavoriteColumns.SPECIALTY_ID + " TEXT,"
					+ FavoriteColumns.SPECIALTY_NAME + " TEXT,"
					+ FavoriteColumns.REASON_FOR_VISIT_ID + " TEXT,"
					+ FavoriteColumns.DATE + " TEXT,"
					+ FavoriteColumns.SPECIALTY + " TEXT,"
					+ FavoriteColumns.DOCTOR_NAME + " TEXT,"
					+ FavoriteColumns.DOCTOR_PROFILE_IMAGE + " TEXT,"
					+ FavoriteColumns.DOCTOR_PROFFESIONAL_STATEMENT + " TEXT,"
					+ FavoriteColumns.DOCTOR_SPECIALTY + " TEXT,"
					+ FavoriteColumns.PLACE_ID + " TEXT,"
					+ FavoriteColumns.PLACE_ADDRESS + " TEXT,"
					+ FavoriteColumns.PLACE_NAME + " TEXT,"
					+ FavoriteColumns.TIMESLOT_ID + " TEXT,"
					+ FavoriteColumns.DOCTOR_EDUCATION + " TEXT,"
					+ FavoriteColumns.DOCTOR_AWARDS + " TEXT,"
					+ "UNIQUE (" + FavoriteColumns.DOCTOR_ID + ") ON CONFLICT REPLACE)");
		}
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sort)
	{
		REZZCOMMSQLiteOpenHelper openHelper = new REZZCOMMSQLiteOpenHelper(getContext(), new AutoCloseCursorFactory());
		SQLiteDatabase db = openHelper.getWritableDatabase();

		if (sort == null){
			sort = FavoriteColumns.DOCTOR_ID + " DESC";
		}

		return db.query(TABLE_FAVORITE, columns, selection, selectionArgs, null, null, sort);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		REZZCOMMSQLiteOpenHelper openHelper = new REZZCOMMSQLiteOpenHelper(getContext(), null);
		SQLiteDatabase db = openHelper.getWritableDatabase();

		db.beginTransaction();

		db.insert(TABLE_FAVORITE, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();

		String favoriteId = values.getAsString(FavoriteColumns.DOCTOR_ID);

		return FavoriteColumns.CONTENT_URI.buildUpon().appendPath(favoriteId).build();
	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		REZZCOMMSQLiteOpenHelper openHelper = new REZZCOMMSQLiteOpenHelper(getContext(), null);
		SQLiteDatabase db = openHelper.getWritableDatabase();

		db.beginTransaction();

		db.delete(TABLE_FAVORITE, selection, selectionArgs);

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();

		return 1;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
