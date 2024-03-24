package space.peetseater.game.tile;

import space.peetseater.game.token.NextTokenLikelyToMatchAlgorithm;
import space.peetseater.game.token.NextTokenWillNotMatchAlgorithm;
import space.peetseater.game.grid.GameGrid;

import java.util.TreeSet;

public class NextTileAlgorithms {

    public static class WillNotMatch extends NextTokenWillNotMatchAlgorithm<TileType> {
        public WillNotMatch(GameGrid<TileType> grid) {
            super(getTreeSet(), grid);
        }
    }

    public static class LikelyToMatch extends NextTokenLikelyToMatchAlgorithm<TileType> {
        public LikelyToMatch(GameGrid<TileType> grid) {
            super(getTreeSet(), grid);
        }
    }

    private static TreeSet<TileType> getTreeSet() {
        TreeSet<TileType> set = new TreeSet<>();
        for (TileType tileType : TileType.values()) {
            set.add(tileType);
        }
        return set;
    }
}

