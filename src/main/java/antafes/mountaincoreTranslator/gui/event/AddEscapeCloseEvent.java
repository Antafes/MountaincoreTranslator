package antafes.mountaincoreTranslator.gui.event;

import lombok.Getter;
import lombok.Setter;
import scripts.laniax.framework.event_dispatcher.Event;

import javax.swing.*;

@Getter
@Setter
public class AddEscapeCloseEvent extends Event
{
    private JDialog dialog;
}
