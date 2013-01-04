package hongbosb.rollingball.data;

public class EnvironmentData {

    static public final float NORMAL_SCALE = 80.0f;
    static public final float BOARD_SIZE = 100f;

    static public final float WALL_HEIGHT = 40;
    static public final float WALL_WIDTH = 20;
    static public final float WALL_ABS_INNER_X = BOARD_SIZE;
    static public final float WALL_ABS_INNER_Y = BOARD_SIZE;
    static public final float WALL_ABS_OUTER_X = BOARD_SIZE + WALL_WIDTH;
    static public final float WALL_ABS_OUTER_Y = BOARD_SIZE + WALL_WIDTH;

    static public final int VERTEX_COUNT_WALL_TOP = 10;
    static public final int VERTEX_COUNT_WALL_INNER_FACE = 10;
    static public final int VERTEX_COUNT_WALL_OUTER_FACE = 10;
    static public final int VERTEX_COUNT_FLOOR = 4;

    static public final int[] ITEMS_VERTEX_COUNT = {
        VERTEX_COUNT_WALL_TOP,
        VERTEX_COUNT_WALL_INNER_FACE,
        VERTEX_COUNT_WALL_OUTER_FACE,
        VERTEX_COUNT_FLOOR,
    };

    static public final float TRIANGLE_POS_ARRAY[] = {
        //Wall top.
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,
        -WALL_ABS_INNER_X , -WALL_ABS_INNER_Y , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , -WALL_ABS_OUTER_Y , WALL_HEIGHT ,
        WALL_ABS_INNER_X  , -WALL_ABS_INNER_Y , WALL_HEIGHT ,
        WALL_ABS_OUTER_X  , -WALL_ABS_OUTER_Y , WALL_HEIGHT ,
        WALL_ABS_INNER_X  , WALL_ABS_INNER_Y  , WALL_HEIGHT ,
        WALL_ABS_OUTER_X  , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,

        //Wall inner face.
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , 0           ,
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , WALL_HEIGHT ,
        -WALL_ABS_INNER_X , -WALL_ABS_INNER_Y , 0           ,
        -WALL_ABS_INNER_X , -WALL_ABS_INNER_Y , WALL_HEIGHT ,
        WALL_ABS_INNER_X  , -WALL_ABS_INNER_Y , 0           ,
        WALL_ABS_INNER_X  , -WALL_ABS_INNER_Y , WALL_HEIGHT ,
        WALL_ABS_INNER_X  , WALL_ABS_INNER_Y  , 0           ,
        WALL_ABS_INNER_X  , WALL_ABS_INNER_Y  , WALL_HEIGHT ,
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , 0           ,
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , WALL_HEIGHT ,

        //Wall outer face.
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , 0           ,
        -WALL_ABS_OUTER_X , -WALL_ABS_OUTER_Y , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , -WALL_ABS_OUTER_Y , 0           ,
        WALL_ABS_OUTER_X  , -WALL_ABS_OUTER_Y , WALL_HEIGHT ,
        WALL_ABS_OUTER_X  , -WALL_ABS_OUTER_Y , 0           ,
        WALL_ABS_OUTER_X  , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,
        WALL_ABS_OUTER_X  , WALL_ABS_OUTER_Y  , 0           ,
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , WALL_HEIGHT ,
        -WALL_ABS_OUTER_X , WALL_ABS_OUTER_Y  , 0           ,

        //Floor.
        WALL_ABS_INNER_X  , WALL_ABS_INNER_Y  , 0 ,
        -WALL_ABS_INNER_X , WALL_ABS_INNER_Y  , 0 ,
        WALL_ABS_INNER_X  , -WALL_ABS_INNER_Y , 0 ,
        -WALL_ABS_INNER_X , -WALL_ABS_INNER_Y , 0 ,
    };

    static public final float NORMAL_ARRAY[] = {
        //Wall left top.
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,

        //Wall left inner face.
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        0.0f          , NORMAL_SCALE  , 0.0f ,
        0.0f          , NORMAL_SCALE  , 0.0f ,
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        0.0f          , -NORMAL_SCALE , 0.0f ,
        0.0f          , -NORMAL_SCALE , 0.0f ,

        //Wall left outer face.
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        -NORMAL_SCALE , 0.0f          , 0.0f ,
        0.0f          , -NORMAL_SCALE , 0.0f ,
        0.0f          , -NORMAL_SCALE , 0.0f ,
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        NORMAL_SCALE  , 0.0f          , 0.0f ,
        0.0f          , NORMAL_SCALE  , 0.0f ,
        0.0f          , NORMAL_SCALE  , 0.0f ,

        //Floor.
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
        0.0f , 0.0f , NORMAL_SCALE ,
    };

    static public final float VERTEX_COLOR_ARRAY[] = {
        //wall top
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,
        1.0f , 0.0f , 0.0f , 1.0f ,

        //wall inner face.
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,

        //wall outer face.
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,
        0.0f , 1.0f , 0.0f , 1.0f ,

        //Floor.
        0.5f , 0.5f , 0.5f , 1.0f ,
        0.5f , 0.5f , 0.5f , 1.0f ,
        0.5f , 0.5f , 0.5f , 1.0f ,
        0.5f , 0.5f , 0.5f , 1.0f ,
    };

    static public final float TEXTURE_COORD_ARRAY[] = {
        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,
    };
}
