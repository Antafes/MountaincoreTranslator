package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.Configuration;
import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.gui.SaveFileChooser;
import antafes.mountaincoreTranslator.service.FileHandlerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveActionListener implements ActionListener
{
    private final Component parent;

    public SaveActionListener(Component parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        SaveFileEvent event = new SaveFileEvent();
        MountaincoreTranslator.getDispatcher().dispatch(event);
        Configuration configuration = Configuration.getInstance();
        SaveFileChooser fileChooser = new SaveFileChooser();
        fileChooser.setSelectedFile(configuration.getSaveDirPath(event.getFilename()));
        int result = fileChooser.showSaveDialog(this.parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            configuration.setSaveDirPath(fileChooser.getSelectedFile().getParent());
            configuration.saveProperties();

            File newFile = new File(fileChooser.getSelectedFile().getPath());

            if (newFile.exists()) {
                int response = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to replace the existing file?",
                    "Confirm", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            FileHandlerService service = new FileHandlerService(fileChooser.getSelectedFile().getPath());
            service.write(event.getTranslationMap());
        }
    }
}
