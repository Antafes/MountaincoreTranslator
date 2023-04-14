package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class CloseProgramListener extends EventListener<CloseProgramEvent>
{
    public CloseProgramListener()
    {
    }

    public CloseProgramListener(Consumer<CloseProgramEvent> consumer)
    {
        super(consumer);
    }

    public CloseProgramListener(Consumer<CloseProgramEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
