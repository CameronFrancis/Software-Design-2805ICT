public class GameConfig {
    // Global config variables for gameboard size, used to size mainMenu window and gameboard.
    public static final int CELL_SIZE = 30;    // cell size
    public static int BOARD_WIDTH = 10;  // columns for gameboard
    public static int BOARD_HEIGHT = 20; // rows for game board

    // Other Settings
    public static final int TIMER_DELAY = 500; // Splash delay
    public static final String GAME_TITLE = "Enhanced Tetris"; // Game title
    public static final String AUTHOR = "Cameron Francis";

    // Configurable Settings
    public static int INITIAL_LEVEL = 1;
    public static String PLAYER_TYPE = "Human";
    public static boolean MUSIC_ON = false;
    public static boolean SOUND_EFFECTS_ON = false;
    public static boolean AI_PLAY = false;
    public static boolean EXTEND_MODE = false;

    // Audio File Paths
    public static final String BACKGROUND_MUSIC_PATH = "resources/background.wav";
    public static final String ERASE_LINE_SOUND_PATH = "resources/erase-line.wav";
    public static final String GAME_FINISH_SOUND_PATH = "resources/game-finish.wav";
    public static final String LEVEL_UP_SOUND_PATH = "resources/level-up.wav";
    public static final String MOVE_TURN_SOUND_PATH = "resources/move-turn.wav";
}