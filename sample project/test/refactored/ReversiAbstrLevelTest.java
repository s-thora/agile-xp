package refactored;

import javafx.util.Pair;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ReversiAbstrLevelTest {

    private ReversiAbstrLevel rev = new ReversiAbstrLevel();

    private String gameConfigDir = "./game_config/";
    private Path gameAllAlpha = new File(gameConfigDir + "game_all_alpha.txt").toPath();
    private Path gameAllNum = new File(gameConfigDir + "game_all_num.txt").toPath();
    private Path gameAlmostComplete = new File(gameConfigDir + "game_almost_complete.txt").toPath();
    private Path gameComplete = new File(gameConfigDir + "game_complete.txt").toPath();
    private Path gameEmpty = new File(gameConfigDir + "game_empty.txt").toPath();
    private Path gameFourLines = new File(gameConfigDir + "game_four_lines.txt").toPath();
    private Path gameInitBStarts = new File(gameConfigDir + "game_init_b_starts.txt").toPath();
    private Path gameInitWStarts = new File(gameConfigDir + "game_init_w_starts.txt").toPath();
    private Path gameNoOnTurn = new File(gameConfigDir + "game_no_on_turn.txt").toPath();
    private Path gameOneLine = new File(gameConfigDir + "game_one_line.txt").toPath();
    private Path gameNotExisting = new File(gameConfigDir + "game_not_existing.txt").toPath();


    // Player

    @Test
    public void testPlayerValueOf() {
        assertEquals("Value of Player B", Player.B, Player.valueOf("B"));
        assertEquals("Value of Player W", Player.W, Player.valueOf("W"));
        assertEquals("Value of Player NONE", Player.NONE, Player.valueOf("NONE"));
    }


    // Alpha

    @Test
    public void testAlphaValueOf() {
        assertEquals("Value of Alpha A", Alpha.A, Alpha.valueOf("A"));
        assertEquals("Value of Alpha B", Alpha.B, Alpha.valueOf("B"));
        assertEquals("Value of Alpha C", Alpha.C, Alpha.valueOf("C"));
        assertEquals("Value of Alpha D", Alpha.D, Alpha.valueOf("D"));
        assertEquals("Value of Alpha E", Alpha.E, Alpha.valueOf("E"));
        assertEquals("Value of Alpha F", Alpha.F, Alpha.valueOf("F"));
        assertEquals("Value of Alpha G", Alpha.G, Alpha.valueOf("G"));
        assertEquals("Value of Alpha H", Alpha.H, Alpha.valueOf("H"));
    }


    // readGameConfig

    @Test
    public void testReadGameConfigInitB() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameInitBStarts);

        assertEquals("reading initial config file: lines number should be 3", 3, gameConfig.length);
        assertEquals("1st line of initial config file", "B", gameConfig[0]);
        assertEquals("2nd line of initial config file", "E4 D5", gameConfig[1]);
        assertEquals("3rd line of initial config file", "D4 E5", gameConfig[2]);
    }

    @Test
    public void testReadGameConfigInitW() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameInitWStarts);

        assertEquals("reading initial config file: lines number should be 3", 3, gameConfig.length);
        assertEquals("1st line of initial config file", "W", gameConfig[0]);
        assertEquals("2nd line of initial config file", "E4 D5", gameConfig[1]);
        assertEquals("3rd line of initial config file", "D4 E5", gameConfig[2]);
    }

    @Test
    public void testReadGameConfigEmpty() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameEmpty);

        assertEquals("lines number of empty config file", 0, gameConfig.length);
    }

    @Test
    public void testReadGameConfigOneLine() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameOneLine);

        assertEquals("lines number of 1-line config file", 1, gameConfig.length);
        assertEquals("1st line of 1-line config file", "E4 D5", gameConfig[0]);
    }

    @Test
    public void testReadGameConfigFourLines() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameFourLines);

        assertEquals(4, gameConfig.length);
        assertEquals("B", gameConfig[0]);
        assertEquals("E4 D5", gameConfig[1]);
        assertEquals("D4 E5", gameConfig[2]);
        assertEquals("E4 D5", gameConfig[3]);
    }

    @Test
    public void testReadGameConfigNotExisting() {
        ReversiAbstrLevel game = rev;
        String[] gameConfig = game.readGameConfig(gameNotExisting);

        String[] expectedGameConfig = new String[]{};
        assertArrayEquals(expectedGameConfig, gameConfig);
    }


    // isOnTurnInputCorrect

