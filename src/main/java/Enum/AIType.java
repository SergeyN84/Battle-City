package Enum;

public enum AIType {
    BASIC("tank_basic"), ARMOR("tank_armor"), POWER("tank_power"), FAST("tank_fast");

    private String typeString;

    AIType(String typeStr) {
        typeString = typeStr;
    }

    public String getAITypeString() {
        return typeString;
    }
}
