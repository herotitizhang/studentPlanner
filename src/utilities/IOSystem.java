package utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.ScheduleI;

public class IOSystem {

	/**
	 * Load from the local drive 
	 * @param fileName
	 * @return
	 */
	public static ScheduleI loadFromDisk(String fileName) {
		FileInputStream fileIn = null;
		BufferedInputStream bufferedIn = null;
		DataInputStream dataIn = null;
		try {
			fileIn = new FileInputStream(fileName+".SAV");
			bufferedIn = new BufferedInputStream(fileIn);
			dataIn = new DataInputStream(bufferedIn);

			File file = new File (fileName+".SAV");
			byte[] toBeConverted = new byte[file.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) file.length()];
			dataIn.readFully(toBeConverted);
			
			Object obj = getObject(toBeConverted);
			if (obj instanceof ScheduleI) { // TODO not sure if it can do type checking
				return (ScheduleI) obj;
			} else {
				return null;
			}
			
			// an alternative method. use this if the current way of getting byte array doesn't work
//			int nRead;
//			ByteArrayOutputStream buffer = null;
//			byte[] data = new byte[8388608]; // allocate 8MB in memory
//			buffer = new ByteArrayOutputStream();
//			while ((nRead = bufferedIn.read(data, 0, data.length)) != -1) {
//				buffer.write(data, 0, nRead); // not sure if it overwrites the previous data
//			}
//
//			buffer.flush();
		
		
		} catch (FileNotFoundException e) {
			System.out.println("The save file is not found!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeCloseable(fileIn);
			closeCloseable(bufferedIn);
			closeCloseable(dataIn);
		}
		
		
		return null;
	}
	
	/**
	 * Write to the local drive
	 * @param schedule
	 * @param fileName
	 */
	public static void writeToDisk(ScheduleI schedule, String fileName) {
		FileOutputStream fileOut = null;
		BufferedOutputStream bufferedOut = null;
		try {
			fileOut = new FileOutputStream(fileName+".SAV");
			bufferedOut = new BufferedOutputStream(fileOut);
			bufferedOut.write(getByteArray(schedule));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
//			closeCloseable(fileOut);
			closeCloseable(bufferedOut); // TODO we need to close either fileOut or bufferedOut but not both (don't understand why)
		}
	}
	
	public static ScheduleI loadFromServer() {
		
		return null;
	}

	public static void writeToServer(ScheduleI schedule) {
		
	}
	
	// TODO not sure if it's the correct method header
	public static void RequestAuthenticateCell() {
		
	}

	// TODO not sure if it's the correct method header
	public static String AuthentiateCell() {
		return null;
	}
	
	/**
	 * mostly close an inputstream or an outputstream
	 * @param toBeClosed
	 */
	public static void closeCloseable(Closeable toBeClosed) {
		if (toBeClosed != null) {
			try {
				toBeClosed.close();
			} catch (IOException e) {
				System.out.println(toBeClosed.getClass());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * convert an object to a byte array
	 * @param obj
	 * @return
	 */
	public static byte[] getByteArray(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeCloseable(oos);
		}
		
		return baos.toByteArray();
	}

	/**
	 * convert a byte array to an object
	 * @param byteArray
	 * @return
	 */
	public static Object getObject(byte[] byteArray) {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeCloseable(ois);
		}
		return null;
	}
	
}