//    @Test
//    public void testIsOnTurnInputCorrectB() {
//        ReversiAbstrLevel game = rev;
//
//        assertTrue("on turn value of config file: B", game.isOnTurnInputCorrect("B"));
//    }
//
//    @Test
//    public void testIsOnTurnInputCorrectW() {
//        ReversiAbstrLevel game = rev;
//
//        assertTrue("on turn value of config file: W", game.isOnTurnInputCorrect("W"));
//    }
//
//    @Test
//    public void testIsOnTurnInputCorrectA() {
//        ReversiAbstrLevel game = rev;
//
//        assertFalse("on turn value of config file: A", game.isOnTurnInputCorrect("A"));
//    }
//
//    @Test
//    public void testIsOnTurnInputCorrectNONE() {
//        ReversiAbstrLevel game = rev;
//
//        assertFalse("on turn value of config file: A", game.isOnTurnInputCorrect("NONE"));
//    }
//
//    @Test
//    public void testIsOnTurnInputCorrectnull() {
//        ReversiAbstrLevel game = rev;
//
//        assertFalse("on turn value of config file: A", game.isOnTurnInputCorrect(null));
//    }


    // setOnTurn

    @Test
    public void testSetOnTurnB() {
        ReversiAbstrLevel game = rev;
        game.setOnTurn("B");

        assertEquals("set player on turn: B", Player.B, game.onTurn);
    }

    @Test
    public void testSetOnTurnW() {
        ReversiAbstrLevel game = rev;
        game.setOnTurn("W");

        assertEquals("set player on turn: W", Player.W, game.onTurn);
    }

    @Test
    public void testSetOnTurnA() {
        ReversiAbstrLevel game = rev;
        game.setOnTurn("A");

        assertEquals(Player.NONE, game.onTurn);
    }

    @Test
    public void testSetOnTurnNone() {
        ReversiAbstrLevel game = rev;
        game.setOnTurn("NONE");

        assertEquals(Player.NONE, game.onTurn);
    }

    @Test
    public void testSetOnTurnnull() {
        ReversiAbstrLevel game = rev;
        game.setOnTurn(null);

        assertEquals(Player.NONE, game.onTurn);
    }

    // createPlayground

    @Test
    public void testCreatePlayground() {
        ReversiAbstrLevel game = getRevWithPlayground();

        assertArrayEquals("create empty playground", getEmptyPlayground(), game.playground);
    }


    // setTile

    @Test
    public void testSetTileA1() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("A1", Player.B);

        assertEquals("set player B on tile A1", Player.B, getTile(game, Alpha.A, 1));
    }

    @Test
    public void testSetTileAA() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("AA", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTile11() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("11", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTilea1() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("a1", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTile1A() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("1A", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTileI1() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("I1", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTileA9() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("A9", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }

    @Test
    public void testSetTileI9() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.setTile("I9", Player.B);

        Player[][] expectedPlayground = getInitPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
        assertEquals(Player.B, game.onTurn);
    }


    // fillPlayground

    @Test
    public void testFillPlaygroundInit() {
        String[] gameConfig = new String[] {"B", "E4 D5", "D4 E5"};
        ReversiAbstrLevel game = getRevWithPlayground();
        game.fillPlayground(gameConfig);

        assertEquals("fill playground with initial game config", Player.B, getTile(game, Alpha.E, 4));
        assertEquals("fill playground with initial game config", Player.B, getTile(game, Alpha.D, 5));
        assertEquals("fill playground with initial game config", Player.W, getTile(game, Alpha.D, 4));
        assertEquals("fill playground with initial game config", Player.W, getTile(game, Alpha.E, 5));
    }

    @Test
    public void testFillPlaygroundConfigLen1() {
        String[] gameConfig = new String[] {"one"};
        ReversiAbstrLevel game = getRevWithPlayground();
        game.fillPlayground(gameConfig);

        Player[][] expectedPlayground = getEmptyPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
    }

    @Test
    public void testFillPlaygroundNull() {
        ReversiAbstrLevel game = getRevWithPlayground();
        game.fillPlayground(null);

        Player[][] expectedPlayground = getEmptyPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
    }

    @Test
    public void testFillPlaygroundIncorrectConfig() {
        String[] gameConfig = new String[] {"B", "AA BB", "CC DD"};
        ReversiAbstrLevel game = getRevWithPlayground();
        game.fillPlayground(gameConfig);

        Player[][] expectedPlayground = getEmptyPlayground();
        assertArrayEquals(expectedPlayground, game.playground);
    }


    // initGame

    @Test
    public void testInitGameInit() {
        String[] gameConfig = new String[] {"B", "E4 D5", "D4 E5"};
        ReversiAbstrLevel game = rev;
        game.initGame(gameConfig);

        assertEquals("init playground on initial game config", Player.B, game.onTurn);
        assertEquals("init playground on initial game config", Player.B, getTile(game, Alpha.E, 4));
        assertEquals("init playground on initial game config", Player.B, getTile(game, Alpha.D, 5));
        assertEquals("init playground on initial game config", Player.W, getTile(game, Alpha.D, 4));
        assertEquals("init playground on initial game config", Player.W, getTile(game, Alpha.E, 5));
    }

    @Test
    public void testInitGameNoLines() {
        String[] gameConfig = new String[] {};
        ReversiAbstrLevel game = rev;
        game.initGame(gameConfig);

        assertArrayEquals(null, game.playground);
    }

    @Test
    public void testInitGameOneLine() {
        String[] gameConfig = new String[] {"E4 D5"};
        ReversiAbstrLevel game = rev;
        game.initGame(gameConfig);

        assertArrayEquals(null, game.playground);
    }

    @Test
    public void testInitGameFourLines() {
        String[] gameConfig = new String[] {"B", "E4 D5", "D4 E5", "E4 D5"};
        ReversiAbstrLevel game = rev;
        game.initGame(gameConfig);

        assertArrayEquals(null, game.playground);
    }

    @Test
    public void testInitGameNull() {
        ReversiAbstrLevel game = rev;
        game.initGame(null);

        assertArrayEquals(null, game.playground);
    }

    @Test
    public void testInitGameOnlyB() {
        String[] gameConfig = new String[] {"B", "E4 D5"};
        ReversiAbstrLevel game = rev;
        game.initGame(gameConfig);

        assertArrayEquals(null, game.playground);
    }


    // initTilesCount

    @Test
    public void testInitTilesCountInit() {
        String[] gameConfig = new String[] {"B", "E4 D5", "D4 E5"};
        ReversiAbstrLevel game = initReversi(gameConfig);
        game.initTilesCount();

        assertEquals("init tiles count on initial game config", 2, game.getLeftB());
        assertEquals("init tiles count on initial game config", 2, game.getLeftW());
    }

    @Test
    public void testInitTilesCountEmpty() {
        ReversiAbstrLevel game = getRevWithPlayground();

        assertEquals("init tiles count on empty game config", 0, game.getLeftB());
        assertEquals("init tiles count on empty game config", 0, game.getLeftW());
    }


    // getLeftB

    @Test
    public void testGetLeftB() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);

        assertEquals("left Bs on initial game config", 2, game.getLeftB());
    }

    // getLeftW

    @Test
    public void testGetLeftW() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);

        assertEquals("left Ws on initial game config", 2, game.getLeftW());
    }


    // ReversiAbstrLevel

    @Test
    public void testInit() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);

        assertEquals("on turn player on initial game config", Player.B, game.onTurn);
        assertEquals("playground on initial game config", Player.W, getTile(game, Alpha.D, 4));
        assertEquals("playground on initial game config", Player.B, getTile(game, Alpha.E, 4));
        assertEquals("playground on initial game config", Player.B, getTile(game, Alpha.D, 5));
        assertEquals("playground on initial game config", Player.W, getTile(game, Alpha.E, 5));
        assertEquals("left Bs on initial game config", 2, game.getLeftB());
        assertEquals("left Ws on initial game config", 2, game.getLeftW());
    }

    @Test
    public void testEmpty() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameEmpty);

        assertArrayEquals(null, game.playground);
        assertEquals(Player.NONE, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }

    @Test
    public void testOneLine() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameOneLine);

        assertArrayEquals(null, game.playground);
        assertEquals(Player.NONE, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }

    @Test
    public void testFourLines() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameFourLines);

        assertArrayEquals(null, game.playground);
        assertEquals(Player.NONE, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }

    @Test
    public void testAllNum() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameAllNum);

        assertArrayEquals(getEmptyPlayground(), game.playground);
        assertEquals(Player.B, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }

    @Test
    public void testAllAlpha() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameAllAlpha);

        assertArrayEquals(getEmptyPlayground(), game.playground);
        assertEquals(Player.B, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }

    @Test
    public void testNoOnTurn() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameNoOnTurn);

        assertArrayEquals(null, game.playground);
        assertEquals(Player.NONE, game.onTurn);
        assertFalse(game.ended);
        assertEquals(Player.NONE, game.winner);
    }


    // isEmpty

