package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class AddEscapeCloseListener extends EventListener<AddEscapeCloseEvent>
{
    public AddEscapeCloseListener()
    {
    }

    public AddEscapeCloseListener(Consumer<AddEscapeCloseEvent> consumer)
    {
        super(consumer);
    }

    public AddEscapeCloseListener(Consumer<AddEscapeCloseEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
