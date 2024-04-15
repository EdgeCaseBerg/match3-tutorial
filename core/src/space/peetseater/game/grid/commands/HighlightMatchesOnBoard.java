package space.peetseater.game.grid.commands;

import com.badlogic.gdx.Gdx;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.match.CrossSectionTileMatcher;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.commands.IncludeInMatch;

import java.util.List;

public class HighlightMatchesOnBoard implements Command {

    private final CrossSectionTileMatcher<TileGraphic> matcher;

    public HighlightMatchesOnBoard(GameGrid<TileGraphic> gameGrid, int row, int column) {
        this.matcher = new CrossSectionTileMatcher<>(row, column, gameGrid);
    }

    @Override
    public void execute() {
        List<Match<TileGraphic>> matches = matcher.findMatches();
        for (Match<TileGraphic> match : matches) {
            for (TileGraphic t:  match.getValues()) {
                (new IncludeInMatch(t)).execute();
            }
        }
    }
}
