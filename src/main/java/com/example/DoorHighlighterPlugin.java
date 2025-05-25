package com.example;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PluginDescriptor(
		name = "Door Highlighter"
)
@Slf4j
public class DoorHighlighterPlugin extends Plugin
{
	private static final Logger log = LoggerFactory.getLogger(DoorHighlighterPlugin.class);
	@Inject private Client client;
	@Inject private OverlayManager overlayManager;
	@Inject private DoorHighlighterOverlay overlay;

	private final List<TileObject> doors = new ArrayList<>();

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		doors.clear();
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		GameObject obj = event.getGameObject();
		if (obj == null || client == null)
		{
			return;
		}

		int id = obj.getId();
		ObjectComposition comp = client.getObjectDefinition(id);
		if (comp == null)
		{
			return;
		}

		log.info(String.valueOf(comp.getVarbitId()));
		// Temporarily skip impostor to avoid crashes
		// comp = comp.getImpostor();

		String name = comp.getName();
		if (name == null)
		{
			return;
		}

		if (name.toLowerCase().contains("door"))
		{
			log.info("door added");
			doors.add(obj);
		}
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		WallObject obj = event.getWallObject();
		if (obj == null || client == null)
		{
			return;
		}

		int id = obj.getId();
		ObjectComposition comp = client.getObjectDefinition(id);
		if (comp == null)
		{
			return;
		}

		// Temporarily skip impostor to avoid crashes
		// comp = comp.getImpostor();

		String name = comp.getName();
		if (name == null)
		{
			return;
		}

		if (name.toLowerCase().contains("door"))
		{
			log.info("door added");
			doors.add(obj);
		}
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		doors.remove(event.getWallObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		doors.remove(event.getGameObject());
	}

	public List<TileObject> getDoors() {
		return doors;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOADING) {
			doors.clear();
		}
	}
}