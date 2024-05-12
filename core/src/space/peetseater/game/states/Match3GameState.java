package space.peetseater.game.states;

import space.peetseater.game.DragEventSubscriber;
import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.grid.match.MatchEventPublisher;
import space.peetseater.game.grid.match.MatchSubscriber;
import space.peetseater.game.grid.states.BoardAcceptingMoves;
import space.peetseater.game.grid.states.BoardState;
import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Match3GameState implements DragEventSubscriber, MatchEventPublisher<TileType>, MatchSubscriber<TileType> {
    private final BoardManager boardManager;

    private TokenGeneratorAlgorithm<TileType> tokenAlgorithm;
    private final GameGrid<TileType> gameGrid;
    private BoardState boardState;
    Queue<Command> commands;
    private final HashSet<MatchSubscriber<TileType>> subscribers;

    public Match3GameState(BoardManager boardManager, GameGrid<TileType> gameGrid, TokenGeneratorAlgorithm<TileType> tokenAlgorithm) {
        this.boardManager = boardManager;
        this.commands = new LinkedList<>();
        this.gameGrid = gameGrid;
        this.boardState = new BoardAcceptingMoves(this);
        this.boardState.onEnterState(this);
        this.subscribers = new HashSet<>(1);
        this.tokenAlgorithm = tokenAlgorithm;
    }

    public void update(float delta) {
        while (!commands.isEmpty()) {
            Command command = commands.remove();
            command.execute();
        }
        BoardState newState = this.boardState.update(delta);
        if (newState != this.boardState) {
            changeState(newState);
        }
    }

    public void changeState(BoardState boardState) {
        // Should we consider a stack instead?
        this.boardState.onExitState(this);
        this.boardState = boardState;
        this.boardState.onEnterState(this);
    }

    protected boolean pointNotInBoard(float gameX, float gameY) {
        return !this.boardManager.pointInBounds(gameX, gameY);
    }

    @Override
    public void onDragStart(float gameX, float gameY) {
        if (pointNotInBoard(gameX, gameY)) {
            return;
        }
        boardState.onDragStart(gameX, gameY);
    }

    @Override
    public void onDrag(float gameX, float gameY) {
        if (pointNotInBoard(gameX, gameY)) {
            return;
        }
        boardState.onDrag(gameX, gameY);
    }

    @Override
    public void onDragEnd(float gameX, float gameY) {
        boardState.onDragEnd(gameX, gameY);
    }

    @Override
    public void onLeftClick(float gameX, float gameY) {
        boardState.onLeftClick(gameX, gameY);
    }

    @Override
    public void addSubscriber(MatchSubscriber<TileType> matchSubscriber) {
        this.subscribers.add(matchSubscriber);
    }

    @Override
    public void removeSubscriber(MatchSubscriber<TileType> matchSubscriber) {
        this.subscribers.remove(matchSubscriber);
    }

    @Override
    public void onMatches(List<Match<TileType>> matches) {
        for (MatchSubscriber<TileType> matchSubscriber : subscribers) {
            matchSubscriber.onMatches(matches);
        }
    }

    public BoardManager getBoardGraphic() {
        return boardManager;
    }

    public GameGrid<TileType> getGameGrid() {
        return gameGrid;
    }

    public TokenGeneratorAlgorithm<TileType> getTokenAlgorithm() {
        return tokenAlgorithm;
    }

    public void setTokenAlgorithm(TokenGeneratorAlgorithm<TileType> tokenAlgorithm) {
        this.tokenAlgorithm = tokenAlgorithm;
    }
}
