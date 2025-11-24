package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.screen.WidgetContainerScreen;
import net.velli.scelli.widget.WidgetPos;
import net.velli.scelli.widget.widgets.TextWidget;
import net.velli.scelli.widget.widgets.containers.ListWidget;

import java.util.List;

public class ReportScreen extends WidgetContainerScreen {

    protected ListWidget reportList;

    public ReportScreen() {
        super(null);
        List<ReportData> reports = ReportOverlayFeature.instance.reports;
        reportList = ListWidget.create().withDimensions(300, reports.isEmpty() ? 30 : ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        reportList.withAlignment(WidgetPos.Alignment.CENTER);
        reportList.addWidget(TextWidget.create().withText(Text.literal(reports.isEmpty() ? "No reports this session!" : "Reports")).withAlignment(TextWidget.TextAlignment.CENTER));
        for (ReportData report : ReportOverlayFeature.instance.reports.reversed()) reportList.addWidget(ReportWidget.create(report));
        reportList.withPosition(0, 500, true);
        reportList.withPosition(0, 0, false);
        addWidget(reportList);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<ReportData> reports = ReportOverlayFeature.instance.reports;
        reportList.withDimensions(300, reports.isEmpty() ? 30 : ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        super.resize(client, width, height);
    }
}
