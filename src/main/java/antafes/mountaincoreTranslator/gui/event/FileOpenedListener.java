package antafes.mountaincoreTranslator.gui.event;

import scripts.laniax.framework.event_dispatcher.EventListener;

import java.util.function.Consumer;

public class FileOpenedListener extends EventListener<FileOpenedEvent>
{
    public FileOpenedListener()
    {
    }

    public FileOpenedListener(Consumer<FileOpenedEvent> consumer)
    {
        super(consumer);
    }

    public FileOpenedListener(Consumer<FileOpenedEvent> consumer, int priority)
    {
        super(consumer, priority);
    }
}
