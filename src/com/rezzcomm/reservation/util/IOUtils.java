package com.rezzcomm.reservation.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

/**
 * @author $Author$
 * @version $Revision$
 */
public class IOUtils
{
	public static String read(InputStream in) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] bytes = new byte[1024];
		int read;

		while (-1 != (read = in.read(bytes)))
		{
			baos.write(bytes, 0, read);
		}

		return baos.toString();
	}
	
	public static void close(Cursor query)
	{
		try
		{
			if (query != null)
			{
				query.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void flush(Flushable flushable)
	{
		try
		{
			if (flushable != null)
			{
				flushable.flush();
			}
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
	}

	public static void close(Closeable closeable)
	{
		try
		{
			if (closeable != null)
			{
				closeable.close();
			}
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	public static void antiClose(Activity act)
	{
		if (!act.isTaskRoot()) {
			final Intent intent = act.getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
					intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
				act.finish();
			}
		}
	}
}
