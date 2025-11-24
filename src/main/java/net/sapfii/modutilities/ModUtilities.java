package net.sapfii.modutilities;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.sapfii.modutilities.commands.ModUtilsCommands;
import net.sapfii.modutilities.config.ConfigOption;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.Features;
import net.sapfii.modutilities.features.logscreen.LogScreenFeature;
import net.sapfii.modutilities.features.playertracker.PlayerTrackerFeature;
import net.sapfii.modutilities.features.reportoverlay.ReportOverlayFeature;
import net.sapfii.modutilities.features.servermute.ServerMuteFeature;
import net.sapfii.modutilities.features.setrankautocomplete.SetRankAutocompleteFeature;
import net.sapfii.modutilities.features.tprepeat.TPRepeatFeature;
import net.sapfii.modutilities.features.vanishoverlay.VanishOverlayFeature;
import net.sapfii.modutilities.keybinds.ModUtilsKeyBinds;
import net.sapfii.modutilities.sounds.ModUtilsSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ModUtilities implements ClientModInitializer {

    public static MinecraftClient MC = MinecraftClient.getInstance();
    public static final String MOD_ID = "modutilities";
    public static final Logger LOGGER = LoggerFactory.getLogger("ModUtils");

    @Override
    public void onInitializeClient() {
        ModUtilsConfig.loadConfig();
        ModUtilsCommands.init();
        ModUtilsSounds.init();
        ModUtilsKeyBinds.init();

        Features.registerFeatures(
                new ReportOverlayFeature(),
                new VanishOverlayFeature(),
                new ServerMuteFeature(),
                new TPRepeatFeature(),
                new SetRankAutocompleteFeature(),
                new LogScreenFeature(),
                new PlayerTrackerFeature()
        );

        Features.init();

        LOGGER.info("ogfh... im modding it... modding it!");
    }

    public static void sendMessage(Text msg, boolean overlay) {
        if (MC.player != null) MC.player.sendMessage(msg, overlay);
    }

    public static void playSound(SoundEvent sound, float pitch) {
        MC.getSoundManager().play(PositionedSoundInstance.master(sound, pitch));
    }

    public static void sendCommand(String command) {
        Objects.requireNonNull(MC.getNetworkHandler()).sendChatCommand(command);
    }

    public static float lerp(float a, float b, float t) {
        t = Math.clamp(t, 0, 1);
        return a + t * (b - a);
    }

    public static int wrap(int value, int max) {
        return ((value % max) + max) % max;
    }
}
