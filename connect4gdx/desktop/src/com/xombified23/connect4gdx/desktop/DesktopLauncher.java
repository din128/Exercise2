package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Connect4";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new ProjectApplication(), config);
    }
}
