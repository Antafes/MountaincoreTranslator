package antafes.mountaincoreTranslator.gui.policy;

import java.awt.*;
import java.util.Vector;

public class TranslationFocusTraversalPolicy extends FocusTraversalPolicy
{
    private final Vector<Component> order;

    public TranslationFocusTraversalPolicy(Vector<Component> order)
    {
        this.order = new Vector<>();
        this.order.addAll(order);
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent)
    {
        int idx = (this.order.indexOf(aComponent) + 1) % this.order.size();

        return this.order.get(idx);
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent)
    {
        int idx = (this.order.indexOf(aComponent) - 1);

        if (idx < 0) {
            idx = this.order.size() - 1;
        }

        return this.order.get(idx);
    }

    @Override
    public Component getFirstComponent(Container aContainer)
    {
        return this.order.get(0);
    }

    @Override
    public Component getLastComponent(Container aContainer)
    {
        return this.order.lastElement();
    }

    @Override
    public Component getDefaultComponent(Container aContainer)
    {
        return this.order.get(0);
    }
}
