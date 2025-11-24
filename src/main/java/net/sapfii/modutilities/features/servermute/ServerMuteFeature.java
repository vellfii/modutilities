package net.sapfii.modutilities.features.servermute;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerMuteFeature extends Feature implements PacketListeningFeature {
    private static final Pattern FAILEDSPEECH = Pattern.compile("(?<player>.+) tried to speak, but is muted\\.");
    private static final Pattern FAILEDJOIN = Pattern.compile("(?<player>.+) tried to join, but is banned \\((?<time>.+)\\)!");
    private static final Pattern GENERIC = Pattern.compile("\\[Server: (?<msg>.+)]");
    private static final String VIAVERS = "\\[ViaVersion] There is a newer plugin version available:";
    private static final String FAWE = "(FAWE) A new release for FastAsyncWorldEdit";


    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay)) || !ModUtilsConfig.config.hideJoinMessages.get()) return PacketResult.PASS;
        String str = msgText.getString();
        Matcher failedJoin = FAILEDJOIN.matcher(str);
        Matcher failedSpeech = FAILEDSPEECH.matcher(str);
        Matcher generic = GENERIC.matcher(str);
        boolean cancel = failedJoin.find() || failedSpeech.find() || generic.find() || str.startsWith(VIAVERS) || str.startsWith(FAWE);
        return cancel ? PacketResult.CANCEL : PacketResult.PASS;
    }
}
