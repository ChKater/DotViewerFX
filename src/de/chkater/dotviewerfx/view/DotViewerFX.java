/*
 * Copyright (C) 2014 Christian Kater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.chkater.dotviewerfx.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Display a graph in dot notation as Image by using the dot command line
 * program. Additional flags can be set (see <a
 * href="http://www.graphviz.org/doc/info/command.html"
 * >http://www.graphviz.org/doc/info/command.html</a>). You may not use the
 * flags -T and -o. Otherwise the correctness of this class is not guaranteed.
 * You have to set the Path to your dot program. If you dont set the path the
 * following paths are default: Windows: c:/Program Files
 * (x86)/Graphviz/bin/dot.exe Mac: /usr/local/bin/dot All other Systems:
 * /usr/bin/dot
 * 
 * @author Christian Kater
 *
 */
public class DotViewerFX extends ImageView {

	/**
	 * Default path to the dot programm
	 */
	public static String DOT_PATH = null;

	private static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Used path to the dot program
	 */
	private String dotPath;

	/**
	 * FLags for the dot programm (see <a
	 * href="http://www.graphviz.org/doc/info/command.html"
	 * >http://www.graphviz.org/doc/info/command.html</a>).
	 */
	private String flags = "";

	/**
	 * Instanciate a new DotViewerFX object with nothing to display. If DOT_PATH
	 * is not set the following paths are used: Windows: c:/Program Files
	 * (x86)/Graphviz/bin/dot.exe Mac: /usr/local/bin/dot All other Systems:
	 * /usr/bin/dot
	 */
	public DotViewerFX() {
		super();
		if (DOT_PATH != null) {
			dotPath = DOT_PATH;
		} else {
			if (OS.indexOf("win") >= 0) {
				dotPath = "c:/Program Files (x86)/Graphviz/bin/dot.exe";
			} else if (OS.indexOf("mac") >= 0) {
				dotPath = "/usr/local/bin/dot";
			} else {
				dotPath = "/usr/bin/dot";
			}
		}
	}

	private void checkError(InputStream error) throws IOException, DotException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(error));
		String errormsg = bf.readLine();
		bf.close();
		if (errormsg != null) {
			throw new DotException(errormsg);
		}
	}

	public String getDotPath() {
		return dotPath;
	}

	public String getFlags() {
		return flags;
	}

	public void setDotPath(String dotPath) {
		this.dotPath = dotPath;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}


	/**
	 * Set the displayed Image to the rendered image of the graph in the given file. 
	 * @param dotFile file with graph in dot notation
	 * @throws IOException 
	 * @throws DotException
	 */
	public void setImage(File dotFile) throws IOException, DotException {
		Process process = Runtime.getRuntime().exec(
				dotPath + " -Tpng " + flags + " " + dotFile.getAbsolutePath());
		checkError(process.getErrorStream());
		setImage(process.getInputStream());
	}

	private void setImage(InputStream imgStream) throws IOException {
		Image image = new Image(imgStream);
		super.setImage(image);
		imgStream.close();
	}
	
	/**
	 * Set the displayed Image to the rendered image of the graph in the given string. 
	 * @param dotFile string with graph in dot notation
	 * @throws IOException 
	 * @throws DotException
	 */
	public void setImage(String dot) throws IOException, DotException {
		Process process = Runtime.getRuntime()
				.exec(dotPath + " -Tpng " + flags);
		// dot read from stdin if no input file is given
		OutputStream stdin = process.getOutputStream();
		stdin.write(dot.getBytes());
		stdin.flush();
		stdin.close();
		checkError(process.getErrorStream());
		setImage(process.getInputStream());
	}
	
	/**
	 * Exception class for any error from the dot program.
	 * 
	 * @author Christian Kater
	 *
	 */
	public static class DotException extends Exception {

		public DotException(String message) {
			super(message);
		}

	}

}