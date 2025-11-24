package net.sapfii.modutilities.features.interfaces;

import net.minecraft.client.gui.DrawContext;
import net.velli.scelli.widget.interfaces.WidgetContainer;

public interface RenderedFeature extends WidgetContainer<RenderedFeature> {

    void render(DrawContext context, float mouseX, float mouseY);

    @Override
    default int x() {
        return 0;
    }

    @Override
    default int renderedX() {
        return 0;
    }

    @Override
    default int y() {
        return 0;
    }

    @Override
    default int renderedY() {
        return 0;
    }

    @Override
    default int width() {
        return 0;
    }

    @Override
    default int renderedWidth() {
        return 0;
    }

    @Override
    default int height() {
        return 0;
    }

    @Override
    default int renderedHeight() {
        return 0;
    }

    @Override
    default int opacity() {
        return 255;
    }

    @Override
    default int renderedOpacity() {
        return 255;
    }
}
