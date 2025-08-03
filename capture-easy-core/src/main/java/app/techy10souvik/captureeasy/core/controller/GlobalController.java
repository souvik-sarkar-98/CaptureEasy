package app.techy10souvik.captureeasy.core.controller;

public class GlobalController {
    public static void registerControllers() throws Exception {
       new ControlWindowController();
       new BackgroundController();
    }
}
