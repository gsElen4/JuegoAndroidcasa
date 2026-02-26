package com.elena.mijuegocasa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class finalPerder implements Screen {

    private static final float VIEW_WIDTH = 1920f;
    private static final float VIEW_HEIGHT = 1080f;

    private final Main juego;
    private final float playTimeSeconds;

    private final SpriteBatch batch;
    private final Texture bgTexture;
    private final Sprite bgSprite;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Rectangle btnRect;
    private final Vector3 touchPos;

    public finalPerder(Main juego,float playTimeSeconds) {
        this.juego = juego;
        this.playTimeSeconds = playTimeSeconds;
        this.batch = juego.getBatch();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        camera.position.set(VIEW_WIDTH / 2f, VIEW_HEIGHT / 2f, 0);

        // Fondo
        bgTexture = new Texture("pantallafinal/perder/imagenfondo.png");
        bgSprite = new Sprite(bgTexture);
        bgSprite.setSize(VIEW_WIDTH, VIEW_HEIGHT);
        bgSprite.setPosition(0, 0);

        font = new BitmapFont();
        font.setColor(1f, 1f, 1f, 1f);

        glyphLayout = new GlyphLayout();

        shapeRenderer = new ShapeRenderer();

        // Rectángulo del botón "REINICIAR" (abajo centrado)
        btnRect = new Rectangle();
        btnRect.width = 400f;
        btnRect.height = 100f;
        btnRect.x = (VIEW_WIDTH - btnRect.width) / 2f;
        btnRect.y = 150f;

        touchPos = new Vector3();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        viewport.apply();

        // Dibujar fondo
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        bgSprite.draw(batch);
        batch.end();

        // Dibujar rectángulo del botón
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.1f, 0.2f, 0.9f);
        shapeRenderer.rect(btnRect.x, btnRect.y, btnRect.width, btnRect.height);
        shapeRenderer.end();

        // Textos
        batch.begin();

        // Tiempo jugado
        String tiempoStr = String.format("Tiempo jugado: %02d:%02d", (int)(playTimeSeconds / 60f), (int)(playTimeSeconds % 60f));
        glyphLayout.setText(font, tiempoStr);
        font.getData().setScale(2.5f);
        font.draw(batch, tiempoStr, VIEW_WIDTH / 2f - glyphLayout.width / 2f, VIEW_HEIGHT * 0.75f);

        // Botón REINICIAR
        glyphLayout.setText(font, "REINICIAR");
        font.getData().setScale(4f);
        font.draw(batch, "REINICIAR", VIEW_WIDTH / 2f - glyphLayout.width / 2f, btnRect.y + btnRect.height * 0.75f);

        font.getData().setScale(1f); // Reset scale
        batch.end();

        // Detectar toque en botón
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            viewport.unproject(touchPos);
            if (btnRect.contains(touchPos.x, touchPos.y)) {
                // Cambiar a pantalla principal
                juego.setScreen(new MainMenuScreen(juego));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(VIEW_WIDTH / 2f, VIEW_HEIGHT / 2f, 0);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        bgTexture.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
