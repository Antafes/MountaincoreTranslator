package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class SearchListener extends EventListener<SearchEvent>
{
    public SearchListener()
    {
    }

    public SearchListener(Consumer<SearchEvent> consumer)
    {
        super(consumer);
    }

    public SearchListener(Consumer<SearchEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
