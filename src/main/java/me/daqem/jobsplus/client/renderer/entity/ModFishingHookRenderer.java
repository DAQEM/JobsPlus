package me.daqem.jobsplus.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.common.item.RodItem;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ModFishingHookRenderer extends EntityRenderer<ModFishingHook> {

    private static final ResourceLocation TEXTURE_LOCATION_LEVEL_1 = JobsPlus.getId("textures/entity/fishing_hook_level_1.png");
    private static final ResourceLocation TEXTURE_LOCATION_LEVEL_2 = JobsPlus.getId("textures/entity/fishing_hook_level_2.png");
    private static final ResourceLocation TEXTURE_LOCATION_LEVEL_3 = JobsPlus.getId("textures/entity/fishing_hook_level_3.png");
    private static final ResourceLocation TEXTURE_LOCATION_LEVEL_4 = JobsPlus.getId("textures/entity/fishing_hook_level_4.png");
    private static final RenderType RENDER_TYPE_LEVEL_1 = RenderType.entityCutout(TEXTURE_LOCATION_LEVEL_1);
    private static final RenderType RENDER_TYPE_LEVEL_2 = RenderType.entityCutout(TEXTURE_LOCATION_LEVEL_2);
    private static final RenderType RENDER_TYPE_LEVEL_3 = RenderType.entityCutout(TEXTURE_LOCATION_LEVEL_3);
    private static final RenderType RENDER_TYPE_LEVEL_4 = RenderType.entityCutout(TEXTURE_LOCATION_LEVEL_4);

    public ModFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static float fraction(int p_114691_) {
        return (float) p_114691_ / (float) 16;
    }

    private static void vertex(VertexConsumer p_114712_, Matrix4f p_114713_, Matrix3f p_114714_, int p_114715_, float p_114716_, int p_114717_, int p_114718_, int p_114719_) {
        p_114712_.vertex(p_114713_, p_114716_ - 0.5F, (float) p_114717_ - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) p_114718_, (float) p_114719_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114715_).normal(p_114714_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void stringVertex(float p_174119_, float p_174120_, float p_174121_, VertexConsumer p_174122_, PoseStack.Pose p_174123_, float p_174124_, float p_174125_) {
        float f = p_174119_ * p_174124_;
        float f1 = p_174120_ * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = p_174121_ * p_174124_;
        float f3 = p_174119_ * p_174125_ - f;
        float f4 = p_174120_ * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = p_174121_ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        p_174122_.vertex(p_174123_.pose(), f, f1, f2).color(0, 0, 0, 255).normal(p_174123_.normal(), f3, f4, f5).endVertex();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ModFishingHook modFishingHook) {
        Item item = Objects.requireNonNull(modFishingHook.getPlayerOwner()).getMainHandItem().getItem();
        if (item == ModItems.FISHERMANS_ROD_LEVEL_1.get()) {
            return TEXTURE_LOCATION_LEVEL_1;
        }
        if (item == ModItems.FISHERMANS_ROD_LEVEL_2.get()) {
            return TEXTURE_LOCATION_LEVEL_2;
        }
        if (item == ModItems.FISHERMANS_ROD_LEVEL_3.get()) {
            return TEXTURE_LOCATION_LEVEL_3;
        }
        if (item == ModItems.FISHERMANS_ROD_LEVEL_4.get()) {
            return TEXTURE_LOCATION_LEVEL_4;
        }
        return TEXTURE_LOCATION_LEVEL_1;
    }

    @SuppressWarnings("ConstantConditions")
    public void render(ModFishingHook hook, float p_114706_, float p_114707_, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int p_114710_) {
        Player player = hook.getPlayerOwner();
        if (player != null) {
            poseStack.pushPose();
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            Item item = hook.getPlayerOwner().getMainHandItem().getItem();
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE_LEVEL_1);
            if (item == ModItems.FISHERMANS_ROD_LEVEL_2.get())
                vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE_LEVEL_2);
            if (item == ModItems.FISHERMANS_ROD_LEVEL_3.get())
                vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE_LEVEL_3);
            if (item == ModItems.FISHERMANS_ROD_LEVEL_4.get())
                vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE_LEVEL_4);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 0.0F, 0, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 1.0F, 0, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 1.0F, 1, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 0.0F, 1, 0, 0);
            poseStack.popPose();
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            if (!(item instanceof RodItem)) {
                i = -i;
            }

            float f = player.getAttackAnim(p_114707_);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(p_114707_, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = Mth.sin(f2);
            double d1 = Mth.cos(f2);
            double d2 = (double) i * 0.35D;
            double d4;
            double d5;
            double d6;
            float f3;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
                double d7 = 960.0D / this.entityRenderDispatcher.options.fov().get();
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.525F, -0.1F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.5F);
                vec3 = vec3.xRot(-f1 * 0.7F);
                d4 = Mth.lerp(p_114707_, player.xo, player.getX()) + vec3.x;
                d5 = Mth.lerp(p_114707_, player.yo, player.getY()) + vec3.y;
                d6 = Mth.lerp(p_114707_, player.zo, player.getZ()) + vec3.z;
                f3 = player.getEyeHeight();
            } else {
                d4 = Mth.lerp(p_114707_, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) p_114707_ - 0.45D;
                d6 = Mth.lerp(p_114707_, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = player.isCrouching() ? -0.1875F : 0.0F;
            }

            double d9 = Mth.lerp(p_114707_, hook.xo, hook.getX());
            double d10 = Mth.lerp(p_114707_, hook.yo, hook.getY()) + 0.25D;
            double d8 = Mth.lerp(p_114707_, hook.zo, hook.getZ());
            float f4 = (float) (d4 - d9);
            float f5 = (float) (d5 - d10) + f3;
            float f6 = (float) (d6 - d8);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.lineStrip());
            PoseStack.Pose pose = poseStack.last();

            for (int k = 0; k <= 16; ++k) {
                stringVertex(f4, f5, f6, vertexConsumer, pose, fraction(k), fraction(k + 1));
            }

            poseStack.popPose();
            super.render(hook, p_114706_, p_114707_, poseStack, multiBufferSource, p_114710_);
        }
    }
}
