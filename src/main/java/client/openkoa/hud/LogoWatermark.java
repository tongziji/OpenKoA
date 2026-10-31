package client.openkoa.hud;

import client.openkoa.ClientBase;
import client.openkoa.OpenKoAClient;
import client.openkoa.event.impl.GlRenderEvent;
import client.openkoa.render.DrawContext;
import client.openkoa.render.FontPresets;
import client.openkoa.render.FontRenderer;

public class LogoWatermark {
    private static final String LOGO = "N";
    private static final FontRenderer LOGO_FONT = FontPresets.openkoaIcon(96.0f);

    public void onGlRender(GlRenderEvent event) {
        if (ClientBase.mc.player == null || OpenKoAClient.getInstance().getHudManager() == null) {
            return;
        }
        ModuleListHud moduleList = OpenKoAClient.getInstance().getHudManager().getHudElement(ModuleListHud.class);
        int topColor = moduleList == null ? 0xFFFFFFFF : moduleList.getThemeColor(0, 0.0f, 1);
        int bottomColor = moduleList == null ? 0xFFFFFFFF : moduleList.getThemeColor(1, 1.0f, 1);
        DrawContext drawContext = event.drawContext();
        drawContext.drawStringGradient(LOGO, -2.0f, 18.0f + LOGO_FONT.getMetrics().capHeight(), LOGO_FONT, topColor, bottomColor);
    }
}
