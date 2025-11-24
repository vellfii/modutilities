package net.sapfii.modutilities.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.Features;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.Rank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MClientPlayNetworkHandler {

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void onSendCommand(String command, CallbackInfo ci) {
        boolean result = false;
        for (Feature feature : Features.registeredFeatures()) {
            if (feature instanceof CommandListeningFeature clf &&
                    clf.onCommand(command).Value()) {
                result = true;
            }
        }
        if (result) ci.cancel();
    }

    @Inject(method = "onCommandTree", at = @At("TAIL"))
    private void onCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        CommandDispatcher<?> dispatcher = ((ClientPlayNetworkHandler)(Object)this).getCommandDispatcher();
        traverse(dispatcher.getRoot());
    }

    @Unique
    private void traverse(CommandNode<?> node) {
        for (CommandNode<?> child : node.getChildren()) {
            if (child.getName().matches("history") && Rank.is(Rank.NONE)) Rank.set(Rank.MOD);
            if (child.getName().matches("adminv")) Rank.set(Rank.ADMIN);
            traverse(child);
        }
    }

}
