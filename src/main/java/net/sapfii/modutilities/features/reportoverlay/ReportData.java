package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.config.enums.ReportType;

import java.time.Duration;

public class ReportData {
    public final String reporter;
    public final String offender;
    public final String reason;
    public final String location;

    private final long creationTime;

    public static ReportData create(String reporter, String offender, String reason, String location) {
        return new ReportData(reporter, offender, reason, location);
    }

    private ReportData(String reporter, String offender, String reason, String location) {
        this.reporter = reporter;
        this.offender = offender;
        this.reason = reason;
        this.location = location;
        this.creationTime = System.currentTimeMillis();
    }

    public int time() {
        return Math.round((System.currentTimeMillis() - creationTime) / 1000f);
    }

    public String formattedTime() {
        Duration d = Duration.ofSeconds(time());
        return String.format("%02d:%02d", d.toMinutes(), d.toSeconds() % 60);
    }

    public Text title() {
        ModUtilsConfig config = ModUtilsConfig.config;
        if (config.reportType.is(ReportType.CLASSIC)) {
            return Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true))
                    .append(Text.literal("Incoming Report ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal("(" + reporter + ") ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + formattedTime() + " ago)\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        } else if (config.reportType.is(ReportType.REIMAGINED)) {
            return Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true))
                    .append(Text.literal("Incoming Report ").styled(style -> style.withColor(0xFFAAAA).withBold(false)))
                    .append(Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true)))
                    .append(Text.literal("(").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal(reporter).styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal(") ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + formattedTime() + " ago)\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        } else if (config.reportType.is(ReportType.REVAMPED)) {
            return Text.literal("[ ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false))
                    .append(Text.literal("Report ").styled(style -> style.withColor(0xFFAAAA).withBold(false)))
                    .append(Text.literal("filed by ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal(reporter).styled(style -> style.withColor(Formatting.RED).withBold(false)))
                    .append(Text.literal(" ] ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + formattedTime() + " ago)\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        }
        return Text.literal("SOMETHING WENT HORRIBLY WRONG");
    }

}
