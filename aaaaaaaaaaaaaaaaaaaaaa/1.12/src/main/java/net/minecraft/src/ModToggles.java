package com.isacofff.clientbase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

/**
 * Small client-side toggle manager used by EntityRenderer.
 *
 * Keys:
 *   Right Shift = show/hide this menu
 *   G            = FullBright
 *   Z            = Zoom
 */
public final class ModToggles
{
    public static final int KEY_MENU = Keyboard.KEY_RSHIFT;
    public static final int KEY_FULLBRIGHT = Keyboard.KEY_G;
    public static final int KEY_ZOOM = Keyboard.KEY_Z;

    private static boolean fullBright;
    private static boolean zoom;
    private static boolean menuOpen;

    private static boolean lastMenuKey;
    private static boolean lastFullBrightKey;
    private static boolean lastZoomKey;

    private ModToggles()
    {
    }

    /** Called once per rendered frame by EntityRenderer. */
    public static void update(Minecraft mc)
    {
        if (mc == null)
        {
            return;
        }

        // Do not steal keys while another GUI is open.
        boolean allowKeys = mc.currentScreen == null;

        boolean menuKey = allowKeys && Keyboard.isKeyDown(KEY_MENU);
        boolean fullBrightKey = allowKeys && Keyboard.isKeyDown(KEY_FULLBRIGHT);
        boolean zoomKey = allowKeys && Keyboard.isKeyDown(KEY_ZOOM);

        if (menuKey && !lastMenuKey)
        {
            menuOpen = !menuOpen;
        }

        if (fullBrightKey && !lastFullBrightKey)
        {
            fullBright = !fullBright;
        }

        if (zoomKey && !lastZoomKey)
        {
            zoom = !zoom;
        }

        lastMenuKey = menuKey;
        lastFullBrightKey = fullBrightKey;
        lastZoomKey = zoomKey;
    }

    public static boolean isFullBright()
    {
        return fullBright;
    }

    public static boolean isZoomEnabled()
    {
        return zoom;
    }

    public static boolean isMenuOpen()
    {
        return menuOpen;
    }

    public static void setFullBright(boolean enabled)
    {
        fullBright = enabled;
    }

    public static void setZoomEnabled(boolean enabled)
    {
        zoom = enabled;
    }

    public static void setMenuOpen(boolean open)
    {
        menuOpen = open;
    }

    /**
     * Draws a simple toggle list in the upper-left corner.
     * This is deliberately rendered from EntityRenderer so it appears over the game.
     */
    public static void renderOverlay(Minecraft mc)
    {
        if (mc == null || !menuOpen || mc.world == null || mc.currentScreen != null || mc.gameSettings.hideGUI)
        {
            return;
        }

        int x = 8;
        int y = 8;
        int width = 150;
        int height = 76;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        Gui.drawRect(x, y, x + width, y + height, 0xD0101418);
        Gui.drawRect(x, y, x + width, y + 2, 0xFF5FA8A0);

        mc.fontRenderer.drawStringWithShadow("MOD TOGGLES", x + 8, y + 7, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(
                "FullBright: " + (fullBright ? "ON" : "OFF"),
                x + 8, y + 23, fullBright ? 0xFF55FF55 : 0xFFFF5555);
        mc.fontRenderer.drawStringWithShadow(
                "Zoom: " + (zoom ? "ON" : "OFF"),
                x + 8, y + 38, zoom ? 0xFF55FF55 : 0xFFFF5555);
        mc.fontRenderer.drawStringWithShadow("RSHIFT: hide menu", x + 8, y + 53, 0xFFAAAAAA);
        mc.fontRenderer.drawStringWithShadow("G / Z: toggle", x + 8, y + 65, 0xFFAAAAAA);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}

