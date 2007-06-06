/*
 * Copyright 2002-2005 the original author or authors.
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

package info.jtrac.maven;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @goal generate
 * @requiresDependencyResolution runtime
 * @description ant integration properties file generator
 */
public class GenerateMojo extends AntPropsMojo {
	
	/**
	 * generate properties file with dependency information, ready to use by ant
	 */
	protected void generate() throws Exception {
		OutputStream os = new FileOutputStream("build-deps.properties");
		Writer out = new PrintWriter(os);
		Date date = new Date();
		out.write("# *** generated by the AntProps Maven2 plugin: " + date + " ***\n\n");
		Set allPaths = new TreeSet(); // collect all paths here
		//==========================================================		
		for (Iterator i = buildProperties.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			out.write(entry.getKey() + "=" + entry.getValue() + "\n\n");
		}
		//===========================================================
		out.write("runtime.jars=");	
		for (Iterator i = runtimeFiles.iterator(); i.hasNext(); ) {			
			String path = (String) i.next();
			out.write("\\\n    " + path + ",");
		}
		out.write("\n\n");
		//===========================================================
		out.write("test.jars=");
		for (Iterator i = testClassPaths.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();			
			String key = (String) entry.getKey();
			Set paths = (Set) entry.getValue();
			for (Iterator j = paths.iterator(); j.hasNext(); ) {
				String path = (String) j.next();
				String fullPath = "${" + key + "}/" + path;
				out.write("\\\n    " + fullPath + ":");
				if(key.equals("m2.repo")) {
					allPaths.add(path);
				}
			}
		}
		out.write("\n\n");
		//===============================================================
		for (Iterator i = extraClassPaths.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			String name = (String) entry.getKey();
			boolean isFileset = filesets.contains(name);
			out.write(name + "=");
			Set paths = (Set) entry.getValue();
			for (Iterator j = paths.iterator(); j.hasNext(); ) {
				String path = (String) j.next();
				String fullPath = "${m2.repo}/" + path; 					
				if (isFileset) {
					out.write("\\\n    " + path + ",");
				} else {
					out.write("\\\n    " + fullPath + ":");
				}
				allPaths.add(path);
			}
			out.write("\n\n");
		}
		//===============================================================		
		out.write("all.jars=");
		for (Iterator i = allPaths.iterator(); i.hasNext(); ) {
			String path = (String) i.next();
			out.write("\\\n    " + path + ",");
		}
		out.write("\n\n");
		//===============================================================
		out.close();
		os.close();
	}	
	
}