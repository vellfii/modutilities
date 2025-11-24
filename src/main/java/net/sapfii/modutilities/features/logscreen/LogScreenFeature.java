package net.sapfii.modutilities.features.logscreen;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.velli.scelli.ScreenHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogScreenFeature extends Feature implements PacketListeningFeature, CommandListeningFeature {
    private static final Pattern LOG_REGEX = Pattern.compile("--------------\\[ (.+) Log | (.+) ]--------------");
    private static Text logHeader = Text.literal("");
    private static boolean collectingLogs = false;
    private static boolean awaitingLogs = false;
    public static String lastLogCmd = "";

    private ArrayList<Text> logLines = new ArrayList<>();

    @Override
    public CommandResult onCommand(String command) {
        if (command.startsWith("mod log") || command.startsWith("admin log")) {
            awaitingLogs = true;
            lastLogCmd = command;
            return CommandResult.PASS;
        }
        return CommandResult.PASS;
    }

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        if (!ModUtilsConfig.config.useLogScreen.get()) return PacketResult.PASS;
        String string = msgText.getString();

        Matcher matcher = LOG_REGEX.matcher(string);
        if (matcher.find()) {
            if (collectingLogs) {
                ScreenHandler.openScreen(new LogScreen(logLines, logHeader));
                logHeader = Text.literal("");
                logLines = new ArrayList<>();
            } else if (awaitingLogs) {
                logHeader = msgText;
                awaitingLogs = false;
            }
            collectingLogs = !collectingLogs;
            return PacketResult.CANCEL;
        }

        if (collectingLogs) {
            logLines.add(msgText);
            return PacketResult.CANCEL;
        }
        return PacketResult.PASS;
    }
}
