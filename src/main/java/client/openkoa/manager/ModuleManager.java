package client.openkoa.manager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import client.openkoa.ClientBase;
import client.openkoa.OpenKoAClient;
import client.openkoa.event.impl.KeyEvent;
import client.openkoa.exception.ModuleNotFoundException;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.modules.impl.combat.AntiBots;
import client.openkoa.modules.impl.combat.AntiFireball;
import client.openkoa.modules.impl.combat.AntiKB;
import client.openkoa.modules.impl.combat.AutoOffHand;
import client.openkoa.modules.impl.combat.AutoSoup;
import client.openkoa.modules.impl.combat.AutoThrow;
import client.openkoa.modules.impl.combat.Backtrack;
import client.openkoa.modules.impl.combat.Critical;
import client.openkoa.modules.impl.combat.CrystalAura;
import client.openkoa.modules.impl.combat.FakeLag;
import client.openkoa.modules.impl.combat.KillAura;
import client.openkoa.modules.impl.exploit.Disabler;
import client.openkoa.modules.impl.exploit.FastPlace;
import client.openkoa.modules.impl.misc.AimAssist;
import client.openkoa.modules.impl.misc.AutoClicker;
import client.openkoa.modules.impl.misc.AutoRod;
import client.openkoa.modules.impl.misc.KillSay;
import client.openkoa.modules.impl.misc.MusicPlayer;
import client.openkoa.modules.impl.misc.SafeWalk;
import client.openkoa.modules.impl.movement.CollisionSpeed;
import client.openkoa.modules.impl.movement.SpeedModule;
import client.openkoa.modules.impl.movement.NoSlow;
import client.openkoa.modules.impl.movement.FastWeb;
import client.openkoa.modules.impl.movement.FireballBlink;
import client.openkoa.modules.impl.movement.Fly;
import client.openkoa.modules.impl.movement.HighJump;
import client.openkoa.modules.impl.movement.NoDelay;
import client.openkoa.modules.impl.movement.NoPush;
import client.openkoa.modules.impl.movement.Scaffold;
import client.openkoa.modules.impl.movement.Sprint;
import client.openkoa.modules.impl.movement.TargetStrafe;
import client.openkoa.modules.impl.player.AntiTNT;
import client.openkoa.modules.impl.player.AntiVoid;
import client.openkoa.modules.impl.player.AntiWeb;
import client.openkoa.modules.impl.player.AutoMLG;
import client.openkoa.modules.impl.player.AutoWebPlace;
import client.openkoa.modules.impl.player.ChestStealer;
import client.openkoa.modules.impl.player.GhostHand;
import client.openkoa.modules.impl.player.Helper;
import client.openkoa.modules.impl.player.InventoryManager;
import client.openkoa.modules.impl.player.MidPearl;
import client.openkoa.modules.impl.player.NoFall;
import client.openkoa.modules.impl.player.Stuck;
import client.openkoa.modules.impl.render.*;
import client.openkoa.modules.impl.world.AntiStaff;
import client.openkoa.modules.impl.world.AutoPlay;
import client.openkoa.modules.impl.world.AutoTools;
import client.openkoa.modules.impl.world.BlockIn;
import client.openkoa.modules.impl.world.Debugger;
import client.openkoa.modules.impl.world.PacketBreak;
import client.openkoa.modules.impl.world.Teams;
import client.openkoa.modules.impl.world.Protocol;
import client.openkoa.modules.impl.world.WebUI;
import client.openkoa.event.EventTarget;

public class ModuleManager extends ClientBase {
    private final Map<String, Module> moduleMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Module> modulesByClass = new ConcurrentHashMap<>();
    private final Map<String, Module> modulesByName = new ConcurrentHashMap<>();
    private volatile List<Module> moduleSnapshot = List.of();
    private volatile Map<Category, List<Module>> modulesByCategory = Map.of();
    private volatile boolean cachesDirty = true;

    public ModuleManager() {
        OpenKoAClient.getInstance().getEventBus().register(this);
    }

