package client.openkoa;

import asm.patchify.loader.ClassAgent;
import asm.patchify.loader.PatchRegistry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import client.openkoa.event.EventBus;
import client.openkoa.event.EventTarget;
import client.openkoa.event.impl.TickEvent;
import client.openkoa.gui.IntroAnimation;
import client.openkoa.manager.CommandManager;
import client.openkoa.manager.ConfigManager;
import client.openkoa.manager.HudManager;
import client.openkoa.manager.LagManager;
import client.openkoa.manager.ModuleManager;
import client.openkoa.manager.TargetManager;
import client.openkoa.patch.ChatComponentPatch;
import client.openkoa.patch.ChatScreenPatch;
import client.openkoa.patch.ClientLevelPatch;
import client.openkoa.patch.ConnectionPatch;
import client.openkoa.patch.EntityPatch;
import client.openkoa.patch.EntityRenderDispatcherPatch;
import client.openkoa.patch.EntityRendererPatch;
import client.openkoa.patch.FriendlyByteBufPatch;
import client.openkoa.patch.GuiPatch;
import client.openkoa.patch.BlockOcclusionCachePatch;
import client.openkoa.patch.GameRendererPatch;
import client.openkoa.patch.HumanoidModelPatch;
import client.openkoa.patch.ItemInHandLayerPatch;
import client.openkoa.patch.ItemInHandRendererPatch;
import client.openkoa.patch.ItemPatch;
import client.openkoa.patch.KeyboardHandlerPatch;
import client.openkoa.patch.KeyboardInputPatch;
import client.openkoa.patch.LevelRendererPatch;
import client.openkoa.patch.LivingEntityPatch;
import client.openkoa.patch.LivingEntityRendererPatch;
import client.openkoa.patch.LocalPlayerPatch;
import client.openkoa.patch.MinecraftPatch;
import client.openkoa.patch.PacketUtilsPatch;
import client.openkoa.patch.PlayerPatch;
import client.openkoa.patch.PlayerTabOverlayPatch;
import client.openkoa.asm.Bootstrap;
import client.openkoa.render.Fonts;
import client.openkoa.utils.rotation.RotationHandler;
import client.openkoa.utils.misc.Assets;

@Mod(value = "openkoa")
@Getter
@Setter
public class OpenKoAClient extends ClientBase {
    @Getter
    public static OpenKoAClient instance;
    public static final String CLIENT_NAME = "OpenKoA";
    public static final String CLIENT_NAME_UPPER = "OPENKOA";
    public static final String VERSION = "1.0";
    public static float serverTickRate;
    public static boolean isReady;
    public static boolean isMCPMapped;
    public static String configDir = System.getProperty("user.home") + File.separator + ".openkoa";
    public static String username = "";

    private static final String[] CLOUD_ASSET_NAMES = { "panel.png", "ptr.png", "lie.wav", "truth.wav" };

    private EventBus eventBus;
    private RotationHandler rotationHandler;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private HudManager hudManager;
    private LagManager lagManager;
    private TargetManager targetManager;
    private int reconnectAttempts;

    public OpenKoAClient() {
        if (instance == null) {
            instance = this;
            this.init();
        }
    }

