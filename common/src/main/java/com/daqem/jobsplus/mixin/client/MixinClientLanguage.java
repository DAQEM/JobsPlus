package com.daqem.jobsplus.mixin.client;

import com.daqem.jobsplus.client.translator.ConfigTranslator;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class MixinClientLanguage {

    @Inject(
            method = "loadFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void jobsplus$loadFrom(ResourceManager resourceManager, List<String> list, boolean bl, CallbackInfoReturnable<ClientLanguage> cir, Map<String, String> map) {
        ConfigTranslator.load(list, map);
    }
}