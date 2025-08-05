import java.awt.*;

public class Constants {

    public static final int TILE_SIZE = 40;
    public static final Color COLOR_GREEN = new Color(229, 229, 229); // default tile
    public static final Color COLOR_BROWN = new Color(200, 200, 200); // revealed tile
    public static final Color COLOR_FLAG = new Color(255, 102, 102);  // flag
    public static final Color COLOR_MINE = new Color(66, 66, 66);     // mine tile

    public static final Color[] NUMBER_COLORS = {
            null,                                // 0
            new Color(25, 118, 210),             // 1 - Blue
            new Color(56, 142, 60),              // 2 - Green
            new Color(211, 47, 47),              // 3 - Red
            new Color(123, 31, 162),             // 4 - Purple
            new Color(255, 143, 0),              // 5 - Orange
            new Color(0, 151, 167),              // 6 - Cyan
            new Color(85, 85, 85),               // 7 - Gray
            new Color(0, 0, 0)                   // 8 - Black
    };

}