    public void initModules() {
        this.register(new AntiBots());
        this.register(new AntiFireball());
        this.register(new AntiKB());
        this.register(new AutoOffHand());
        this.register(new AutoSoup());
        this.register(new AutoThrow());
        this.register(new Backtrack());
        this.register(new Critical());
        this.register(new CrystalAura());
        this.register(new KillAura());
        this.register(new FakeLag());

        this.register(new Disabler());
        this.register(new FastPlace());

        this.register(new AimAssist());
        this.register(new AutoClicker());
        this.register(new AutoRod());
        this.register(new SafeWalk());
        this.register(new MusicPlayer());
        this.register(new KillSay());

        this.register(new SpeedModule());
        this.register(new CollisionSpeed());
        this.register(new NoSlow());
        this.register(new FastWeb());
        this.register(new FireballBlink());
        this.register(new Fly());
        this.register(new HighJump());
        this.register(new NoDelay());
        this.register(new NoPush());
        this.register(new Scaffold());
        this.register(new Sprint());
        this.register(new TargetStrafe());

        this.register(new AntiTNT());
        this.register(new AntiVoid());
        this.register(new AntiWeb());
        this.register(new AutoMLG());
        this.register(new AutoWebPlace());
        this.register(new ChestStealer());
        this.register(new GhostHand());
        this.register(new Helper());
        this.register(new InventoryManager());
        this.register(new MidPearl());
        this.register(new NoFall());
        this.register(new Stuck());

        this.register(new AspectRatio());
        this.register(new BetterChat());
        this.register(new BetterFPS());
        this.register(new TerrainLOD());
        this.register(new ChestESP());
        this.register(new ClickGuiModule());
        this.register(new Compass());
        this.register(new DamageGlow());
        this.register(new ESP());
        this.register(new EntityEditor());
        this.register(new EntityDuang());
        this.register(new AudioVisualizer());
        this.register(new FakeAntiAim());
        this.register(new FullBright());
        this.register(new Interface());
        this.register(new ItemTags());
        this.register(new LyricsModule());
        this.register(new NameProtect());
        this.register(new NameTags());
        this.register(new NoFov());
        this.register(new NoHurtCam());
        this.register(new OldHitting());
        this.register(new Projectiles());
        this.register(new TimeWeather());
        this.register(new Watermark());
        this.register(new XRay());

        this.register(new AntiStaff());
        this.register(new AutoPlay());
        this.register(new AutoTools());
        this.register(new BlockIn());
        this.register(new Debugger());
        this.register(new PacketBreak());
        this.register(new Teams());
        this.register(new WebUI());
        this.register(new Protocol());
    }

    public void register(Module module) {
        this.moduleMap.put(module.getClass().getSimpleName(), module);
        this.modulesByClass.put(module.getClass(), module);
        this.modulesByName.put(normalizeName(module.getName()), module);
        module.registerSettings();
        // Rebuilding on every registration made the snapshot O(n^2): ~80 registrations each
        // re-copied the list and re-sorted seven categories. Mark dirty and rebuild on read.
        this.cachesDirty = true;
    }

    // 索引键是去掉空格的模块名；查询键沿用原先的行为，不对入参做去空格处理。
    public Module getModule(String string) {
        Module module = this.modulesByName.get(string.toLowerCase(Locale.ROOT));
        if (module == null) {
            throw new ModuleNotFoundException();
        }
        return module;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        Module module = this.modulesByClass.get(clazz);
        if (module == null) {
            throw new ModuleNotFoundException();
        }
        return clazz.cast(module);
    }

    public List<Module> getModules() {
        this.ensureCaches();
        return this.moduleSnapshot;
    }

    public List<Module> getModulesByCategory(Category category) {
        this.ensureCaches();
        return this.modulesByCategory.getOrDefault(category, List.of());
    }

    private synchronized void ensureCaches() {
        if (this.cachesDirty) {
            this.rebuildCaches();
            this.cachesDirty = false;
        }
    }

    private static String normalizeName(String name) {
        return name.replace(" ", "").toLowerCase(Locale.ROOT);
    }

    private void rebuildCaches() {
        List<Module> snapshot = List.copyOf(this.moduleMap.values());
        this.moduleSnapshot = snapshot;
        Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            List<Module> modules = new ArrayList<>();
            for (Module module : snapshot) {
                if (module.getCategory().equals(category)) {
                    modules.add(module);
                }
            }
            modules.sort((a, b) -> a.getName().compareTo(b.getName()));
            byCategory.put(category, List.copyOf(modules));
        }
        this.modulesByCategory = byCategory;
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        if (mc.screen == null && event.isPressed()) {
            for (Module module : this.getModules()) {
                if (module.getKey() != 0 && module.getKey() == event.getKeyCode()) {
                    module.toggle();
                }
            }
        }
    }
}
