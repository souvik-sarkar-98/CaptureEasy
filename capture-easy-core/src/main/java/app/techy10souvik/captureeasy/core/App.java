package app.techy10souvik.captureeasy.core;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.jnativehook.NativeHookException;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose 
 */
public interface App {
	
	void init() throws ConfigurationException, IOException;
	void launch() throws ConfigurationException, IOException, NativeHookException;
	void handleError();

}
