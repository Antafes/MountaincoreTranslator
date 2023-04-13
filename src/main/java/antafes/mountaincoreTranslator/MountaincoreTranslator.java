package antafes.mountaincoreTranslator;

import antafes.eventDispatcher.Application;
import antafes.mountaincoreTranslator.gui.BaseWindow;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MountaincoreTranslator extends Application
{
    public static void main(String[] args)
    {
        MountaincoreTranslator translator = new MountaincoreTranslator();
        translator.start();
    }

    public void start()
    {
        Configuration configuration = Configuration.getInstance();
        configuration.loadProperties();

        SwingUtilities.invokeLater(() -> {
            BaseWindow baseWindow = new BaseWindow();
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(
                MountaincoreTranslator.getResourceInJar("images/logo_128px.png")
            );
            baseWindow.setIconImage(img);
            baseWindow.setVisible(true);
        });
    }

    /**
     * Get a resource inside the generated JAR.
     *
     * @param path Path of the file
     *
     * @return URL object of the file
     */
    public static URL getResourceInJar(String path) {
        return MountaincoreTranslator.class.getClassLoader().getResource(path);
    }
}
