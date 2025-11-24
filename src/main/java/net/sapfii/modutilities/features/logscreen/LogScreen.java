package net.sapfii.modutilities.features.logscreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.config.enums.LogDirection;
import net.velli.scelli.screen.WidgetContainerScreen;
import net.velli.scelli.widget.WidgetPos;
import net.velli.scelli.widget.widgets.TextWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.containers.ListWidget;

import java.util.ArrayList;
import java.util.List;

public class LogScreen extends WidgetContainerScreen {
    protected ListWidget list;

    public LogScreen(List<Text> lines, Text header) {
        super(null);
        list = ListWidget.create()
                .withAlignment(WidgetPos.Alignment.CENTER)
                .withDimensions(
                        ModUtilities.MC.getWindow().getScaledWidth() - 150,
                        ModUtilities.MC.getWindow().getScaledHeight() - 50,
                        true);
        if (ModUtilsConfig.config.logDirection.is(LogDirection.UP)) lines = lines.reversed();
        List<Widget<?>> screenLines = new ArrayList<>(List.of(
                TextWidget.create().withText(header).withAlignment(TextWidget.TextAlignment.CENTER),
                TextWidget.create()
        ));
        lines.forEach(line -> screenLines.add(TextWidget.create().withText(line)));
        screenLines.add(TextWidget.create());
        screenLines.add(TextWidget.create().withText(header).withAlignment(TextWidget.TextAlignment.CENTER));
        list.addWidgets(screenLines);
        list.withPosition(0, 500, true);
        list.withPosition(0, 0, false);
        addWidget(list);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        list.withDimensions(ModUtilities.MC.getWindow().getScaledWidth() - 150, ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        super.resize(client, width, height);
    }
}
