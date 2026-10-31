package client.openkoa.patch;

import asm.patchify.annotation.At;
import asm.patchify.annotation.Inject;
import asm.patchify.annotation.Patch;
import asm.patchify.annotation.Slice;
import asm.patchify.annotation.WrapInvoke;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import client.openkoa.ClientBase;
import client.openkoa.OpenKoAClient;
import client.openkoa.asm.Invocation;
import client.openkoa.event.impl.DisconnectEvent;
import client.openkoa.event.impl.PostMotionEvent;
import client.openkoa.event.impl.PreMotionEvent;
import client.openkoa.event.impl.TickEvent;
import client.openkoa.modules.impl.movement.NoSlow;
import client.openkoa.modules.impl.render.ESP;
import client.openkoa.render.Renderer;

@Patch(Minecraft.class)
public class MinecraftPatch {
    public static volatile boolean initialized = false;
    private static HitResult savedHitResult;

    public static void markInitialized() {
        initialized = true;
    }

    @Inject(method = "tick", desc = "()V")
    public static void onTick(Minecraft minecraft, CallbackInfo callbackInfo) throws Throwable {
        if (!initialized) {
            synchronized (MinecraftPatch.class) {
                if (!initialized) {
                    ClientBase.mc = OpenKoAClient.getMcInstance();
                    ClientBase.isLoading = true;
                    ModList.get().getMods().removeIf(modInfo -> modInfo.getModId().equals("openkoa"));
                    List<IModFileInfo> toRemove = new ArrayList<>();
                    for (IModFileInfo modFile : ModList.get().getModFiles()) {
                        for (IModInfo modInfo : modFile.getMods()) {
                            if (modInfo.getModId().equals("openkoa")) {
                                toRemove.add(modFile);
                            }
                        }
                    }
                    ModList.get().getModFiles().removeAll(toRemove);
                    new OpenKoAClient();
                    initialized = true;
                }
            }
        }
        if (OpenKoAClient.isReady()) {
            OpenKoAClient.serverTickRate = 1.0f;
            ClientBase.yaw = minecraft.player.getYRot();
            OpenKoAClient.instance.getEventBus().call(new TickEvent());
        }
    }

    @Inject(method = "tick", desc = "()V", at = @At(At.Type.TAIL))
    public static void onTickPost(Minecraft minecraft, CallbackInfo callbackInfo) throws Throwable {
        if (OpenKoAClient.isReady()) {
            OpenKoAClient.instance.getEventBus().call(new PostMotionEvent());
        }
    }

    @Inject(method = "close", desc = "()V", at = @At(At.Type.HEAD))
    public static void onClose(Minecraft minecraft, CallbackInfo callbackInfo) {
        OpenKoAClient.getInstance().shutdown();
    }

    @Inject(method = "setLevel", desc = "(Lnet/minecraft/client/multiplayer/ClientLevel;)V")
    public static void onSetLevel(ClientLevel level, CallbackInfo callbackInfo) {
        if (OpenKoAClient.isReady()) {
            OpenKoAClient.getInstance().getEventBus().call(new DisconnectEvent());
        }
    }

    @Inject(
            method = "handleKeybinds",
            desc = "()V",
            at = @At(value = At.Type.BEFORE_INVOKE, method = "net/minecraft/client/player/LocalPlayer/isUsingItem", desc = "()Z"),
            slice = @Slice(startIndex = 1, endIndex = 1)
    )
    public static void onHandleKeybinds(Minecraft minecraft, CallbackInfo callbackInfo) {
        if (OpenKoAClient.isReady()) {
            PreMotionEvent event = new PreMotionEvent();
            OpenKoAClient.getInstance().getEventBus().call(event);
            if (event.isCancelled()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "startUseItem", desc = "()V", at = @At(At.Type.HEAD))
    public static void onStartUseItemPre(Minecraft minecraft, CallbackInfo callbackInfo) {
        if (NoSlow.isBlocking(minecraft)) {
            savedHitResult = minecraft.hitResult;
            Vec3 location = savedHitResult.getLocation();
            minecraft.hitResult = BlockHitResult.miss(location, Direction.DOWN, BlockPos.containing(location));
        }
    }

    @Inject(method = "startUseItem", desc = "()V", at = @At(At.Type.TAIL))
    public static void onStartUseItemPost(Minecraft minecraft, CallbackInfo callbackInfo) {
        if (savedHitResult != null) {
            minecraft.hitResult = savedHitResult;
            savedHitResult = null;
        }
    }

    @WrapInvoke(
            method = "shouldEntityAppearGlowing",
            desc = "(Lnet/minecraft/world/entity/Entity;)Z",
            target = "net/minecraft/world/entity/Entity/isCurrentlyGlowing",
            targetDesc = "()Z"
    )
    public static boolean onShouldEntityGlow(Minecraft minecraft, Entity entity, Invocation<Entity, Boolean> original) throws Exception {
        if (OpenKoAClient.isReady()
                && OpenKoAClient.instance.getModuleManager() != null
                && ESP.INSTANCE != null
                && ESP.INSTANCE.isGlowing(entity)) {
            return true;
        }
        return original.call();
    }

    @Inject(method = "resizeDisplay", desc = "()V", at = @At(At.Type.TAIL))
    public static void onResizeDisplay(Minecraft minecraft, CallbackInfo callbackInfo) {
        Renderer.setGuiScaleVerified((float) minecraft.getWindow().getGuiScale());
    }
}
