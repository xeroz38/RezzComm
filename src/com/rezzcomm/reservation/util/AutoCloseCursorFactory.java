package com.rezzcomm.reservation.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

/**
 * @author $Author$
 * @version $Revision$
 */
public class AutoCloseCursorFactory implements SQLiteDatabase.CursorFactory
{
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query)
    {
        return new AutoCloseCursor(db, masterQuery, editTable, query);
    }
}

class AutoCloseCursor extends SQLiteCursor
{
    private final SQLiteDatabase mDatabase;

    @SuppressWarnings("deprecation")
	public AutoCloseCursor(SQLiteDatabase database, SQLiteCursorDriver driver, String table, SQLiteQuery query)
    {
        super(database, driver, table, query);
        mDatabase = database;
    }

    @Override
    public void close()
    {
        super.close();

        mDatabase.close();
    }
}