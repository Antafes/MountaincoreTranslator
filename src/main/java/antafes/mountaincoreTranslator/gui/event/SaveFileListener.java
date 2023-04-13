package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class SaveFileListener extends EventListener<SaveFileEvent>
{
    public SaveFileListener()
    {
    }

    public SaveFileListener(Consumer<SaveFileEvent> consumer)
    {
        super(consumer);
    }

    public SaveFileListener(Consumer<SaveFileEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
