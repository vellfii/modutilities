package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.widget.interfaces.WidgetContainer;
import net.velli.scelli.widget.widgets.TextWidget;
import net.velli.scelli.widget.widgets.Widget;

import java.util.ArrayList;
import java.util.List;

public class ReportWidget extends Widget<ReportWidget> implements WidgetContainer<ReportWidget> {

    protected ReportData data;
    protected final TextWidget textDisplay = TextWidget.create();

    public static ReportWidget create(ReportData data) {
        ReportWidget widget = new ReportWidget();
        widget.data = data;
        widget.updateText();
        widget.withDimensions(ModUtilities.MC.textRenderer.getWidth(data.title()) + 6, widget.textDisplay.renderedHeight(), true);
        return widget;
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(textDisplay);
    }

    @Override
    protected void render(DrawContext context, float mouseX, float mouseY, int opacity, float delta) {
        updateText();
        withDimensions(renderedWidth(), textDisplay.height() + 6, true);
        textDisplay.withPosition(3, 3, true);
        context.fill(0, 0, renderedWidth(), renderedHeight(), stackOpacity(0x66000000, opacity));
        renderWidgets(context, mouseX, mouseY, opacity);
    }

    @Override
    public ReportWidget getThis() {
        return this;
    }

    protected void updateText() {
        MutableText text = Text.empty();
        text.append(data.title());
        text.append(Text.literal("").append(Text.literal("Offender: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.offender + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        text.append(Text.literal("").append(Text.literal("Offense: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.reason + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        text.append(Text.literal("").append(Text.literal("Location: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.location + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        textDisplay.withText(text);
        textDisplay.withDimensions(ModUtilities.MC.textRenderer.getWidth(data.title()), textDisplay.height(), true);
    }
}
