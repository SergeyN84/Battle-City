package Enum;

public enum TankDirection {
    UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");
    private String directionString;
    TankDirection(String dirStr) {
        directionString = dirStr;
    }

    public String getDirectionString() {
        return directionString;
    }
}
