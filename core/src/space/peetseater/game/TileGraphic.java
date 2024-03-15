package space.peetseater.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TileGraphic {
    public static int TILESIZE = 70;
    private final TileType tileType;
    private Vector2 position;
    Texture texture;

    public TileGraphic(Vector2 position, TileType tileType) {
        this.position = position;
        this.tileType = tileType;
        this.texture = makeTexture();
    }

    private Texture makeTexture() {
        Pixmap pixmap = new Pixmap(TILESIZE, TILESIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(TileType.colorFor(tileType));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void render(float delta, SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
