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
public class SendMailConfigParser implements ConfigParserInterface {
	private final String commentRegex = "#.*";
	private final String directiveRegex = "([^\\s]+)\\s*(.+)";
	private final String mcRegex = "([^\\s]+)\\(`(.+)'\\)*(.+)";
	private final String aliasesRegex = "([^\\s]+):[\\t\\s]*(.+[^,])$";
	private final String startaliasesRegex = "([^\\s]+):[\\t\\s]*(.+),$";
	private final String middlealiasesRegex = "^[\\s\\t][^:]*(.+),$";
	private final String endaliasesRegex = "^[\\s\\t][^:]*(.+)[^,]$";

	private final Matcher commentMatcher = Pattern.compile(commentRegex).matcher("");
	private final Matcher directiveMatcher = Pattern.compile(directiveRegex).matcher("");
	private final Matcher mcMatcher = Pattern.compile(mcRegex).matcher("");
	private final Matcher startaliasesMatcher = Pattern.compile(startaliasesRegex).matcher("");
	private final Matcher middlealiasesMatcher = Pattern.compile(middlealiasesRegex).matcher("");
	private final Matcher endaliasesMatcher = Pattern.compile(endaliasesRegex).matcher("");
	private final Matcher aliasesMatcher = Pattern.compile(aliasesRegex).matcher("");

	public SendMailConfigParser() {
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
				continue;
			} else if (mcMatcher.reset(line).find()) {
				String name = mcMatcher.group(1);
				String content = mcMatcher.group(2);
				ConfigNode.createChildNode(name, content, currentNode);
				continue;
			} else if (aliasesMatcher.reset(line).find()) {
				String name = aliasesMatcher.group(1);
				//String[] name = aliasesMatcher.group(1).split(":");
				String content = aliasesMatcher.group();
				ConfigNode.createChildNode(name, content, currentNode);
				continue;
			} else if (startaliasesMatcher.reset(line).find()) {
				String name = startaliasesMatcher.group(1);
				String content = startaliasesMatcher.group();
				ConfigNode sectionNode = ConfigNode.createChildNode(name, content, currentNode);
				currentNode = sectionNode;
				continue;
			} else if (middlealiasesMatcher.reset(line).find()) {
				String name = middlealiasesMatcher.group(1);
				String content = middlealiasesMatcher.group();
				ConfigNode.createChildNode(name, content, currentNode);
				continue;
			} else if (endaliasesMatcher.reset(line).find()) {
				String name = endaliasesMatcher.group(1);
				String content = endaliasesMatcher.group();
				ConfigNode.createChildNode(name, content, currentNode);
				currentNode = currentNode.getParent();
				continue;
			} else if (directiveMatcher.reset(line).find()) {
				String name = directiveMatcher.group();
				//String[] name = (directiveMatcher.group()).split("(");
				String content = directiveMatcher.group();
				
				
				ConfigNode.createChildNode(name, content, currentNode);
				//ConfigNode.createChildNode(name[0], content, currentNode);
			} // TODO: Should an exception be thrown for unknown lines?
		}

		return currentNode;
	}

}