//    @Test
//    public void testIsEmptyInit00() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
//
//        assertTrue("is empty (0, 0) on init", game.isEmpty(0, 0));
//    }
//
//    @Test
//    public void testIsEmptyInit33() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
//
//        assertFalse("is empty (3, 3) on init", game.isEmpty(3, 3));
//    }


    // isGameOver

//    @Test
//    public void testIsGameOverInit() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
//
//        assertFalse("is game over on init", game.isGameOver());
//    }
//
//    @Test
//    public void testIsGameOverOnEnd() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameComplete);
//        assertFalse("is game over on init", game.isGameOver());
//    }


    // getTilesToFlip

    @Test
    public void testGetTilesToFlipInit32() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        ArrayList<List<Integer>> tiles = game.getTilesToFlip(3, 2);
        ArrayList<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(3, 3));
        expected.add(Arrays.asList(3, 2));

        assertEquals("tiles to flip on onit - (3, 2)", 2, tiles.size());
        assertEquals("...", expected.get(0).get(0), tiles.get(0).get(0));
        assertEquals("...", expected.get(0).get(1), tiles.get(0).get(1));
        assertEquals("...", expected.get(1).get(0), tiles.get(1).get(0));
        assertEquals("...", expected.get(1).get(1), tiles.get(1).get(1));
    }

    @Test
    public void testGetTilesToFlipInit00() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        ArrayList<List<Integer>> tiles = game.getTilesToFlip(0, 0);

        assertEquals("tiles to flip on onit - (0, 0)", 0, tiles.size());
    }


    // flipTiles

    @Test
    public void testFlipTiles() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        ArrayList<List<Integer>> tiles = new ArrayList<>();
        tiles.add(Arrays.asList(3, 3));
        tiles.add(Arrays.asList(3, 2));
        game.flipTiles(tiles);

        assertEquals("...", Player.B, getTile(game, Alpha.C, 4));
        assertEquals("...", Player.B, getTile(game, Alpha.D, 4));
    }

    // getPossibleMoves

    @Test
    public void testGetPossibleMovesEmptyInit() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        ArrayList<String> tiles = game.getPossibleMoves();

        assertEquals("valid length", 4, tiles.size());
        assertEquals("valid moves", "D3", tiles.get(0));
        assertEquals("valid moves", "C4", tiles.get(1));
        assertEquals("valid moves", "F5", tiles.get(2));
        assertEquals("valid moves", "E6", tiles.get(3));
    }

    @Test
    public void testGetPossibleMovesEmpty() {
        ReversiAbstrLevel game = getRevWithPlayground();
        ArrayList<String> tiles = game.getPossibleMoves();

        assertEquals("valid length", 0, tiles.size());
    }


    // areValidMoves

    @Test
    public void testAreValidMovesInit() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);

        assertTrue("...", game.areValidMoves());
    }

    @Test
    public void testAreValidMovesOnEnd() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameComplete);

        assertFalse("...", game.areValidMoves());
    }


    // swapPlayerOnTurn

