package antafes.mountaincoreTranslator.gui.event;

import lombok.Getter;
import lombok.Setter;
import scripts.laniax.framework.event_dispatcher.Event;

@Getter
@Setter
public class UnsavedFileCheckEvent extends Event
{
    private boolean fileUnsaved = false;
}
