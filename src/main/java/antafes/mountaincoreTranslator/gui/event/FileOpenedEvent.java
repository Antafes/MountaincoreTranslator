package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.entity.TranslationMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import scripts.laniax.framework.event_dispatcher.Event;

@Getter
@Setter
@Accessors(chain = true)
public class FileOpenedEvent extends Event
{
    private TranslationMap translationMap;
    private String filename;
}
