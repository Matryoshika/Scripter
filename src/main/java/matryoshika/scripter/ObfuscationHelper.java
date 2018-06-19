package matryoshika.scripter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import net.minecraft.launchwrapper.Launch;

public class ObfuscationHelper {

	private static final String defaultMappings = "20171003";

	private static Map<String, String> fields = new HashMap<String, String>();
	private static Map<String, String> methods = new HashMap<String, String>();
	
	private static Map<String, Field> reflectedFields = new HashMap<String, Field>();
	private static Map<String, Method> reflectedMethods = new HashMap<String, Method>();

	public void verifyMappings() {
		File fields = new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "fields" + ".csv");
		File methods = new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "methods" + ".csv");

		if (!fields.exists() || !methods.exists())
			makeDefaultFiles();
	}

	private void makeDefaultFiles() {
		String zipUrl = "http://export.mcpbot.bspk.rs/mcp_snapshot/" + defaultMappings + "-1.12/mcp_snapshot-" + defaultMappings + "-1.12.zip";
		try {
			URL url = new URL(zipUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			File f = new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "mappings" + ".zip");
			OutputStream stream = new FileOutputStream(f);
			copy(in, stream, 1024);

			stream.close();
			in.close();

			unzip(new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "mappings" + ".zip"), new File(Scripter.dir, "config" + File.separator + "Scripter"));

			new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "mappings" + ".zip").delete();
			new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "params" + ".csv").delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unzip(File zipFilePath, File destDirectory) throws IOException {
		;
		if (!destDirectory.exists()) {
			destDirectory.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null) {
			File filePath = new File(destDirectory, entry.getName());
			if (!entry.isDirectory()) {
				extractFile(zipIn, filePath);
			} else {
				filePath.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private void extractFile(ZipInputStream zipIn, File filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buf = new byte[bufferSize];
		int n = input.read(buf);
		while (n >= 0) {
			output.write(buf, 0, n);
			n = input.read(buf);
		}
		output.flush();
	}

	protected void readMappings() {
		String[] files = new String[] { "fields", "methods" };

		for (String s : files) {
			try {
				String line = "";
				BufferedReader reader = new BufferedReader(new FileReader(new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + s + ".csv")));
				while ((line = reader.readLine()) != null) {
					if (!line.equals("searge,name,side,desc")) {
						String[] split = line.split(",");
						if(s.equals("fields"))
							fields.put(split[1], split[0]);
						if(s.equals("methods"))
							methods.put(split[1], split[0]);
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getObfuscatedField(String name) {
		String obfuscated = fields.get(name);
		return obfuscated == null ? name : obfuscated;
	}
	
	public static String getObfuscatedMethod(String name) {
		String obfuscated = methods.get(name);
		return obfuscated == null ? name : obfuscated;
	}
	
	
	private static Object executeMethod(Object o, String methodname, Object...varargs) {
		String deobfuscated = methodname;
		if(!((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")).booleanValue())
			methodname = getObfuscatedMethod(methodname);
		
		
		Method method = reflectedMethods.get(methodname);
		Object toReturn = null;
		
		Class<?>[] classes = new Class<?>[varargs.length];
		for(int i = 0; i < varargs.length; i++) {
			classes[i] = varargs[i].getClass();
		}
		try {
			if(method == null) {
				method = MethodUtils.getAccessibleMethod(o.getClass(), methodname, classes);
				if(method == null) {
					Scripter.logger.warn("Tried to access non-existing method " + deobfuscated + " in class " + o.getClass().getName());
					return null;
				}
				reflectedMethods.put(methodname, method);
			}
			toReturn = method.invoke(o, varargs);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	private static Object getField(Object o, String fieldname) {
		String deobfuscated = fieldname;
		if(!((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")).booleanValue())
			fieldname = getObfuscatedField(fieldname);
		
		Field field = reflectedFields.get(fieldname);
		Object toReturn = null;
		if(field == null) {
			field = FieldUtils.getDeclaredField(o.getClass(), fieldname, true);
			if(field == null) {
				Scripter.logger.warn("Tried to access non-existing field " + deobfuscated + " in class " + o.getClass().getName());
				return null;
			}
			reflectedFields.put(fieldname, field);
			try {
				toReturn = field.get(o);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return toReturn;
	}
	
	private static boolean setField(Object o, String fieldname, Object value) {
		String deobfuscated = fieldname;
		if(!((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")).booleanValue())
			fieldname = getObfuscatedField(fieldname);
		
		Field field = reflectedFields.get(fieldname);
		if(field == null) {
			field = FieldUtils.getDeclaredField(o.getClass(), fieldname, true);
			if(field == null) {
				Scripter.logger.warn("Tried to access non-existing field " + deobfuscated + " in class " + o.getClass().getName());
				return false;
			}
			reflectedFields.put(fieldname, field);
		}
		
		try {
			field.set(o, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
