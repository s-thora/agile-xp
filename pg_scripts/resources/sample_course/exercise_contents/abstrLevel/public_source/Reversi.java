import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reversi {

    int size;
    Player[][] playground;
    private int leftB = 0;
    private int leftW = 0;
    private Player[] players = new Player[] { Player.B, Player.W };
    Player onTurn = Player.NONE;
    Player winner = Player.NONE;
    boolean ended = false;

    Reversi() {
    }

    Reversi(Path gameFilePath) {
        try {
            String[] gameConfig = readGameConfig(gameFilePath);
            checkLength(gameConfig);
            initGame(gameConfig);
            initPiecesCount();
        } catch (Exception e) {
            ended = true;
            System.out.println(e.getMessage());
        }
    }

    String[] readGameConfig(Path gameFilePath) {
        String[] gameConfig = new String[] {};
        try {
            gameConfig = Files.readAllLines(gameFilePath).toArray(new String[0]);
        } catch (NoSuchFileException e) {
            System.out.println("Game configuration file does not exist");
        } catch (IOException e) {
            System.out.println("Could not read game configuration file");
        }
        return gameConfig;
    }

    void checkLength(String[] gameConfig) throws Exception {
        int configFileLinesNumber = 4;
        if (gameConfig.length != configFileLinesNumber) {
            throw new Exception("Game configuration must contain " + configFileLinesNumber + " lines");
        }
    }

    void initGame(String[] gameConfig) {
        setSize(gameConfig[0]);
        setOnTurn(gameConfig[1]);
        createPlayground();
        fillPlayground(gameConfig);
    }

    void setSize(String size) {
        if (!size.matches("[0-9]+")) {
            System.out.println("Incorrect size input");
            return;
        }
        this.size = Integer.parseInt(size);
    }

    void setOnTurn(String onTurn) {
        if (onTurn == null || !onTurn.matches("[B|W]")) {
            System.out.println("Incorrect player on turn input");
            return;
        }
        if ("B".equals(onTurn)) {
            this.onTurn = Player.B;
        } else if ("W".equals(onTurn)) {
            this.onTurn = Player.W;
        }
    }

    private void createPlayground() {
        playground = new Player[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                playground[r][c] = Player.NONE;
            }
        }
    }

    void fillPlayground(String[] gameConfig) {
        try {
            int[] piecesPositions = new int[] {2, 3};
            for (int piecePosition : piecesPositions) {
                String[] pieces = gameConfig[piecePosition].split(",");
                for (String piece : pieces) {
                    if (!isPieceInputCorrect(piece)) {
                        System.out.println("Incorrect piece input");
                        return;
                    }
                    int[] coordinates = getCoordinates(piece);
                    setPiece(coordinates, players[piecePosition - 2]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Game configuration file is incorrect");
        }
    }

    boolean isPieceInputCorrect(String piece) {
        return piece.matches("[ ]*[0-9]+[ ]+[0-9]+[ ]*");
    }

    int[] getCoordinates(String piece) {
        String[] coordinates = piece.trim().split(" ");
        int r = Integer.parseInt(coordinates[0]);
        int c = Integer.parseInt(coordinates[1]);
        return new int[] {r, c};
    }

    void setPiece(int[] coordinates, Player player) {
        int r = coordinates[0];
        int c = coordinates[1];
        if (r >= size || c >= size) {
            return;
        }
        playground[r][c] = player;
    }

    void initPiecesCount() {
        try {
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (playground[r][c] == Player.B) {
                        leftB++;
                    } else if (playground[r][c] == Player.W) {
                        leftW++;
                    }
                }
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Playground  is not valid" + e.getMessage());
        }
    }

    private void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line;
            while (!ended) {
                PlaygroundPrinter.printHints(playground, size, getPossibleMoves());
                PlaygroundPrinter.printPlayground(playground, size);
                PlaygroundPrinter.printMoveOnTurn(onTurn);
                if (winner != Player.NONE) break;
                if ((line = reader.readLine()) == null) break;
                execute(line);
                printPiecesLeftCount();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("IO exception occurred on reading user input: " + e.getMessage());
        }
    }

    void execute(String line) {
        if (!isPieceInputCorrect(line)) {
            System.out.println("Incorrect piece input");
            return;
        }
        int[] coordinates = getCoordinates(line);
        move(coordinates[0], coordinates[1]);
        if (! areValidMoves()) {
            endGame();
        }
    }

    private void printPiecesLeftCount() {
        PlaygroundPrinter.printPiecesNumber(getLeftB(), getLeftW());
    }

    int getLeftB() {
        return leftB;
    }

    int getLeftW() {
        return leftW;
    }

    void move(int r, int c) {
        if (!isWithinPlayground(r, c)) {
            System.out.println("Move out of bounds is not permitted");
            return;
        }
        if (playground[r][c] != Player.NONE) {
            System.out.println("Move on not empty piece is not permitted");
            return;
        }
        if (winner != Player.NONE) {
            System.out.println("The game is over. No moves are permitted");
            return;
        }

        List<List<Integer>> piecesToFlip = getPiecesToFlip(r, c);
        if (piecesToFlip.isEmpty()) {
            System.out.println("Move is not permitted");
            return;
        }
        flipPieces(piecesToFlip);

        if (onTurn == Player.W) onTurn = Player.B;
        else if (onTurn == Player.B) onTurn = Player.W;
    }

    boolean isWithinPlayground(int r, int c) {
        return r >= 0 && c >= 0 && r < size && c < size;
    }

    List<List<Integer>> getPiecesToFlip(int r0, int c0) {
        List<List<Integer>> toFlip = new ArrayList<>();
        playground[r0][c0] = onTurn;
        Player opposite = Player.NONE;
        if (onTurn == Player.W) opposite = Player.B;
        else if (onTurn == Player.B) opposite = Player.W;

        int[][] directions = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};
        for (int[] direction : directions) {
            int r = r0;
            int c = c0;
            r += direction[0];
            c += direction[1];
            if (isWithinPlayground(r, c) && playground[r][c] != opposite) continue;
            r += direction[0];
            c += direction[1];
            if (!isWithinPlayground(r, c)) continue;
            while (playground[r][c] == opposite) {
                r += direction[0];
                c += direction[1];
                if (!isWithinPlayground(r, c)) break;
            }
            if (!isWithinPlayground(r, c)) continue;
            if (playground[r][c] != onTurn) continue;
            while (true) {
                r -= direction[0];
                c -= direction[1];
                if (r == r0 && c == c0) break;
                toFlip.add(new ArrayList<>(Arrays.asList(r, c)));
            }
        }

        playground[r0][c0] = Player.NONE;
        if (!toFlip.isEmpty()) {
            toFlip.add(new ArrayList<>(Arrays.asList(r0, c0)));
        }
        return toFlip;
    }

    void flipPieces(List<List<Integer>> pieces) {
        for (List<Integer> piece : pieces) {
            int r = piece.get(0);
            int c = piece.get(1);
            if (playground[r][c] == onTurn) break;
            if (playground[r][c] == Player.NONE) {
                playground[r][c] = onTurn;
                if (onTurn == Player.B) leftB++;
                else if (onTurn == Player.W) leftW++;
            } else {
                playground[r][c] = onTurn;
                if (onTurn == Player.B) {
                    leftB++;
                    leftW--;
                } else {
                    leftW++;
                    leftB--;
                }
            }
        }
    }

    boolean areValidMoves() {
        return !getPossibleMoves().isEmpty();
    }

    List<String> getPossibleMoves() {
        List<String> pieces = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (playground[r][c] != Player.NONE) continue;
                if (getPiecesToFlip(r, c).isEmpty()) continue;
                pieces.add(String.format("%s %s", r,  c));
            }
        }
        return pieces;
    }

    void endGame() {
        printPiecesLeftCount();
        ended = true;
        if (getLeftB() > getLeftW()) winner = Player.B;
        else if (getLeftW() > getLeftB()) winner = Player.W;
    }

    public static void main(String[] args) {
        Path gameFilePath = GameConfig.game8bInit;
        Reversi rev = new Reversi(gameFilePath);
        rev.run();

    }

}
