package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class UnsavedFileCheckListener extends EventListener<UnsavedFileCheckEvent>
{
    public UnsavedFileCheckListener()
    {
    }

    public UnsavedFileCheckListener(Consumer<UnsavedFileCheckEvent> consumer)
    {
        super(consumer);
    }

    public UnsavedFileCheckListener(Consumer<UnsavedFileCheckEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
