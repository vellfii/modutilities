package net.sapfii.modutilities.features.tprepeat;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.features.interfaces.KeyBindListeningFeature;
import net.sapfii.modutilities.features.vanishoverlay.VanishMode;
import net.sapfii.modutilities.keybinds.ModUtilsKeyBinds;

public class TPRepeatFeature extends Feature implements CommandListeningFeature, KeyBindListeningFeature {

    private String lastCommand = "";

    @Override
    public CommandResult onCommand(String command) {
        if (command.startsWith("tp ")) lastCommand = command;
        return CommandResult.PASS;
    }

    @Override
    public void onKeyBindPress(KeyBinding keyBind) {
        if (keyBind.equals(ModUtilsKeyBinds.REPEAT_TP)) {
            if (!lastCommand.isEmpty()) {
                if (!VanishMode.is(VanishMode.NONE)) {
                    ModUtilities.sendCommand(lastCommand);
                    ModUtilities.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1f);
                } else {
                    ModUtilities.sendMessage(Text.literal("Error: ").formatted(Formatting.RED).append(Text.literal("You need to be in vanish to do this!").formatted(Formatting.GRAY)), false);
                    ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                }
            } else {
                ModUtilities.sendMessage(Text.literal("Error: ").formatted(Formatting.RED).append(Text.literal("You have not done a TP command to repeat!").formatted(Formatting.GRAY)), false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
            }
        }
    }
}
