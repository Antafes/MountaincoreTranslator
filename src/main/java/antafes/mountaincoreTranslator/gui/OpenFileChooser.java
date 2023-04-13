package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.Configuration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFileChooser extends JFileChooser
{
    public OpenFileChooser()
    {
        Configuration configuration = Configuration.getInstance();
        this.setCurrentDirectory(configuration.getOpenDirPath());
        this.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
    }
}
