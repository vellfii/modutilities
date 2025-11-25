package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.widget.interfaces.ClickableWidget;
import net.velli.scelli.widget.widgets.ButtonWidget;
import net.velli.scelli.widget.widgets.TextWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.containers.BasicContainer;
import org.joml.Vector2f;

import java.util.List;

public class ReportWidget extends BasicContainer<ReportWidget> {

    protected ReportData data;
    protected final TextWidget textDisplay = TextWidget.create();
    protected final ButtonWidget tpButton = ButtonWidget.create().withOpacity(0, true).withClickEvent(
            (button, mouseX, mouseY, active) -> {
                if (active) ReportOverlayFeature.clickedReport(data);
            }
    );

    public static ReportWidget create(ReportData data) {
        ReportWidget widget = new ReportWidget();
        widget.data = data;
        widget.updateText();
        widget.withDimensions(ModUtilities.MC.textRenderer.getWidth(data.title()) + 6, widget.textDisplay.renderedHeight(), true);
        return widget;
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(textDisplay, tpButton);
    }

    @Override
    protected void render(DrawContext context, float mouseX, float mouseY, int opacity, float delta) {
        updateText();
        withDimensions(renderedWidth(), textDisplay.height() + 6, true);
        textDisplay.withPosition(3, 3, true);
        context.fill(0, 0, renderedWidth(), renderedHeight(), stackOpacity(0x66000000, opacity));
        tpButton.withDimensions(renderedWidth(), renderedHeight(), true);
        renderWidgets(context, mouseX, mouseY, opacity);
    }

    @Override
    public void onClick(float mouseX, float mouseY, boolean active) {
        this.getWidgets().forEach((widget) -> {
            if (widget instanceof ClickableWidget cw) {
                boolean hovered = this.isHovered(mouseX, mouseY) && renderedOpacity() > 0;
                Vector2f alignmentOffsets = widget.position().alignmentOffsets(this);
                cw.onClick(mouseX - (float)widget.x() - alignmentOffsets.x, mouseY - (float)widget.y() - alignmentOffsets.y, hovered);
            }
        });
    }

    @Override
    public void hoverWidgets(float mouseX, float mouseY, boolean active) {
        this.getWidgets().forEach((widget) -> {
            boolean hovered = this.isHovered(mouseX, mouseY) && renderedOpacity() > 0;
            Vector2f alignmentOffsets = widget.position().alignmentOffsets(this);
            widget.hover(mouseX - (float)widget.renderedX() - alignmentOffsets.x, mouseY - (float)widget.renderedY() - alignmentOffsets.y, hovered);
        });
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
