package com.example;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

public class DoorHighlighterOverlay extends Overlay
{
    private final Client client;
    private final DoorHighlighterPlugin plugin;

    @Inject
    public DoorHighlighterOverlay(Client client, DoorHighlighterPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.MED);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        graphics.setColor(Color.GREEN);
        graphics.drawString("Overlay active", 20, 20); // Debug text

        for (int i = 0; i < plugin.getDoors().size(); ++i)
        {

            Player player = client.getLocalPlayer();

            int plane = player.getWorldLocation().getPlane();

            if (plugin.getDoors().get(i).getWorldLocation().getPlane() != plane) continue;

            if (plugin.getDoors().get(i).getClickbox() != null)

            {

                graphics.setColor(Color.green);
                    graphics.draw(plugin.getDoors().get(i).getClickbox());
            }
        }

        return null;
    }

}
