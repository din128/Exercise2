package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MapActor extends Actor {
    private final int squareSize;
    private final ShapeRenderer shapeRenderer;
    private int x;
    private int y;

    public MapActor(ShapeRenderer shapeRenderer, int squareSize, int x, int y) {
        this.squareSize = squareSize;
        this.shapeRenderer = shapeRenderer;
        this.x = x;
        this.y = y;

        setSize(squareSize, squareSize);
        setBounds(x * squareSize, Gdx.graphics.getHeight() - y * squareSize, getWidth(), getHeight());
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Test");
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(getX(), getY(), 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, squareSize, squareSize);
        shapeRenderer.end();
    }
}