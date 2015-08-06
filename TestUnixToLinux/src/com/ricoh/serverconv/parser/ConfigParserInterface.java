package com.ricoh.serverconv.parser;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigParserInterface {

	public ConfigNode parse(InputStream inputStream) throws IOException;

}
