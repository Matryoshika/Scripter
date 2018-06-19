package matryoshika.scripter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Loader {

	public static List<File> scripts;
	public static Map<String, List<String>> mappedScripts = new HashMap<String, List<String>>();
	public static Map<String, String> scriptNames = new HashMap<String, String>();

	public static void loadScripts(File directory) {
		if (!directory.exists())
			directory.mkdirs();

		scripts = new ArrayList<File>();
		FileUtils.listFiles(directory, new String[] { "js" }, true).forEach(scripts::add);

		mapScripts();
	}

	public static void mapScripts() {
		for (File file : scripts) {
			if (!file.getName().endsWith("js") || !file.getName().contains("scripter")) {
				Scripter.logger.info("Skipping over " + file.getName());
				continue;
			}

			String txt = "";
			String script = "";
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				txt = br.readLine();
				Scanner scanner = new Scanner(file);
				script = scanner.useDelimiter("\\Z").next();
				scanner.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			txt = txt.replace("//", "");

			if (!mappedScripts.containsKey(txt))
				mappedScripts.put(txt, new ArrayList<String>());

			mappedScripts.get(txt).add(script);
			scriptNames.put(script, file.getName());
			Scripter.logger.info("Adding Script: " + file.getName());
		}
	}

	public static String getScriptName(String script) {
		return scriptNames.get(script);
	}
}
