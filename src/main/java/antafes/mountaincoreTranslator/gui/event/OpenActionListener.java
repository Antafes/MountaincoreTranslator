package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.Configuration;
import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.gui.OpenFileChooser;
import antafes.mountaincoreTranslator.service.FileHandlerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenActionListener implements ActionListener
{
    private final Component parent;

    public OpenActionListener(Component parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Configuration configuration = Configuration.getInstance();
        OpenFileChooser fileChooser = new OpenFileChooser();
        int result = fileChooser.showOpenDialog(this.parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            configuration.setOpenDirPath(fileChooser.getSelectedFile().getParent());
            configuration.saveProperties();

            FileHandlerService service = new FileHandlerService(fileChooser.getSelectedFile().getPath());
            FileOpenedEvent event = new FileOpenedEvent();
            event.setTranslationMap(service.read())
                .setFilename(fileChooser.getSelectedFile().getName());
            MountaincoreTranslator.getDispatcher().dispatch(event);
        }
    }
}
