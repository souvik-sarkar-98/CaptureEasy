package app.techy10souvik.captureeasy.installer;

import java.io.IOException;

import app.techy10souvik.captureeasy.common.ui.AlertPopup;

public class Program {

	public static void main(String[] args) {
		CaptureEasyInstaller cei=new CaptureEasyInstaller();
		try {
			cei.install();
		} catch (IOException e) {
			e.printStackTrace();
			AlertPopup.init().type(AlertPopup.ERROR).message(e.getClass().getSimpleName()+" : "+e.getMessage()).button2("Okay");
		}
	}
}
