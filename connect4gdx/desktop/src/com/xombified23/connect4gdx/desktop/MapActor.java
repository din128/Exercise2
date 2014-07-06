// TODO
// Add API for MapActor to store Red or Yellow Dot data
// Add API to store position of MapActorList

package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapActor extends Actor {
    public final int squareSize;
    public final ShapeRenderer shapeRenderer;
    private String currDot = null;
    private int x;
    private int y;

    public MapActor(ShapeRenderer shapeRenderer, int squareSize, int x, int y) {
        this.squareSize = squareSize;
        this.shapeRenderer = shapeRenderer;
        this.x = x;
        this.y = y;

        setSize(squareSize, squareSize);
        setBounds(x * squareSize, Gdx.graphics.getHeight() - (y + 1) * squareSize, getWidth(), getHeight());
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

    public void setDot(String currDot) {
        this.currDot = currDot;
    }

    public String getDot() {
        return currDot;
    }

    public int getXPos() {
        return this.x;
    }

    public int getYPos() {
        return this.y;
    }
}