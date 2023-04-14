package antafes.mountaincoreTranslator.gui.event;

import lombok.Getter;
import lombok.Setter;
import scripts.laniax.framework.event_dispatcher.Event;

@Getter
@Setter
public class SearchEvent extends Event
{
    private String searchValue;
}
