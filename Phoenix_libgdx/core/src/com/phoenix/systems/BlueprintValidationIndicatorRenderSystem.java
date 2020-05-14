package com.phoenix.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

public class BlueprintValidationIndicatorRenderSystem extends EntitySystem
{
	public static final float BLUEPRINT_VALIDATION_INDICATOR_TRANSPARENCY_VALUE = 0.5f;

	private GameScreen gameScreen;

	public BlueprintValidationIndicatorRenderSystem(GameScreen gs)
	{
		super();
		this.gameScreen = gs;
	}

	public void update(float deltaTime)
	{
		Player player = gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);

		if (player.selectedBlueprint != null)
		{
			Circle circle = player.selectedBlueprint.validBuildIndicator;

			Color color = new Color();

			switch (player.selectedBlueprint.getValidity())
			{
				case InvalidTerrain:
				{
					color = new Color(Color.RED);
					break;
				}

				case MissingResources:
				{
					color = new Color(Color.YELLOW);
					break;
				}

				case Valid:
				{
					color = new Color(Color.GREEN);
					break;
				}
			}

			color.a = BLUEPRINT_VALIDATION_INDICATOR_TRANSPARENCY_VALUE;

			gameScreen.shapeRendererFilled.setColor(color);
			gameScreen.shapeRendererFilled.circle(circle.x, circle.y, circle.radius);
			gameScreen.shapeRendererFilled.setColor(Color.GREEN);
		}
	}
}
