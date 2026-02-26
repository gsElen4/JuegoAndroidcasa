package com.elena.mijuegocasa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Personajes extends Game {

    Sprite protagonistaSprite;
    @Override
    public void create() {
        Texture protagonista = new Texture("protagonista/pquietofrente.png");
        protagonistaSprite = new Sprite(protagonista);
    }
}
