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
public class ProfileConfigParser extends CronConfigParser {
	protected final String profiledirectiveRegex = "^(.+)=(.+)";
	protected final Matcher profiledirectiveMatcher = Pattern.compile(profiledirectiveRegex).matcher("");

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
			} else if (sectionOpenMatcher.reset(line).find()) {
				if (!currentNode.isRootNode()) {
					currentNode = currentNode.getParent();
				}
				String name = sectionOpenMatcher.group(1);
				String content = sectionOpenMatcher.group(1);
				if (content == null) continue;
				ConfigNode sectionNode = ConfigNode.createChildNode(name, content, currentNode);
				currentNode = sectionNode;
			} else if (profiledirectiveMatcher.reset(line).find()) {
				String name = profiledirectiveMatcher.group(1);
				String content = profiledirectiveMatcher.group();
				ConfigNode.createChildNode(name, content, currentNode);
			} // TODO: Should an exception be thrown for unknown lines?
		}
		if (!currentNode.isRootNode()) {
			currentNode = currentNode.getParent();
		}

		return currentNode;
	}

}
