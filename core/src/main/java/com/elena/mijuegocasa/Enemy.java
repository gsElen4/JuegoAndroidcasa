package com.elena.mijuegocasa;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy {

    private final Sprite sprite;
    private final Animation<TextureRegion> walkAnimation;
    private final TextureRegion idleFrame;

    private float stateTime = 0f;

    private final float speed;
    private boolean movingRight = true;
    private final float patrolCenterX;
    private final float patrolRange;

    private boolean isMoving = true;  // patrulla continuamente por ahora

    private static final float FRAME_DURATION = 0.18f;

    // Eliminamos TARGET_HEIGHT para usar tamaño original

    public Enemy(TextureRegion textureRegion, float startX, float y,
                 float speed, float patrolRange, int frameCols) {

        // textureRegion debe ser el sprite sheet completo (varios frames horizontales)
        int FRAME_ROWS = 1;

        TextureRegion[][] tmp = TextureRegion.split(
            textureRegion.getTexture(),
            textureRegion.getRegionWidth() / frameCols,
            textureRegion.getRegionHeight() / FRAME_ROWS
        );

        TextureRegion[] walkFrames = new TextureRegion[frameCols];
        for (int j = 0; j < frameCols; j++) {
            walkFrames[j] = tmp[0][j];
        }

        this.walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        this.walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // Frame quieto = normalmente el primero
        this.idleFrame = walkFrames[0];

        // Sprite inicial con tamaño ORIGINAL del frame (pixel-perfect)
        this.sprite = new Sprite(idleFrame);

        // NO escalamos ni forzamos tamaño → se mantiene el tamaño nativo del PNG
        // sprite.setSize(...);  ← ELIMINADO para respetar tamaño original

        // Centramos el origen (útil para flip y animación)
        sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);

        // Posición inicial centrada en X
        sprite.setPosition(startX - sprite.getWidth() / 2f, y);

        this.patrolCenterX = startX;
        this.speed = speed;
        this.patrolRange = patrolRange;
    }

    public void update(float delta, float playerX) {
        stateTime += delta;

        float centerX = sprite.getX() + sprite.getWidth() / 2f;

        // Patrulla simple
        if (movingRight) {
            centerX += speed * delta;
            if (centerX > patrolCenterX + patrolRange / 2f) {
                movingRight = false;
            }
        } else {
            centerX -= speed * delta;
            if (centerX < patrolCenterX - patrolRange / 2f) {
                movingRight = true;
            }
        }

        sprite.setX(centerX - sprite.getWidth() / 2f);

        // Frame actual
        TextureRegion currentFrame = isMoving
            ? walkAnimation.getKeyFrame(stateTime)
            : idleFrame;

        // Flip según dirección
        boolean shouldFlip = !movingRight;
        if (currentFrame.isFlipX() != shouldFlip) {
            currentFrame.flip(true, false);
        }

        sprite.setRegion(currentFrame);

        // NO escalamos aquí → mantiene tamaño original
        // Opcional: si quieres forzar misma altura visual en todos los enemigos:
        // float desiredHeight = 200f;
        // sprite.setScale(desiredHeight / sprite.getHeight());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);  // Dibuja con tamaño original del frame actual
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isFacingRight() {
        return movingRight;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }
}
