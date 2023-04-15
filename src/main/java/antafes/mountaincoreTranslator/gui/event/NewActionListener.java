package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.Configuration;
import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import antafes.mountaincoreTranslator.gui.OpenFileChooser;
import antafes.mountaincoreTranslator.service.FileHandlerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewActionListener implements ActionListener
{
    private final Component parent;

    public NewActionListener(Component parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Configuration configuration = Configuration.getInstance();

        JOptionPane.showMessageDialog(
            this.parent,
            "In order to create a new translation file you need to select any existing file."
        );
        OpenFileChooser fileChooser = new OpenFileChooser();
        fileChooser.setCurrentDirectory(configuration.getOpenDirPath());
        int result = fileChooser.showOpenDialog(this.parent);

        if (result == OpenFileChooser.APPROVE_OPTION) {
            configuration.setOpenDirPath(fileChooser.getSelectedFile().getParent());
            configuration.saveProperties();

            String language = JOptionPane.showInputDialog(this.parent, "What language do you want to create?")
                .toLowerCase();
            FileHandlerService service = new FileHandlerService(fileChooser.getSelectedFile().getPath());
            FileOpenedEvent event = new FileOpenedEvent();
            TranslationMap map = new TranslationMap(language);
            map.putAll(service.read());
            event.setTranslationMap(map)
                .setFilename(configuration.getSaveDirPath(language).getName());
            MountaincoreTranslator.getDispatcher().dispatch(event);
        }
    }
}
