package antafes.mountaincoreTranslator;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class Configuration
{
    private static Configuration instance;
    public static final String PATH = System.getProperty("user.home") + "/.mctranslator/";
    private final Properties properties;
    private final File propertiesFile;

    /**
     * constructor
     */
    private Configuration()
    {
        this.propertiesFile = new File(PATH + "gui.xml");
        this.properties = new Properties();
    }

    /**
     * Create and return a singleton instance of the configuration object.
     *
     * @return Instance of the Configuration object
     */
    public static Configuration getInstance() {
        if (Configuration.instance == null) {
            Configuration.instance = new Configuration();
        }

        return Configuration.instance;
    }

    /**
     * load all saved properties
     */
    public void loadProperties()
    {
        if (this.propertiesFile.exists())
        {
            try
            {
                try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(this.propertiesFile.toPath()))) {
                    this.properties.loadFromXML(inputStream);
                }
            }
            catch (IOException ignored)
            {}
        }
        else
        {
            this.properties.setProperty("openDirPath", new File(PATH + "../Documents/").getPath());
            this.properties.setProperty("saveDirPath", new File(PATH + "../Documents/").getPath());
        }
    }

    /**
     * save all properties
     */
    public void saveProperties()
    {
        BufferedOutputStream outputStream;
        try
        {
            if (!this.propertiesFile.exists())
            {
                File path = new File(this.propertiesFile.getParent());
                path.mkdirs();
                this.propertiesFile.createNewFile();
            }

            outputStream = new BufferedOutputStream(Files.newOutputStream(this.propertiesFile.toPath()));
            this.properties.storeToXML(outputStream, null);
        }
        catch (IOException ignored)
        {}
    }

    /**
     * Get the "open" dir path.
     *
     * @return File object for the "open" dir path
     */
    public File getOpenDirPath()
    {
        return new File(this.properties.getProperty("openDirPath"));
    }

    /**
     * Get the save dir path including the new file.
     *
     * @param filename The file to save
     *
     * @return File object for the file that should be saved
     */
    public File getSaveDirPath(String filename)
    {
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        return new File(this.properties.getProperty("saveDirPath") + "/" + filename);
    }

    /**
     * Get the save dir path.
     *
     * @return A path
     */
    public File getSaveDirPath()
    {
        return new File(this.properties.getProperty("saveDirPath"));
    }

    /**
     * Get the windows location on screen.
     * This will default to 0:0 if no position is saved.
     *
     * @return A Point object with the x and y coordinates.
     */
    public Point getWindowLocation()
    {
        Point point = new Point(0, 0);
        String pointX = this.properties.getProperty("windowLocationX");
        String pointY = this.properties.getProperty("windowLocationY");

        if (pointX != null && !pointX.isEmpty() && pointY != null && !pointY.isEmpty())
        {
            point.setLocation(Double.parseDouble(pointX), Double.parseDouble(pointY));
        }

        return point;
    }

    /**
     * Get the extended state of the window. This correlates to the states from JFrame.
     * If nothing is found in the properties object JFrame.NORMAL is returned.
     */
    public int getExtendedState()
    {
        if (this.properties.getProperty("extendedState") == null || this.properties.getProperty("extendedState").equals("")) {
            return JFrame.NORMAL;
        }

        return Integer.parseInt(this.properties.getProperty("extendedState"));
    }

    /**
     * Set the open dir path.
     *
     * @param path The path used for opening files
     */
    public void setOpenDirPath(String path)
    {
        if (new File(path).isFile()) {
            path = new File(path).getParent();
        }

        this.properties.setProperty("openDirPath", path);
    }

    /**
     * Set the save dir path.
     *
     * @param path The path used for saving files
     */
    public void setSaveDirPath(String path)
    {
        if (new File(path).isFile())
            path = new File(path).getParent();

        this.properties.setProperty("saveDirPath", path);
    }

    /**
     * Set the windows position on the screen.
     *
     * @param point Position of the window
     */
    public void setWindowLocation(Point point)
    {
        this.properties.setProperty("windowLocationX", String.valueOf(point.getX()));
        this.properties.setProperty("windowLocationY", String.valueOf(point.getY()));
    }

    /**
     * Set the extended state of the window.
     */
    public void setExtendedState(int extendedState) {
        this.properties.setProperty("extendedState", Integer.toString(extendedState));
    }
}
