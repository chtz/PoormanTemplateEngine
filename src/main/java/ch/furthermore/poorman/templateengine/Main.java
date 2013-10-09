package ch.furthermore.poorman.templateengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class Main {
	public static void main(String[] args) throws IOException {
		String propsFileName = System.getProperty("props");
		String inFileName = System.getProperty("in");
		String outFileName = System.getProperty("out");
		
		replacePlaceholders(propsFileName, inFileName, outFileName);
	}
		
	public static void replacePlaceholders(String propsFileName, String inFileName, String outFileName) throws IOException {
		Properties p = new Properties();
		File pf = new File(propsFileName);
		if (pf.exists()) {
			BufferedReader pr = new BufferedReader(new FileReader(pf));
			try {
				p.load(pr);
			}
			finally {
				pr.close();
			}
		}
				
		BufferedReader r = new BufferedReader(new FileReader(new File(inFileName)));
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(new File(outFileName)));
			try {
				Map<String, Object> placeholders = new HashMap<String, Object>();
				addAll(p, placeholders);
				addAll(System.getProperties(), placeholders);
				
				replacePlaceholders(r, w, placeholders);
			}
			finally {
				w.close();
			}
		}
		finally {
			r.close();
		}
	}

	private static void addAll(Properties p, Map<String, Object> placeholders) {
		for (Entry<Object, Object> e : p.entrySet()) {
			placeholders.put((String) e.getKey(), e.getValue());
		}
	}
	
	public static void replacePlaceholders(Reader r, Writer sw, Map<String,Object> placeholders) {
		Velocity.init();
		
		VelocityContext context = new VelocityContext();
		for (Entry<String, Object> e : placeholders.entrySet()) {
			context.put(e.getKey(), e.getValue());
		}
		
		Velocity.evaluate(context, sw, "", r);
	}
}
