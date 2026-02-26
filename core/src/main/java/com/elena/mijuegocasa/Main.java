package com.elena.mijuegocasa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends Game {

    public static final float WORLD_WIDTH  = 1920;
    public static final float WORLD_HEIGHT = 1080;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport viewport;
    public BitmapFont font;

    // ← Tiempo total jugado (se suma en los pisos)
    public float tiempoJugado = 0f;

    // ← Bandera para empezar a contar SOLO desde Piso 1
    public boolean juegoEmpezado = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Ir al menú principal (aquí tiempoJugado = 0 y juegoEmpezado = false)
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();

        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    // Getters
    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
