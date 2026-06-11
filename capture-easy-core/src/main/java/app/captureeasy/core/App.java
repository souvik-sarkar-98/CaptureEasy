package app.captureeasy.core;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose 
 */
public interface App {
	
	void init() throws Exception;
	void launch() throws Exception;
	void handleError();

}
