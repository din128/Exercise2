package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapActor extends Actor {
    private ShapeRenderer mShapeRenderer = new ShapeRenderer();

    Texture texture = new Texture(Gdx.files.internal("assets/apiButton.png"));

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, 0, 0);
    }

	/*
    @Override
	public void draw (SpriteBatch batch, float alpha) {
		mShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		mShapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		mShapeRenderer.translate(getX(), getY(), 0);
		
		mShapeRenderer.begin(ShapeType.Rectangle);
		mShapeRenderer.setColor(Color.BLACK);
		mShapeRenderer.rect(0, 0, 50, 50);
		mShapeRenderer.end();	
		
	}
	*/
}
