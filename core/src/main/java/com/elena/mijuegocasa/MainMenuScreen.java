package com.elena.mijuegocasa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    private final Main juego;
    private Stage stage;
    private SpriteBatch batch;

    private Texture fondo;
    private Texture botonTex;
    private BitmapFont fontHorror;

    private float uiScale = 1f;

    private static final float WORLD_WIDTH  = 846;
    private static final float WORLD_HEIGHT = 392;

    public MainMenuScreen(Main game) {
        this.juego = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        fondo    = new Texture("MenuPrincipal/imagenInicio.png");
        botonTex = new Texture("MenuPrincipal/boton.png");

        try {
            fontHorror = new BitmapFont(
                Gdx.files.internal("letras/realhorror.fnt"),
                false
            );

            fontHorror.getData().setScale(1.4f);
            fontHorror.setColor(0.9f, 0.1f, 0.1f, 1f);

        } catch (Exception e) {
            Gdx.app.error("MainMenuScreen", "No se pudo cargar realhorror.fnt", e);
            fontHorror = new BitmapFont();
            fontHorror.getData().setScale(2.0f);
            fontHorror.setColor(Color.WHITE);
        }

        updateUiScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up   = new TextureRegionDrawable(new TextureRegion(botonTex));
        style.down = new TextureRegionDrawable(new TextureRegion(botonTex));
        style.font = fontHorror;
        style.fontColor = Color.WHITE;

        // Tabla alineada a la izquierda (como siempre)
        Table table = new Table();
        table.setFillParent(true);
        table.left().padLeft(30 * uiScale);

        // Botones
        TextButton nuevaBtn  = new TextButton("NUEVA PARTIDA", style);
        TextButton logrosBtn = new TextButton("Records",       style);
        TextButton configBtn = new TextButton("Configuracion", style);
        TextButton salirBtn  = new TextButton("Salir",         style);

        float buttonWidth  = 400 * uiScale;
        float buttonHeight = 250 * uiScale;

        // Padding INTERNO (margen dentro del botón para que el texto cuadre)
        float padVertical   = 25 * uiScale;
        float padHorizontal = 45 * uiScale;

        nuevaBtn.getLabelCell()  .pad(padVertical, padHorizontal, padVertical, padHorizontal);
        logrosBtn.getLabelCell() .pad(padVertical, padHorizontal, padVertical, padHorizontal);
        configBtn.getLabelCell() .pad(padVertical, padHorizontal, padVertical, padHorizontal);
        salirBtn.getLabelCell()  .pad(padVertical, padHorizontal, padVertical, padHorizontal);

        nuevaBtn.getLabel().setWrap(true);
        logrosBtn.getLabel().setWrap(true);
        configBtn.getLabel().setWrap(true);
        salirBtn.getLabel().setWrap(true);

        // Listeners
        nuevaBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                juego.juegoEmpezado = true;
                juego.tiempoJugado = 0f;
                juego.setScreen(new NuevaPartida(juego));
            }
        });

        logrosBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Menu", "Records clickeado");
            }
        });

        configBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Menu", "Configuración clickeado");
                // juego.setScreen(new Configuración(juego));
            }
        });

        salirBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Espacio muy pequeño entre botones para que queden juntitos
        float spacingMuyPequeno = 5 * uiScale;  // ← antes era más grande, ahora muy reducido

        table.add(nuevaBtn)  .width(buttonWidth).height(buttonHeight).padBottom(spacingMuyPequeno).left().row();
        table.add(logrosBtn) .width(buttonWidth).height(buttonHeight).padBottom(spacingMuyPequeno).left().row();
        table.add(configBtn) .width(buttonWidth).height(buttonHeight).padBottom(spacingMuyPequeno).left().row();
        table.add(salirBtn)  .width(buttonWidth).height(buttonHeight).padBottom(0).left().row();  // sin espacio abajo

        stage.addActor(table);
    }

    private void updateUiScale(int width, int height) {
        float scaleX = (float) width  / WORLD_WIDTH;
        float scaleY = (float) height / WORLD_HEIGHT;
        uiScale = Math.max(scaleX, scaleY);

        if (fontHorror != null) {
            fontHorror.getData().setScale(uiScale * 1.4f);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();

        float screenW = stage.getViewport().getWorldWidth();
        float screenH = stage.getViewport().getWorldHeight();

        float scale = Math.min(screenW / fondo.getWidth(), screenH / fondo.getHeight());
        float drawW = fondo.getWidth() * scale;
        float drawH = fondo.getHeight() * scale;
        float drawX = (screenW - drawW) / 2f;
        float drawY = (screenH - drawH) / 2f;

        batch.draw(fondo, drawX, drawY, drawW, drawH);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateUiScale(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        fondo.dispose();
        botonTex.dispose();
        if (fontHorror != null) fontHorror.dispose();
    }
}
