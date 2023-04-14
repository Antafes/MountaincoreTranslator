package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class TriggerSaveFileListener extends EventListener<TriggerSaveFileEvent>
{
    public TriggerSaveFileListener()
    {
    }

    public TriggerSaveFileListener(Consumer<TriggerSaveFileEvent> consumer)
    {
        super(consumer);
    }

    public TriggerSaveFileListener(Consumer<TriggerSaveFileEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
