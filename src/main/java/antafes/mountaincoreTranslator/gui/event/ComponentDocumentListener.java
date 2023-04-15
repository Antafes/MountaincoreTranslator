package antafes.mountaincoreTranslator.gui.event;

import javax.swing.event.DocumentListener;
import java.awt.*;

public abstract class ComponentDocumentListener implements DocumentListener {
    private Component component;

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
