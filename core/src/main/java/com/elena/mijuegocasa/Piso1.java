package com.elena.mijuegocasa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Piso1 implements Screen {

    public static final float VIEW_WIDTH  = 1920;
    public static final float VIEW_HEIGHT = 1080;

    public static final float WORLD_WIDTH  = 5000;
    public static final float WORLD_HEIGHT = 1080;

    private final Main juego;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;

    // Protagonista
    private final Sprite protagonistaSprite;
    private final Animation<TextureRegion> CaminarDer;
    private final Animation<TextureRegion> CaminarIzq;
    private final TextureRegion QuietoDer;
    private final TextureRegion QuietoIzq;
    private final Texture texturaCaminar;
    private final Texture texturaQuieto;

    private float playerX;
    private final float playerY = WORLD_HEIGHT * 0.35f;
    private final float playerSpeed = 300f;
    private boolean mirandoDerecha = true;
    private boolean isMoving = false;

    private float stateTime = 0f;

    // Fondo
    private final Sprite oficinaSprite;
    private final Texture oficinaTexture;

    // Trigger para cambiar de pantalla
    private static final float TRIGGER_X = 2500f;
    private static final float TRIGGER_TOLERANCE = 120f;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private boolean enZonaTrigger = false;
    private final BitmapFont fontMensaje;
    private final String mensajeTrigger = "Pulsa flecha para arriba para subir ";


    // Controles en pantalla (botones táctiles)
    private final Stage stageUI;
    private final ImageButton btnLeft;
    private final ImageButton btnRight;
    private final ImageButton btnUp;
    private ImageButton btnDown;
    private boolean touchLeft  = false;
    private boolean touchRight = false;
    private boolean touchUp    = false;
    private final boolean touchDown    = false;

    public Piso1(Main juego) {
        this.juego = juego;
        this.batch = juego.getBatch();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);

        playerX = WORLD_WIDTH / 2f;

        // Animaciones del protagonista
        texturaCaminar = new Texture("protagonista/spriteprotagonista.png");
        int FRAME_COLS = 2;
        int FRAME_ROWS = 1;

        TextureRegion[][] tmp = TextureRegion.split(texturaCaminar,
            texturaCaminar.getWidth() / FRAME_COLS,
            texturaCaminar.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFramesDerecha = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFramesDerecha[index++] = tmp[i][j];
            }
        }

        CaminarDer = new Animation<>(0.12f, walkFramesDerecha);
        CaminarDer.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] walkFramesIzquierda = new TextureRegion[walkFramesDerecha.length];
        for (int i = 0; i < walkFramesDerecha.length; i++) {
            walkFramesIzquierda[i] = new TextureRegion(walkFramesDerecha[i]);
            walkFramesIzquierda[i].flip(true, false);
        }

        CaminarIzq = new Animation<>(0.12f, walkFramesIzquierda);
        CaminarIzq.setPlayMode(Animation.PlayMode.LOOP);

        // Quieto
        texturaQuieto = new Texture("protagonista/pquietofrente.png");
        QuietoDer = new TextureRegion(texturaQuieto);
        QuietoIzq = new TextureRegion(texturaQuieto);
        QuietoIzq.flip(true, false);

        protagonistaSprite = new Sprite(QuietoDer);
        protagonistaSprite.setSize(100f, 200f);
        protagonistaSprite.setOriginCenter();

        // Fondo
        oficinaTexture = new Texture("fondoOfi/fondoOfi.png");
        oficinaSprite = new Sprite(oficinaTexture);
        oficinaSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        oficinaSprite.setPosition(0, 0);

        // Mensaje
        fontMensaje = new BitmapFont();
        fontMensaje.getData().setScale(2.0f);
        fontMensaje.setColor(1, 1, 0.8f, 1);

        // ── Botones en pantalla ─────────────────────────────────────────────────
        stageUI = new Stage(new ScreenViewport());
        // Nota: si ya tienes InputMultiplexer en la clase Main, agrégalo allí en vez de aquí
        Gdx.input.setInputProcessor(stageUI);

        Texture texLeft  = new Texture("controles/flechaizquierda.png");
        Texture texRight = new Texture("controles/flechaderecha.png");
        Texture texUp    = new Texture("controles/flechaarriba.png");

        // Botón izquierda
        ImageButton.ImageButtonStyle styleLeft = new ImageButton.ImageButtonStyle();
        styleLeft.imageUp = new TextureRegionDrawable(texLeft);
        styleLeft.imageDown = new TextureRegionDrawable(texLeft);
        btnLeft = new ImageButton(styleLeft);
        btnLeft.setSize(80, 80);
        btnLeft.setPosition(40, 40+20);

        // Botón derecha
        ImageButton.ImageButtonStyle styleRight = new ImageButton.ImageButtonStyle();
        styleRight.imageUp = new TextureRegionDrawable(texRight);
        styleRight.imageDown = new TextureRegionDrawable(texRight);
        btnRight = new ImageButton(styleRight);
        btnRight.setSize(80, 80);
        btnRight.setPosition(40 + 80 + 40, 40+20);   // 300 aprox

        // Botón arriba
        ImageButton.ImageButtonStyle styleUp = new ImageButton.ImageButtonStyle();
        styleUp.imageUp = new TextureRegionDrawable(texUp);
        styleUp.imageDown = new TextureRegionDrawable(texUp);
        btnUp = new ImageButton(styleUp);
        btnUp.setSize(80, 80);
        // centrado entre izquierda y derecha y más arriba
        float centerX = (btnLeft.getX() + btnRight.getX() + btnRight.getWidth()) / 2f - btnUp.getWidth() / 2f;
        btnUp.setPosition(centerX, 40 + 80 + 20);   // 20 píxeles de separación vertical


        // Listeners
        btnLeft.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchLeft = true; return true;
            }
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchLeft = false;
            }
        });

        btnRight.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchRight = true; return true;
            }
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchRight = false;
            }
        });

        btnUp.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = true; return true;
            }
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchUp = false;
            }
        });

        stageUI.addActor(btnLeft);
        stageUI.addActor(btnRight);
        stageUI.addActor(btnUp);
    }

    @Override
    public void render(float delta) {

        if(juego.juegoEmpezado){
            juego.tiempoJugado +=delta;
        }


        stateTime += delta;

        stageUI.act(delta);

        // ── Entrada combinada ───────────────────────────────────────
        boolean keyLeft  = Gdx.input.isKeyPressed(Input.Keys.LEFT)  || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean keyRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean keyDown  = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        boolean movingLeft  = keyLeft  || touchLeft;
        boolean movingRight = keyRight || touchRight;

        isMoving = movingLeft || movingRight;

        if (movingRight) {
            playerX += playerSpeed * delta;
            mirandoDerecha = true;
        }
        if (movingLeft) {
            playerX -= playerSpeed * delta;
            mirandoDerecha = false;
        }

        float mitadAncho = protagonistaSprite.getWidth() / 2f;
        playerX = MathUtils.clamp(playerX, mitadAncho, WORLD_WIDTH - mitadAncho);

        // Trigger (ahora también se puede activar con el botón arriba)
        float distancia = Math.abs(playerX - TRIGGER_X);
        enZonaTrigger = distancia <= TRIGGER_TOLERANCE;

        if (enZonaTrigger && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || touchUp)) {
            juego.setScreen(new Piso2(juego));
            return;
        }

        // Cámara
        camera.position.x = playerX;
        camera.position.y = playerY + protagonistaSprite.getHeight() * 0.6f;

        float camHalfW = viewport.getWorldWidth() / 2f;
        float camHalfH = viewport.getWorldHeight() / 2f;
        camera.position.x = MathUtils.clamp(camera.position.x, camHalfW, WORLD_WIDTH - camHalfW);
        camera.position.y = MathUtils.clamp(camera.position.y, camHalfH, WORLD_HEIGHT - camHalfH);

        camera.update();
        viewport.apply();

        // Frame del personaje
        TextureRegion currentFrame;
        if (keyDown) {
            currentFrame = mirandoDerecha ? QuietoDer : QuietoIzq;
        } else if (isMoving) {
            currentFrame = mirandoDerecha ? CaminarDer.getKeyFrame(stateTime) : CaminarIzq.getKeyFrame(stateTime);
        } else {
            currentFrame = mirandoDerecha ? QuietoDer : QuietoIzq;
        }

        protagonistaSprite.setRegion(currentFrame);
        protagonistaSprite.setPosition(playerX - protagonistaSprite.getWidth() / 2f, playerY);

        // Dibujado
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        oficinaSprite.draw(batch);
        protagonistaSprite.draw(batch);

        if (enZonaTrigger) {
            glyphLayout.setText(fontMensaje, mensajeTrigger);
            float x = playerX - glyphLayout.width / 2f;
            float y = playerY + protagonistaSprite.getHeight() + 80f;
            fontMensaje.draw(batch, glyphLayout, x, y);
        }
        batch.end();

        // UI (botones)
        stageUI.getViewport().apply();
        stageUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stageUI.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        oficinaTexture.dispose();
        texturaQuieto.dispose();
        texturaCaminar.dispose();
        fontMensaje.dispose();
        stageUI.dispose();
        // Opcional: dispose de texturas de botones si no se usan en otras pantallas
    }
}