//    @Test
//    public void testSwapPlayerOnTurnBtoW() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
//        game.swapPlayerOnTurn();
//
//        assertEquals("...", Player.W, game.onTurn);
//    }
//
//    @Test
//    public void testSwapPlayerOnTurnWtoB() {
//        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitWStarts);
//        game.swapPlayerOnTurn();
//
//        assertEquals("...", Player.B, game.onTurn);
//    }

    // endGame

    @Test
    public void testEndGame() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameComplete);
        game.endGame();

        assertTrue("...", game.ended);
        assertEquals("...", Player.B, game.winner);
    }


    // move

    @Test
    public void testMoveOnNotEmpty() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.E,5);

        assertArrayEquals("check if didn't change", getInitPlayground(), game.playground);
    }

    @Test
    public void testMoveOutOfBoundsBelow() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.A,9);

        assertArrayEquals("check if didn't change", getInitPlayground(), game.playground);
    }

    @Test
    public void testMoveOutOfBoundsAbove() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.A,0);

        assertArrayEquals("check if didn't change", getInitPlayground(), game.playground);
    }

    @Test
    public void testMoveOnNotAdjacent() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.A,1);

        assertArrayEquals("check if didn't change", getInitPlayground(), game.playground);
    }

    @Test
    public void testMoveFlipRight() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.C,4);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.D, 4));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.C, 4));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 1, game.getLeftW());
        assertEquals("B left", 4, game.getLeftB());
    }

    @Test
    public void testMoveFlipUp() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.E, 6);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.E, 5));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.E, 6));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 1, game.getLeftW());
        assertEquals("B left", 4, game.getLeftB());
    }

    @Test
    public void testMoveFlipLeft() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.F, 5);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.E, 5));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.F, 5));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 1, game.getLeftW());
        assertEquals("B left", 4, game.getLeftB());
    }

    @Test
    public void testMoveFlipDown() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.move(Alpha.D, 3);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.D, 4));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.D, 3));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 1, game.getLeftW());
        assertEquals("B left", 4, game.getLeftB());
    }

    @Test
    public void testMoveFlipRightUp() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.E, 6));
        moves.add(new Pair<>(Alpha.D, 6));
        moves.add(new Pair<>(Alpha.C, 7));
        ReversiAbstrLevel game = setMoves(moves);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.D, 6));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.C, 7));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 2, game.getLeftW());
        assertEquals("B left", 5, game.getLeftB());
    }

    @Test
    public void testMoveFlipLeftUp() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.E, 6));
        moves.add(new Pair<>(Alpha.F, 6));
        ReversiAbstrLevel game = setMoves(moves);

        assertEquals("check if flipped", Player.W, getTile(game, Alpha.E, 5));
        assertEquals("check if flipped", Player.W, getTile(game, Alpha.F, 6));
        assertEquals("on turn", Player.B, game.onTurn);
        assertEquals("W left", 3, game.getLeftW());
        assertEquals("B left", 3, game.getLeftB());
    }

    @Test
    public void testMoveFlipLeftDown() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.D, 3));
        moves.add(new Pair<>(Alpha.E, 3));
        moves.add(new Pair<>(Alpha.F, 2));
        ReversiAbstrLevel game = setMoves(moves);

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.E, 3));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.F, 2));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 2, game.getLeftW());
        assertEquals("B left", 5, game.getLeftB());
    }

    @Test
    public void testMoveFlipRightDown() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.D, 3));
        moves.add(new Pair<>(Alpha.C, 3));
        ReversiAbstrLevel game = setMoves(moves);

        assertEquals("check if flipped", Player.W, getTile(game, Alpha.D, 4));
        assertEquals("check if flipped", Player.W, getTile(game, Alpha.C, 3));
        assertEquals("on turn", Player.B, game.onTurn);
        assertEquals("W left", 3, game.getLeftW());
        assertEquals("B left", 3, game.getLeftB());
    }

    @Test
    public void testMoveDoubleFlip() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.D, 3));
        moves.add(new Pair<>(Alpha.C, 3));
        moves.add(new Pair<>(Alpha.C, 4));
        moves.add(new Pair<>(Alpha.E, 3));
        ReversiAbstrLevel game = setMoves(moves);

        assertEquals("check if flipped (D,3) correctly", Player.W, getTile(game, Alpha.D, 3));
        assertEquals("check if flipped (E,4) correctly", Player.W, getTile(game, Alpha.E, 4));
        assertEquals("W left", 5, game.getLeftW());
        assertEquals("B left", 3, game.getLeftB());
    }

    @Test
    public void testMovesCompleteGame() {
        ArrayList<Pair<Alpha, Integer>> moves = new ArrayList<>();
        moves.add(new Pair<>(Alpha.E, 6)); moves.add(new Pair<>(Alpha.F, 4));
        moves.add(new Pair<>(Alpha.D, 3)); moves.add(new Pair<>(Alpha.C, 4));
        moves.add(new Pair<>(Alpha.C, 3)); moves.add(new Pair<>(Alpha.D, 6));
        moves.add(new Pair<>(Alpha.E, 3)); moves.add(new Pair<>(Alpha.C, 2));
        moves.add(new Pair<>(Alpha.B, 3)); moves.add(new Pair<>(Alpha.F, 5));
        moves.add(new Pair<>(Alpha.F, 3)); moves.add(new Pair<>(Alpha.D, 2));
        moves.add(new Pair<>(Alpha.E, 2)); moves.add(new Pair<>(Alpha.B, 4));
        moves.add(new Pair<>(Alpha.C, 5)); moves.add(new Pair<>(Alpha.F, 1));
        moves.add(new Pair<>(Alpha.A, 3)); moves.add(new Pair<>(Alpha.F, 2));
        moves.add(new Pair<>(Alpha.C, 6)); moves.add(new Pair<>(Alpha.F, 6));
        moves.add(new Pair<>(Alpha.G, 6)); moves.add(new Pair<>(Alpha.A, 5));
        moves.add(new Pair<>(Alpha.B, 5)); moves.add(new Pair<>(Alpha.A, 6));
        moves.add(new Pair<>(Alpha.G, 5)); moves.add(new Pair<>(Alpha.C, 7));
        moves.add(new Pair<>(Alpha.G, 3)); moves.add(new Pair<>(Alpha.D, 7));
        moves.add(new Pair<>(Alpha.E, 7)); moves.add(new Pair<>(Alpha.H, 4));
        moves.add(new Pair<>(Alpha.D, 8)); moves.add(new Pair<>(Alpha.G, 4));
        moves.add(new Pair<>(Alpha.A, 4)); moves.add(new Pair<>(Alpha.A, 2));
        moves.add(new Pair<>(Alpha.H, 2)); moves.add(new Pair<>(Alpha.H, 3));
        moves.add(new Pair<>(Alpha.H, 5)); moves.add(new Pair<>(Alpha.B, 6));
        moves.add(new Pair<>(Alpha.C, 8)); moves.add(new Pair<>(Alpha.F, 7));
        moves.add(new Pair<>(Alpha.E, 8)); moves.add(new Pair<>(Alpha.B, 7));
        moves.add(new Pair<>(Alpha.C, 1)); moves.add(new Pair<>(Alpha.H, 6));
        moves.add(new Pair<>(Alpha.H, 7)); moves.add(new Pair<>(Alpha.D, 1));
        moves.add(new Pair<>(Alpha.A, 8)); moves.add(new Pair<>(Alpha.B, 1));
        moves.add(new Pair<>(Alpha.A, 7)); moves.add(new Pair<>(Alpha.F, 8));
        moves.add(new Pair<>(Alpha.G, 2)); moves.add(new Pair<>(Alpha.H, 1));
        moves.add(new Pair<>(Alpha.G, 1)); moves.add(new Pair<>(Alpha.H, 8));
        moves.add(new Pair<>(Alpha.E, 1)); moves.add(new Pair<>(Alpha.B, 8));
        moves.add(new Pair<>(Alpha.A, 1)); moves.add(new Pair<>(Alpha.B, 2));
        moves.add(new Pair<>(Alpha.G, 8)); moves.add(new Pair<>(Alpha.G, 7));
        ReversiAbstrLevel game = setMoves(moves);

        assertFalse("if the are valid moves", game.areValidMoves());
        assertEquals("W left", 28, game.getLeftW());
        assertEquals("B left", 36, game.getLeftB());
        assertEquals("winner", Player.B, game.winner);
    }


    // execute

    @Test
    public void testExecute() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.execute("C4");

        assertEquals("check if flipped", Player.B, getTile(game, Alpha.D, 4));
        assertEquals("check if flipped", Player.B, getTile(game, Alpha.C, 4));
        assertEquals("on turn", Player.W, game.onTurn);
        assertEquals("W left", 1, game.getLeftW());
        assertEquals("B left", 4, game.getLeftB());
    }

    @Test
    public void testExecuteA1() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        game.execute("A1");

        assertArrayEquals("check if didn't change", getInitPlayground(), game.playground);
    }

    @Test
    public void testFinishGame() {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameAlmostComplete);
        game.execute("G7");

        assertFalse("if the are valid moves", game.areValidMoves());
        assertEquals("W left", 28, game.getLeftW());
        assertEquals("B left", 36, game.getLeftB());
        assertEquals("winner", Player.B, game.winner);
    }


    // utility functions

    private Player getTile(ReversiAbstrLevel game, Alpha c0, int r0) {
        return game.playground[r0-1][c0.getValue()];
    }

    private ReversiAbstrLevel setMoves(ArrayList<Pair<Alpha, Integer>> moves) {
        ReversiAbstrLevel game = new ReversiAbstrLevel(gameInitBStarts);
        for (Pair<Alpha, Integer> move  : moves) {
            Alpha r = move.getKey();
            Integer c = move.getValue();
            game.move(r, c);
        }
        return game;
    }

    private ReversiAbstrLevel initReversi(String[] gameConfig) {
        ReversiAbstrLevel rev = new ReversiAbstrLevel();
        rev.initGame(gameConfig);
        return rev;
    }

    private ReversiAbstrLevel getRevWithPlayground() {
        ReversiAbstrLevel rev = new ReversiAbstrLevel();
        rev.createPlayground();
        return rev;
    }

    private Player[][] getEmptyPlayground() {
        Player[][] empty = new Player[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                empty[r][c] = Player.NONE;
            }
        }
        return empty;
    }

    private Player[][] getInitPlayground() {
        Player[][] init = new Player[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                init[r][c] = Player.NONE;
            }
        }
        init[3][3] = Player.W;
        init[4][4] = Player.W;
        init[3][4] = Player.B;
        init[4][3] = Player.B;
        return init;
    }
}
