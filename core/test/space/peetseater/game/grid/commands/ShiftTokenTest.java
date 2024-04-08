package space.peetseater.game.grid.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.InvalidShiftingException;
import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

class ShiftTokenTest {

    GameGrid<TileType> grid;

    TokenGeneratorAlgorithm<TileType> fixedTokenAlgo = new TokenGeneratorAlgorithm<TileType>() {
        final TileType[] tokens = {
            TileType.LowValue, TileType.Multiplier, TileType.HighValue, TileType.MidValue,
            TileType.HighValue, TileType.Negative, TileType.MidValue, TileType.HighValue,
            TileType.LowValue, TileType.Multiplier, TileType.HighValue, TileType.MidValue,
            TileType.HighValue, TileType.Negative, TileType.MidValue, TileType.HighValue
        };
        int i = 0;
        @Override
        public TileType next(int row, int column) {
            TileType tileType = tokens[i];
            i = (i + 1) % (tokens.length);
            return tileType;
        }
    };

    @BeforeEach
    void setUp() {
        grid = new GameGrid<>(4, 4);
        for (GridSpace<TileType> space : grid) {
            space.setValue(fixedTokenAlgo.next(0, 0));
        }
    }

    @Test
    void movingLeftSwapsATokenWithItsLeftNeighbor() {
        Command command = new ShiftToken(0,1, ShiftToken.Direction.LEFT, grid);
        TileType willBeDisplaced = grid.getTile(0,0 ).getValue();
        TileType willReplaceDisplaced = grid.getTile(0, 1).getValue();
        command.execute();
        Assertions.assertEquals(willBeDisplaced, grid.getTile(0, 1).getValue());
        Assertions.assertEquals(willReplaceDisplaced, grid.getTile(0, 0).getValue());
    }

    @Test
    void movingUpSwapsATokenWithItsNeighborAbove() {
        Command command = new ShiftToken(0,0, ShiftToken.Direction.UP, grid);
        TileType willBeDisplaced = grid.getTile(0,0 ).getValue();
        TileType willReplaceDisplaced = grid.getTile(1, 0).getValue();
        command.execute();
        Assertions.assertEquals(willBeDisplaced, grid.getTile(1, 0).getValue());
        Assertions.assertEquals(willReplaceDisplaced, grid.getTile(0, 0).getValue());
    }

    @Test
    void movingRightSwapsATokenWithItsRightNeighbor() {
        Command command = new ShiftToken(0,0, ShiftToken.Direction.RIGHT, grid);
        TileType willBeDisplaced = grid.getTile(0,1 ).getValue();
        TileType willReplaceDisplaced = grid.getTile(0, 0).getValue();
        command.execute();
        Assertions.assertEquals(willBeDisplaced, grid.getTile(0, 0).getValue());
        Assertions.assertEquals(willReplaceDisplaced, grid.getTile(0, 1).getValue());
    }

    @Test
    void movingDownSwapsATokenWithItsNeighborBelow() {
        Command command = new ShiftToken(1,0, ShiftToken.Direction.DOWN, grid);
        TileType willBeDisplaced = grid.getTile(1,0 ).getValue();
        TileType willReplaceDisplaced = grid.getTile(0, 0).getValue();
        command.execute();
        Assertions.assertEquals(willBeDisplaced, grid.getTile(0, 0).getValue());
        Assertions.assertEquals(willReplaceDisplaced, grid.getTile(1, 0).getValue());
    }

    @Test
    void throwsExceptionIfMoveIsOffTheGrid() {
        Assertions.assertThrows(InvalidShiftingException.class, new Executable() {
            @Override
            public void execute() {
                ShiftToken move = new ShiftToken(
                        grid.getHeight() - 1,
                        grid.getWidth() - 1,
                        ShiftToken.Direction.RIGHT,
                        grid
                );
                move.execute();
            }
        });
    }
}