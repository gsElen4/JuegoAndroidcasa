package com.elena.mijuegocasa;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParallaxVista {
    private final Texture texture;
    private float x;
    private final float speed;

    private final float width;
    private final float height;

    public ParallaxVista(String path, float speed, float worldHeight) {
        this.texture = new Texture(path);
        this.speed = speed;
        this.x = 0;

        // Escalado para ocupar toda la altura del monitor
        float scale = worldHeight / texture.getHeight();
        this.width = texture.getWidth() * scale;
        this.height = worldHeight;
    }

    public void update(float delta) {
        x -= speed * delta;
        if (x <= -width) {
            x = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, 0, width, height);
        batch.draw(texture, x + width, 0, width, height);
    }

    public void dispose() {
        texture.dispose();
    }
}
