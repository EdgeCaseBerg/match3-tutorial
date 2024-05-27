package space.peetseater.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;

import java.util.List;

public interface Scene extends Screen {
    List<AssetDescriptor<?>> getRequiredAssets();
}
