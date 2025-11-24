package net.sapfii.modutilities.features.setrankautocomplete;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.velli.scelli.ScreenHandler;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetRankAutocompleteFeature extends Feature implements PacketListeningFeature, CommandListeningFeature {
    private static final Pattern CODE = Pattern.compile("Attempting to execute \"(.+)\". To verify, run this command again and add \\[HOVER] to the end\\. This key will expire in 1 minute\\.");

    private static boolean awaitingCode = false;
    private static String lastCommand = "";

    @Override
    public CommandResult onCommand(String command) {
        if (command.startsWith("setrank")) {
            awaitingCode = true;
            lastCommand = command;
        }
        return CommandResult.PASS;
    }

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        String string = msgText.getString();
        if (string.matches("An unexpected error occurred trying to execute that command")) awaitingCode = false;
        if (!awaitingCode) return PacketResult.PASS;
        Matcher matcher = CODE.matcher(string);
        if (matcher.find()) {
            List<Text> siblings = msgText.getSiblings();
            for (Text sibling : siblings) {
                HoverEvent.ShowText text = new HoverEvent.ShowText(Text.literal("hi"));
                if (sibling.getStyle().getHoverEvent() instanceof HoverEvent.ShowText(Text value)) {
                    String code = Objects.requireNonNull(value.getString());
                    ScreenHandler.openScreen(new ChatScreen("/" + lastCommand + " " + code));
                }
            }
        }
        awaitingCode = false;
        return PacketResult.PASS;
    }
}
