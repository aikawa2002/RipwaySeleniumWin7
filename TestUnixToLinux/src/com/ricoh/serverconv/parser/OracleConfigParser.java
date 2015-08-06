/*
 * Copyright 2013 Stackify
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.ricoh.serverconv.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for Apache httpd configuration files.
 * 
 * <p>
 * This parser reads the configuration file line by line and builds a tree of the configuration.
 * 
 * @author jleacox
 * 
 */
public class OracleConfigParser implements ConfigParserInterface {
	private final String commentRegex = "#.*";
	private final String directiveRegex = "([ADDRESS\\s=|SERVICE_NAME\\s=].+)";
	private final String sectionOpenRegex = "^[^\\s](.+)[\\s]*=$";
	private final String sectionCloseRegex = "^$";

	private final Matcher commentMatcher = Pattern.compile(commentRegex).matcher("");
	private final Matcher directiveMatcher = Pattern.compile(directiveRegex).matcher("");
	private final Matcher sectionOpenMatcher = Pattern.compile(sectionOpenRegex).matcher("");
	private final Matcher sectionCloseMatcher = Pattern.compile(sectionCloseRegex).matcher("");

	public OracleConfigParser() {
	}

	/**
	 * Parses an Apache httpd configuration file into a configuration tree.
	 * 
	 * <p>
	 * Each directive will be a leaf node in the tree, while configuration sections will be nodes with additional child
	 * configuration nodes
	 * 
	 * @param inputStream
	 *            the configuration file
	 * @return a {@link ConfigNode tree} representing the Apache configuration
	 * @throws IOException
	 *             if there is an error reading the configuration
	 */
	@Override
	public ConfigNode parse(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new NullPointerException("inputStream: null");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		ConfigNode currentNode = ConfigNode.createRootNode();
		while ((line = reader.readLine()) != null) {
			if (commentMatcher.reset(line).find()) {
				if (!currentNode.isRootNode()) {
					currentNode = currentNode.getParent();
				}
				continue;
			} else if (sectionOpenMatcher.reset(line).find()) {
				String name = sectionOpenMatcher.group();
				String content = sectionOpenMatcher.group();
				int name_size = name.length();
				ConfigNode sectionNode = ConfigNode.createChildNode(name.substring(1, name_size - 1), content, currentNode);
				currentNode = sectionNode;
			} else if (sectionCloseMatcher.reset(line).find()) {
				if (!currentNode.isRootNode()) {
					currentNode = currentNode.getParent();
				}
			} else if (directiveMatcher.reset(line).find()) {
				String name = directiveMatcher.group();
				String content = directiveMatcher.group();
				ConfigNode.createChildNode(name.trim(), content.trim(), currentNode);
			} // TODO: Should an exception be thrown for unknown lines?
		}
		if (!currentNode.isRootNode()) {
			currentNode = currentNode.getParent();
		}

		return currentNode;
	}

}
