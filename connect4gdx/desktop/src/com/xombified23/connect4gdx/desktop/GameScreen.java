package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {
    // Constants
    private final int NUMXSQUARE = 7;
    private final int NUMYSQUARE = 6;
    private final int SQUARESIZE = 100;

    private final Stage stage;
    private final ProjectApplication game;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private MapActor[][] mapActorList;

    public GameScreen(final ProjectApplication game) {
        this.game = game;
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        camera.setToOrtho(false, 1280, 720);
        createGrid(NUMXSQUARE, NUMYSQUARE);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
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
        mapActorList = new MapActor[x][y];

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                mapActorList[i][j] = new MapActor(shapeRenderer, SQUARESIZE, i, j);
                stage.addActor(mapActorList[i][j]);
            }
        }
    }
}

