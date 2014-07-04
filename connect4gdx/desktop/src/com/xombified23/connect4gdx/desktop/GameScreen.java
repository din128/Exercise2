package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This is where you screen code will go, any UI should be in here
 *
 * @author Dan Lee Jo
 */
public class GameScreen implements Screen {
    // Constants
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private final int NUMXSQUARE = 7;
    private final int NUMYSQUARE = 6;
    private final int SQUARESIZE = 100;

    private final Stage stage;
    private final SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private final ProjectApplication game;

    public GameScreen(final ProjectApplication game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        stage.dispose();
        dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();

        createGrid(NUMXSQUARE, NUMYSQUARE);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {
        // Irrelevant on desktop, ignore this
    }

    @Override
    public void resume() {
        // Irrelevant on desktop, ignore this
    }

    private void createGrid(int x, int y) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                shapeRenderer.rect(SQUARESIZE * i, HEIGHT - SQUARESIZE - SQUARESIZE * j, SQUARESIZE, SQUARESIZE);
            }
        }
        shapeRenderer.end();
    }
}

