package me.daqem.jobsplus.init;

import me.daqem.jobsplus.common.item.RodItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ModItemProperties {

    @SubscribeEvent
    public static void clientSetupHandler(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_1.get(), new ResourceLocation("pull"), (p_174620_, p_174621_, p_174622_, p_174623_) -> {
                if (p_174622_ == null) {
                    return 0.0F;
                } else {
                    return (float) (p_174620_.getUseDuration() - p_174622_.getUseItemRemainingTicks()) / 20.0F;
                }
            });
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_2.get(), new ResourceLocation("pull"), (p_174620_, p_174621_, p_174622_, p_174623_) -> {
                if (p_174622_ == null) {
                    return 0.0F;
                } else {
                    return (float) (p_174620_.getUseDuration() - p_174622_.getUseItemRemainingTicks()) / 20.0F;
                }
            });
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_3.get(), new ResourceLocation("pull"), (p_174620_, p_174621_, p_174622_, p_174623_) -> {
                if (p_174622_ == null) {
                    return 0.0F;
                } else {
                    return (float) (p_174620_.getUseDuration() - p_174622_.getUseItemRemainingTicks()) / 20.0F;
                }
            });
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_4.get(), new ResourceLocation("pull"), (p_174620_, p_174621_, p_174622_, p_174623_) -> {
                if (p_174622_ == null) {
                    return 0.0F;
                } else {
                    return (float) (p_174620_.getUseDuration() - p_174622_.getUseItemRemainingTicks()) / 20.0F;
                }
            });

            ItemProperties.register(ModItems.FISHERMANS_ROD_LEVEL_1.get(), new ResourceLocation("cast"), (p_174595_, p_174596_, p_174597_, p_174598_) -> {
                if (p_174597_ == null) {
                    return 0.0F;
                } else {
                    boolean flag = p_174597_.getMainHandItem() == p_174595_;
                    boolean flag1 = p_174597_.getOffhandItem() == p_174595_;
                    if (p_174597_.getMainHandItem().getItem() instanceof RodItem) {
                        flag1 = false;
                    }

                    return (flag || flag1) && p_174597_ instanceof Player && ((Player) p_174597_).fishing != null ? 1.0F : 0.0F;
                }
            });
            ItemProperties.register(ModItems.FISHERMANS_ROD_LEVEL_2.get(), new ResourceLocation("cast"), (p_174595_, p_174596_, p_174597_, p_174598_) -> {
                if (p_174597_ == null) {
                    return 0.0F;
                } else {
                    boolean flag = p_174597_.getMainHandItem() == p_174595_;
                    boolean flag1 = p_174597_.getOffhandItem() == p_174595_;
                    if (p_174597_.getMainHandItem().getItem() instanceof RodItem) {
                        flag1 = false;
                    }

                    return (flag || flag1) && p_174597_ instanceof Player && ((Player) p_174597_).fishing != null ? 1.0F : 0.0F;
                }
            });
            ItemProperties.register(ModItems.FISHERMANS_ROD_LEVEL_3.get(), new ResourceLocation("cast"), (p_174595_, p_174596_, p_174597_, p_174598_) -> {
                if (p_174597_ == null) {
                    return 0.0F;
                } else {
                    boolean flag = p_174597_.getMainHandItem() == p_174595_;
                    boolean flag1 = p_174597_.getOffhandItem() == p_174595_;
                    if (p_174597_.getMainHandItem().getItem() instanceof RodItem) {
                        flag1 = false;
                    }

                    return (flag || flag1) && p_174597_ instanceof Player && ((Player) p_174597_).fishing != null ? 1.0F : 0.0F;
                }
            });
            ItemProperties.register(ModItems.FISHERMANS_ROD_LEVEL_4.get(), new ResourceLocation("cast"), (p_174595_, p_174596_, p_174597_, p_174598_) -> {
                if (p_174597_ == null) {
                    return 0.0F;
                } else {
                    boolean flag = p_174597_.getMainHandItem() == p_174595_;
                    boolean flag1 = p_174597_.getOffhandItem() == p_174595_;
                    if (p_174597_.getMainHandItem().getItem() instanceof RodItem) {
                        flag1 = false;
                    }

                    return (flag || flag1) && p_174597_ instanceof Player && ((Player) p_174597_).fishing != null ? 1.0F : 0.0F;
                }
            });

            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_1.get(), new ResourceLocation("pulling"), (p_174615_, p_174616_, p_174617_, p_174618_) -> p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_2.get(), new ResourceLocation("pulling"), (p_174615_, p_174616_, p_174617_, p_174618_) -> p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_3.get(), new ResourceLocation("pulling"), (p_174615_, p_174616_, p_174617_, p_174618_) -> p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.HUNTERS_BOW_LEVEL_4.get(), new ResourceLocation("pulling"), (p_174615_, p_174616_, p_174617_, p_174618_) -> p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ ? 1.0F : 0.0F);
        });
    }
}
