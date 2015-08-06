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
public class SystemInfoParser implements ConfigParserInterface  {
	private final String commentRegex = "#.*";
	private final String commentRegex2 = "---.*";
	private final String sectionOpenRegex = "^===.*===$";
	private final String sectionCloseRegex = ".*:";
	private final String directiveRegex = "([^\\s]+).*=.*";
	private final String hostsRegex = "([^\\s]+)\\s*(.+)";
	private final String hostsallowRegex = "([^\\s]+).*:\\s*(.+)";

	private final Matcher commentMatcher = Pattern.compile(commentRegex).matcher("");
	private final Matcher commentMatcher2 = Pattern.compile(commentRegex2).matcher("");
	private final Matcher directiveMatcher = Pattern.compile(directiveRegex).matcher("");
	private final Matcher hostsMatcher = Pattern.compile(hostsRegex).matcher("");
	private final Matcher hostsallowMatcher = Pattern.compile(hostsallowRegex).matcher("");
	private final Matcher sectionOpenMatcher = Pattern.compile(sectionOpenRegex).matcher("");
	private final Matcher sectionCloseMatcher = Pattern.compile(sectionCloseRegex).matcher("");

	public SystemInfoParser() {
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
			if (commentMatcher.reset(line).find() || commentMatcher2.reset(line).find()) {
				continue;
			} else if (directiveMatcher.reset(line).find()) {
				String[] name = directiveMatcher.group().split("=");
				if (name.length < 2) continue;
				ConfigNode.createChildNode(name[0], name[1], currentNode);
				continue;
			} else if (hostsMatcher.reset(line).find()) {
				String name = hostsMatcher.group(1);
				String content = hostsMatcher.group(2);
				ConfigNode.createChildNode(name, content, currentNode);
			} else if (hostsallowMatcher.reset(line).find()) {
				String name = hostsallowMatcher.group(1);
				String content = hostsallowMatcher.group(2);
				ConfigNode.createChildNode(name, content, currentNode);
			} // TODO: Should an exception be thrown for unknown lines?
		}

		return currentNode;
	}

}
