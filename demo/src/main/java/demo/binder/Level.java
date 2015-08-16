package demo.binder;

/**
 * Created by woniper on 15. 4. 28..
 */
public enum Level {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private int value;
    private Level nextLevel;

    Level(int value, Level nextLevel) {
        this.value = value;
        this.nextLevel = nextLevel;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: return null;
        }
    }
}
