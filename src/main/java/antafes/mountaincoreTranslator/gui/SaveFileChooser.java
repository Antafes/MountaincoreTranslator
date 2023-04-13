package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.Configuration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveFileChooser extends JFileChooser
{
    public SaveFileChooser()
    {
        Configuration configuration = Configuration.getInstance();
        this.setCurrentDirectory(configuration.getSaveDirPath());
        this.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
    }
}
