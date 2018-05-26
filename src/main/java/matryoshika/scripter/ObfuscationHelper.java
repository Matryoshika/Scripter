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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ObfuscationHelper {

	private static final String defaultMappings = "20171003";

	private static Map<String, String> mappings = new HashMap<String, String>();

	public void verifyMappings() {
		File fields = new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "fields" + ".csv");
		File methods = new File(Scripter.dir, "config" + File.separator + "Scripter" + File.separator + "methods" + ".csv");

		if (!fields.exists() || !methods.exists())
			makeDefaultFiles();
	}

	private void makeDefaultFiles() {
		String zipUrl = "http://export.mcpbot.bspk.rs/mcp_snapshot_nodoc/" + defaultMappings + "-1.12/mcp_snapshot_nodoc-" + defaultMappings + "-1.12.zip";
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
						mappings.put(split[1], split[0]);
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getObfuscated(String fullClassName) {
		if (!fullClassName.contains("net.minecraft") || !fullClassName.contains("com.mojang"))
			return fullClassName;

		String[] split = fullClassName.split(".");
		String notWanted = split[split.length - 1];
		String wanted = mappings.get(notWanted);

		String obfuscated = "";
		for (int i = 0; i < split.length - 2; i++)
			obfuscated += split[i] + ".";

		obfuscated += wanted;

		return obfuscated;
	}

}
