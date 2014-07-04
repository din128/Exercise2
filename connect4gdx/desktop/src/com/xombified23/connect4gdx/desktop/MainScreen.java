package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is where you screen code will go, any UI should be in here
 *
 * @author Dan Lee Jo
 */
public class MainScreen implements Screen {

    private final Stage stage;
    private BitmapFont pennyFont;
    private Table UIViews;
    private Table APIResults;
    private Texture apiTexture;
    private Texture sfxTexture;
    private Texture gameTexture;
    private final static String url = "http://api.openweathermap.org/data/2.5/weather?q=San%20Francisco,US";
    private final ProjectApplication game;

    public MainScreen(final ProjectApplication game) {
        this.game = game;
        stage = new Stage();
        UIViews = new Table();

        UIViews.setFillParent(true);
        UIViews.left();
        createTitle();
        UIViews.row();
        createSFXButton();
        createAPIButton();
        createGameButton();
        stage.addActor(UIViews);
    }

    @Override
    public void dispose() {
        stage.dispose();
        pennyFont.dispose();
        apiTexture.dispose();
        sfxTexture.dispose();
        gameTexture.dispose();
    }

    @Override
    public void render(float delta) {
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

    private void createTitle() {
        pennyFont = new BitmapFont(Gdx.files.internal("font.fnt"),
                Gdx.files.internal("font.png"), false);

        LabelStyle pennypopStyle = new LabelStyle(pennyFont, Color.RED);
        Label pennypopLabel = new Label("PennyPop", pennypopStyle);
        UIViews.add(pennypopLabel);
    }

    private void createSFXButton() {
        sfxTexture = new Texture(Gdx.files.internal("sfxButton.png"));
        Image sfxButton = new Image(sfxTexture);
        UIViews.add(sfxButton);

        sfxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playSFX();
            }
        });
    }

    private void createGameButton() {
        gameTexture = new Texture(Gdx.files.internal("gameButton.png"));
        Image gameButton = new Image(gameTexture);
        UIViews.add(gameButton);

        gameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
    }

    private void createAPIButton() {
        apiTexture = new Texture(Gdx.files.internal("apiButton.png"));
        Image apiImage = new Image(apiTexture);
        UIViews.add(apiImage);

        apiImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Create API results
                APIResults = new Table();
                APIResults.reset();
                APIResults.setFillParent(true);
                APIResults.right();
                stage.addActor(APIResults);

                try {
                    Color brown = new Color(0.5f, 0.4f, 0, 1);
                    LabelStyle brownStyle = new LabelStyle(pennyFont, brown);
                    LabelStyle blueStyle = new LabelStyle(pennyFont, Color.BLUE);
                    LabelStyle redStyle = new LabelStyle(pennyFont, Color.RED);

                    // Extract information from JSON
                    JSONObject jsonRes = getAPIResults();
                    JSONObject clouds = jsonRes.getJSONObject("clouds");
                    JSONObject wind = jsonRes.getJSONObject("wind");
                    JSONObject main = jsonRes.getJSONObject("main");

                    String city = jsonRes.getString("name");
                    double numClouds = clouds.getDouble("all");
                    double windSpeed = wind.getDouble("speed");
                    double temp = main.getDouble("temp"); // Temperature in Kelvin?

                    temp = Math.floor((temp * 1.8 - 459.67) * 100) / 100; // Kelvin to Farenheit... looks more reasonable
                    boolean clear = false;
                    if (numClouds == 0) {
                        clear = true;
                    }

                    // Add data to table
                    Label currWeather = new Label("CurrentWeather", brownStyle);
                    APIResults.add(currWeather);
                    APIResults.row();

                    Label currCity = new Label(city, blueStyle);
                    APIResults.add(currCity);
                    APIResults.row();

                    Label skyCondition;
                    if (clear) {
                        skyCondition = new Label("Sky is clear", redStyle);
                    } else {
                        skyCondition = new Label("Sky is cloudy", redStyle);
                    }
                    APIResults.add(skyCondition);
                    APIResults.row();

                    String degrees;
                    degrees = temp + " degrees, ";
                    Label degreesLabel = new Label(degrees, redStyle);
                    APIResults.add(degreesLabel);

                    String windSpeedString;
                    windSpeedString = windSpeed + "mph wind";
                    Label windSpeedLabel = new Label(windSpeedString, redStyle);
                    APIResults.add(windSpeedLabel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static JSONObject getAPIResults() {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json");

            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = rd.readLine()) != null) {
                sb.append(output);
            }
            System.out.println("JSONObject = " + sb.toString());

            return new JSONObject(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void playSFX() {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("button_click.wav"));
        sound.play(1.0f);
    }
}
