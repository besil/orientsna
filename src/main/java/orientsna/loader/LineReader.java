package orientsna.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class LineReader implements Iterable<String[]>{
	private final BufferedReader brd;
	private final String sep;
	
	public LineReader(BufferedReader brd, String sep) {
		this.brd = brd;
		this.sep = sep;
	}
	
	@Override
	public Iterator<String[]> iterator() {
		return new Iterator<String[]>() {
			String line;
			@Override
			public boolean hasNext() {
				try {
					return (line = brd.readLine()) != null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public String[] next() {
				return line.split(sep);
			}
		};
	}
}