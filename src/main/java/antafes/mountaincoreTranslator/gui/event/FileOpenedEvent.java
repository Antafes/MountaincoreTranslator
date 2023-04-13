package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.entity.TranslationMap;
import lombok.Getter;
import lombok.Setter;
import scripts.laniax.framework.event_dispatcher.Event;

public class FileOpenedEvent extends Event
{
    @Getter
    @Setter
    private TranslationMap translationMap;
}
