package antafes.mountaincoreTranslator.gui.event;

import antafes.mountaincoreTranslator.entity.TranslationMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import scripts.laniax.framework.event_dispatcher.Event;


@Getter
@Setter
@Accessors(chain = true)
public class SaveFileEvent extends Event
{
    private String filename;
    private TranslationMap translationMap;
}
