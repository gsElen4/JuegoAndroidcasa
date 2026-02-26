package com.elena.mijuegocasa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NuevaPartida implements Screen {
    public static final float WORLD_WIDTH = 1920;
    public static final float WORLD_HEIGHT = 1080;

    private final Main juego;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    //Tiempo de pantalla y tiempo máximo para cambio de pantalla
    private float tiempoAcumulado = 0f; //tiempo inicial desde 0
    private final float tiempoLimite = 5f; //tiempo limite de 5 segundos antes de cambiar de pantalla

    //Parallax para el fondo
    private final ParallaxVista fondo;
    private final ParallaxVista nubes;
    private final ParallaxVista arboles;
    private final ParallaxVista sombra;

    public NuevaPartida(Main game) {
        this.juego = game;
        this.batch = juego.getBatch();
        this.camera = juego.getCamera();
        this.viewport = juego.getViewport();

        fondo = new ParallaxVista("fondo.png", 0f, WORLD_HEIGHT);
        nubes = new ParallaxVista("nubes.png", 20f, WORLD_HEIGHT);
        arboles = new ParallaxVista("arbolesMontaje.png", 40f, WORLD_HEIGHT);
        sombra = new ParallaxVista("sombra.png", 0f, WORLD_HEIGHT);

//log
        Gdx.app.log("GameScreen", "Pantalla de juego creada");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        Gdx.gl.glClearColor(0.05f, 0.1f, 0.15f, 1f);
    }

    @Override
    public void render(float delta) {
        tiempoAcumulado += delta; //acumula tiempo

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (tiempoAcumulado >= tiempoLimite) {
            juego.setScreen(new Piso1(juego)); //cambia pantalla
            dispose();
        }

        fondo.update(delta);
        nubes.update(delta);
        arboles.update(delta);
        sombra.update(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        fondo.draw(batch);
        nubes.draw(batch);
        arboles.draw(batch);
        sombra.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        // pausar música, guardar progreso parcial, etc.
    }

    @Override
    public void dispose() {

        fondo.dispose();
        nubes.dispose();
        arboles.dispose();
        sombra.dispose();
    }
}
