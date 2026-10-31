package client.openkoa.gui.newclickgui;

import lombok.Getter;
import lombok.Generated;
import client.openkoa.gui.newclickgui.CategoryPanel;
import client.openkoa.gui.newclickgui.UIElement;
import client.openkoa.settings.Setting;
import client.openkoa.utils.animation.SmoothAnimationTimer;

public abstract class SettingElement<T extends Setting<?>>
extends UIElement {
    @Getter
    protected final CategoryPanel parentPanel;
    @Getter
    protected final T setting;
    @Getter
    protected final SmoothAnimationTimer visibilityTimer = new SmoothAnimationTimer();

    @Generated
    public SettingElement(CategoryPanel categoryPanel, T setting) {
        this.parentPanel = categoryPanel;
        this.setting = setting;
    }
}