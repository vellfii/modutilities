package net.sapfii.modutilities.features.vanishoverlay;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.widget.interfaces.WidgetContainer;
import net.velli.scelli.widget.widgets.TextWidget;
import net.velli.scelli.widget.widgets.Widget;

import java.util.List;

public class VanishWidget extends Widget<VanishWidget> implements WidgetContainer<VanishWidget> {

    protected final TextWidget textDisplay = TextWidget.create();

    public static VanishWidget create() {
        return new VanishWidget();
    }

    private static Text vanishText = Text.empty();
    private static int bgColor = 0x66000000;

    @Override
    protected void render(DrawContext context, float mouseX, float mouseY, int opacity, float delta) {
        TextRenderer textRenderer = ModUtilities.MC.textRenderer;
        if (VanishMode.is(VanishMode.ADMIN)) vanishText = Text.literal("Admin").withColor(0xFF0000).append(Text.literal(" Vanish"));
        if (VanishMode.is(VanishMode.ADMIN)) bgColor = 0x66DD0023;
        if (VanishMode.is(VanishMode.MOD)) vanishText = Text.literal("Mod").withColor(0x00FF00).append(Text.literal(" Vanish"));
        if (VanishMode.is(VanishMode.MOD)) bgColor = 0x6623DD00;
        textDisplay.withPosition(2, 5, true);
        textDisplay.withDimensions(textRenderer.getWidth(vanishText), 16, true);
        textDisplay.withText(vanishText);
        withDimensions(textRenderer.getWidth(vanishText) + 4, 16, true);
        context.fill(0, 0, renderedWidth(), renderedHeight(), bgColor);
        renderWidgets(context, mouseX, mouseY, opacity);
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(textDisplay);
    }

    @Override
    public VanishWidget getThis() {
        return this;
    }
}
