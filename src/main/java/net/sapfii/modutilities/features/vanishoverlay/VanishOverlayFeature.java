package net.sapfii.modutilities.features.vanishoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.Rank;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.sapfii.modutilities.features.interfaces.RenderedFeature;
import net.velli.scelli.widget.widgets.Widget;

import java.util.List;
import java.util.regex.Pattern;

public class VanishOverlayFeature extends Feature implements RenderedFeature, PacketListeningFeature, CommandListeningFeature {

    private static final Text ENTER_ADMIN_VANISH = Text.literal("[ADMIN]").withColor(0xFF0000).append(Text.literal(" You are now vanished!").withColor(0xFFFFFF));
    private static final Text EXIT_ADMIN_VANISH = Text.literal("[ADMIN]").withColor(0xFF0000).append(Text.literal(" You are no longer vanished.").withColor(0xFFFFFF));
    private static final Text ENTER_MOD_VANISH = Text.literal("[MOD]").withColor(0x00AA00).append(Text.literal(" You are now vanished!").withColor(0xFFFFFF));
    private static final Text EXIT_MOD_VANISH = Text.literal("[MOD]").withColor(0x00AA00).append(Text.literal(" You are no longer vanished.").withColor(0xFFFFFF));
    private static final Text OVERLAPPING_VANISH = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You cannot be in both Admin Vanish and Mod Vanish.").withColor(0xAAAAAA));
    private static final Text ADMIN_VANISH_FAILED2 = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You are already in Admin Vanish.").withColor(0xAAAAAA));
    private static final Text ADMIN_VANISH_FAILED3 = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You are already out of Admin Vanish.").withColor(0xAAAAAA));

    private static final String ENTERED_VANISH = "» Vanish enabled. You will not be visible to other players.";
    private static final String EXITED_VANISH = "» Vanish disabled. You will now be visible to other players.";
    private static final String ADMINV_REMINDER = "\\[ADMIN] You are currently vanished!";


    protected VanishWidget widget = VanishWidget.create();

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (packet instanceof LoginHelloS2CPacket || packet instanceof GameStateChangeS2CPacket) VanishMode.set(VanishMode.NONE);
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        String string = msgText.getString();
        if (string.matches(ADMINV_REMINDER)) {
            VanishMode.set(VanishMode.ADMIN);
            return ModUtilsConfig.config.useVanishDisplay.get() ? PacketResult.CANCEL : PacketResult.PASS;
        } else if (string.matches(ENTERED_VANISH) || string.matches(EXITED_VANISH)
            && ModUtilsConfig.config.useVanishDisplay.get()) {
            if (string.matches(ENTERED_VANISH) && VanishMode.is(VanishMode.NONE)) VanishMode.set(VanishMode.MOD);
            return PacketResult.CANCEL;
        }
        return PacketResult.PASS;
    }

    @Override
    public CommandResult onCommand(String command) {
        if (command.matches("mod v") || command.matches("mod vanish")) {
            if (Rank.is(Rank.NONE)) return CommandResult.PASS;
            if (VanishMode.is(VanishMode.ADMIN)) {
                ModUtilities.sendMessage(OVERLAPPING_VANISH, false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                return CommandResult.CANCEL;
            } else if (VanishMode.is(VanishMode.MOD)) {
                ModUtilities.sendMessage(EXIT_MOD_VANISH, false);
                VanishMode.set(VanishMode.NONE);
            } else if (VanishMode.is(VanishMode.NONE)) {
                ModUtilities.sendMessage(ENTER_MOD_VANISH, false);
                VanishMode.set(VanishMode.MOD);
            }
        } else if (command.matches("adminv on")) {
            if (!Rank.is(Rank.ADMIN)) return CommandResult.PASS;
            if (VanishMode.is(VanishMode.ADMIN)) {
                ModUtilities.sendMessage(ADMIN_VANISH_FAILED2, false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                return CommandResult.CANCEL;
            } else if (VanishMode.is(VanishMode.MOD)) {
                ModUtilities.sendMessage(OVERLAPPING_VANISH, false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                return CommandResult.CANCEL;
            } else if (VanishMode.is(VanishMode.NONE)) {
                ModUtilities.sendMessage(ENTER_ADMIN_VANISH, false);
                VanishMode.set(VanishMode.ADMIN);
            }
        } else if (command.matches("adminv off")) {
            if (!Rank.is(Rank.ADMIN)) return CommandResult.PASS;
            if (VanishMode.is(VanishMode.ADMIN)) {
                ModUtilities.sendMessage(EXIT_ADMIN_VANISH, false);
                VanishMode.set(VanishMode.NONE);
            } else {
                ModUtilities.sendMessage(ADMIN_VANISH_FAILED3, false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                return CommandResult.CANCEL;
            }
        } else if ((command.matches("s") || command.matches("spawn"))
                && VanishMode.is(VanishMode.MOD)) {
            ModUtilities.sendMessage(EXIT_MOD_VANISH, false);
            VanishMode.set(VanishMode.NONE);
        }
        return CommandResult.PASS;
    }

    @Override
    public void render(DrawContext context, float mouseX, float mouseY) {
        if (!VanishMode.is(VanishMode.NONE)) widget.withPosition(5, 0, false);
        else widget.withPosition(5, -20, false);
        if (ModUtilsConfig.config.useVanishDisplay.get()) renderWidgets(context, mouseX, mouseY, 255);
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(widget);
    }

    @Override
    public RenderedFeature getThis() {
        return this;
    }
}
