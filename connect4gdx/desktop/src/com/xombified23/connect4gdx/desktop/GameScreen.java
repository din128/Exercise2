package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    // Constants
    private final String YELLOW_DOT = "0";
    private final String RED_DOT = "1";
    private final int NUMXSQUARE = 7;
    private final int NUMYSQUARE = 6;
    private final int SQUARESIZE = 100;
    private float groundCoord = 0;

    private final Texture yellowTexture;
    private final Texture redTexture;
    private Texture currDotTexture;
    private final Stage stage;
    private final ProjectApplication game;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private MapActor[][] mapActorList;
    private Array<Rectangle> dotsAnimationArray;
    private Array<Rectangle> dotsOnBoardArray;

    public GameScreen(final ProjectApplication game) {
        this.game = game;
        stage = new Stage();
        yellowTexture = new Texture(Gdx.files.internal("yellow.png"));
        redTexture = new Texture(Gdx.files.internal("red.png"));
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        dotsAnimationArray = new Array<Rectangle>();
        dotsOnBoardArray = new Array<Rectangle>();

        if (new Random().nextInt(2) == 0)
            currDotTexture = yellowTexture;
        else currDotTexture = redTexture;

        camera.setToOrtho(false, 1280, 720);
        createGrid(NUMXSQUARE, NUMYSQUARE);

    }

    @Override
    public void dispose() {
        yellowTexture.dispose();
        redTexture.dispose();
        shapeRenderer.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
        stage.act(delta);
        stage.draw();


        Vector2 stageCoord;
        MapActor currMapActor = null;
        if (Gdx.input.justTouched()) {
            stageCoord = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if ((stage.hit(stageCoord.x, stageCoord.y, true)) != null) {
                currMapActor = (MapActor) stage.hit(stageCoord.x, stageCoord.y, true);

                // Check if top square is filled
                if ((stage.hit(currMapActor.getX() + SQUARESIZE / 2, Gdx.graphics.getHeight() - SQUARESIZE / 2, true)) != null) {
                    MapActor topMapActor = (MapActor) stage.hit(currMapActor.getX() + SQUARESIZE / 2, Gdx.graphics.getHeight() - SQUARESIZE / 2, true);
                    if (topMapActor.getDot() == null) {
                        dotsAnimationArray.add(spawnTopPosDot(currMapActor));
                    }
                }
            }
        }

        // Animate drop
        Iterator<Rectangle> iterDotAnim = dotsAnimationArray.iterator();
        while (iterDotAnim.hasNext()) {
            Rectangle currDot = iterDotAnim.next();
            game.batch.begin();
            game.batch.draw(currDotTexture, currDot.getX(), currDot.getY(), currDot.getWidth(), currDot.getHeight());
            game.batch.end();
            currDot.y -= 1000 * Gdx.graphics.getDeltaTime();

            if ((stage.hit(currDot.getX() + SQUARESIZE / 2, currDot.getY(), true)) != null)
                currMapActor = (MapActor) stage.hit(currDot.getX() + SQUARESIZE / 2, currDot.getY(), true);

            // Handle dot when it hits another rectangle or ground
            if (currDot.y < groundCoord || currMapActor.getDot() != null) {
                float xMid = currDot.getX() + SQUARESIZE / 2;
                float yMid = currDot.getY() + SQUARESIZE / 2;
                if ((stage.hit(xMid, yMid, true)) != null) {
                    currMapActor = (MapActor) stage.hit(xMid, yMid, true);
                    dotsOnBoardArray.add(spawnCurrPosDot(currMapActor));

                    if (currDotTexture == yellowTexture) {
                        currMapActor.setDot(YELLOW_DOT);
                        currDotTexture = redTexture;
                    } else {
                        currMapActor.setDot(RED_DOT);
                        currDotTexture = yellowTexture;
                    }
                    calculateWinner();
                }
                iterDotAnim.remove();

            }
        }

        // Redraw new dot on board
        for (Rectangle dotOnBoard : dotsOnBoardArray) {
            Texture onBoardTexture;
            MapActor onBoardActor = null;
            if ((stage.hit(dotOnBoard.getX() + SQUARESIZE / 2, dotOnBoard.getY() + SQUARESIZE / 2, true)) != null)
                onBoardActor = (MapActor) stage.hit(dotOnBoard.getX() + SQUARESIZE / 2, dotOnBoard.getY(), true);

            if (onBoardActor.getDot() == YELLOW_DOT)
                onBoardTexture = yellowTexture;
            else onBoardTexture = redTexture;

            game.batch.begin();
            game.batch.draw(onBoardTexture, dotOnBoard.getX(), dotOnBoard.getY(), dotOnBoard.getWidth(), dotOnBoard.getHeight());
            game.batch.end();
        }
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
        int i;
        int j;
        for (j = 0; j < y; j++) {
            for (i = 0; i < x; i++) {
                mapActorList[i][j] = new MapActor(shapeRenderer, SQUARESIZE, i, j);
                stage.addActor(mapActorList[i][j]);
            }
        }
        groundCoord = Gdx.graphics.getHeight() - j * SQUARESIZE;
    }

    private Rectangle spawnTopPosDot(MapActor mapActor) {

        Rectangle dot = new Rectangle();
        dot.setSize(mapActor.squareSize, mapActor.squareSize);
        dot.setX(mapActor.getX());
        dot.setY(Gdx.graphics.getHeight() - (mapActor.squareSize));
        return dot;
    }

    private Rectangle spawnCurrPosDot(MapActor mapActor) {
        Rectangle dot = new Rectangle();
        dot.setSize(mapActor.squareSize, mapActor.squareSize);
        dot.setX(mapActor.getX());
        dot.setY(mapActor.getY());
        return dot;
    }

    private void calculateWinner() {
        // TODO
    }

}

