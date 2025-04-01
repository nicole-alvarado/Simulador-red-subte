package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileUtil {

	public static final char DELETED = '*';
	public static final int SIZE_DATE = Integer.BYTES * 3;

	public static String readString(RandomAccessFile file, int length) throws IOException {
		char[] s = new char[length];
		for (int i = 0; i < s.length; i++)
			s[i] = file.readChar();
		return new String(s).replace('\0', ' ').trim();
	}

	public static void writeString(RandomAccessFile file, String s, int length) throws IOException {
		StringBuffer buffer = null;
		if (s != null)
			buffer = new StringBuffer(s);
		else
			buffer = new StringBuffer(length);
		buffer.setLength(length);
		file.writeChars(buffer.toString());
	}

	public static Date readDate(RandomAccessFile file) throws IOException {
		return new GregorianCalendar(file.readInt(), file.readInt(), file.readInt()).getTime();
	}

	public static void writeDate(RandomAccessFile file, Date date) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		file.writeInt(calendar.get(Calendar.YEAR));
		file.writeInt(calendar.get(Calendar.MONTH));
		file.writeInt(calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static void copyFile(String nameSource, String nameDest) throws IOException {
		File source = new File (nameSource);
		File dest = new File (nameDest);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}