    private void init() {
        try {
            // Started before the mapping load and the retransform so the font parsing (11 MB
            // pingfang dominates) overlaps with work that blocks the game thread anyway.
            Fonts.preloadFontsAsync();
            username = System.getProperty("user.name", "Player");
            File dir = new File(configDir);
            if (!dir.exists() && !dir.mkdirs()) {
                logger.warn("Failed to create config directory at {}", configDir);
            }
            mc = getMcInstance();
            this.eventBus = new EventBus();
            this.rotationHandler = new RotationHandler();
            this.eventBus.register(this.rotationHandler);
            this.moduleManager = new ModuleManager();
            this.hudManager = new HudManager();
            this.commandManager = new CommandManager();
            this.configManager = new ConfigManager();
            this.extractCloudAssets();
            this.lagManager = new LagManager();
            this.targetManager = new TargetManager();
            this.eventBus.register(this.hudManager);
            this.eventBus.register(this.lagManager);
            this.eventBus.register(this.targetManager);
            this.eventBus.register(this);
            this.commandManager.initCommands();
            this.eventBus.register(new IntroAnimation());
            Bootstrap.init();
            registerPatches();
            if (ClassAgent.getInstrumentation() != null) {
                ClassAgent.installPatchesAndRetransform();
            } else {
                logger.warn("ClassAgent not attached. Launch with `./gradlew runClient0` so the agent jvmArg is set.");
            }
            isReady = true;
            logger.info("{} v{} initialized.", CLIENT_NAME, VERSION);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }

    private boolean moduleInit = false;

    @EventTarget
    public void onTick(TickEvent e) {
        if (isReady() && !moduleInit) {
            moduleInit = true;
            this.moduleManager.initModules();
            this.configManager.loadAll();
        }
    }

    public static boolean isReady() {
        return instance != null
                && OpenKoAClient.instance.eventBus != null
                && isReady
                && mc != null
                && mc.player != null
                && !username.isEmpty()
                && mc.player.tickCount > 5;
    }

    public void shutdown() {
        isReady = false;
        if (this.configManager != null) {
            this.configManager.saveAll();
        }
    }

    private void extractCloudAssets() {
        File targetDir = ConfigManager.CONFIG_DIR;
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            logger.warn("Failed to create config directory at {}", targetDir);
            return;
        }
        for (String name : CLOUD_ASSET_NAMES) {
            File outFile = new File(targetDir, name);
            if (outFile.exists()) continue;
            try (InputStream in = Assets.open("/assets/openkoa/cloud_assets/" + name)) {
                if (in == null) {
                    logger.warn("Cloud asset missing on classpath: {}", name);
                    continue;
                }
                try (OutputStream out = new FileOutputStream(outFile)) {
                    in.transferTo(out);
                }
            } catch (IOException ioException) {
                logger.error("Failed to extract cloud asset {}", name, ioException);
            }
        }
    }

    public static void registerPatches() {
        PatchRegistry.register(MinecraftPatch.class);
        PatchRegistry.register(LocalPlayerPatch.class);
        PatchRegistry.register(LivingEntityPatch.class);
        PatchRegistry.register(EntityPatch.class);
        PatchRegistry.register(PlayerPatch.class);
        PatchRegistry.register(ClientLevelPatch.class);
        PatchRegistry.register(ConnectionPatch.class);
        PatchRegistry.register(PacketUtilsPatch.class);
        PatchRegistry.register(KeyboardHandlerPatch.class);
        PatchRegistry.register(KeyboardInputPatch.class);
        PatchRegistry.register(ChatScreenPatch.class);
        PatchRegistry.register(ChatComponentPatch.class);
        PatchRegistry.register(EntityRendererPatch.class);
        PatchRegistry.register(EntityRenderDispatcherPatch.class);
        PatchRegistry.register(LevelRendererPatch.class);
        PatchRegistry.register(GameRendererPatch.class);
        PatchRegistry.register(ItemInHandRendererPatch.class);
        PatchRegistry.register(ItemInHandLayerPatch.class);
        PatchRegistry.register(HumanoidModelPatch.class);
        PatchRegistry.register(LivingEntityRendererPatch.class);
        PatchRegistry.register(ItemPatch.class);
        PatchRegistry.register(PlayerTabOverlayPatch.class);
        PatchRegistry.register(FriendlyByteBufPatch.class);
        PatchRegistry.register(GuiPatch.class);

        // Compatibility patch for Embeddium/Sodium's BlockOcclusionCache.
        // Always registered so the transformer can catch the class when it
        // first loads. We must NOT use Class.forName() here — that would
        // load the class before our transformer is installed, preventing
        // the patch from ever being applied.
        PatchRegistry.register(BlockOcclusionCachePatch.class);
    }

    public static Minecraft getMcInstance() {
        Minecraft minecraft = null;
        try {
            Class<?> clazz = Minecraft.class;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() != clazz) continue;
                field.setAccessible(true);
                minecraft = (Minecraft) field.get(null);
                field.setAccessible(false);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return minecraft != null ? minecraft : Minecraft.getInstance();
    }

}
