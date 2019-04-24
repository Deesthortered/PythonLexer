class Token {
    TokenName name;
    String value;

    private int spaceOffset1 = 10;
    private int spaceOffset2 = 8;

    Token(TokenName name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        String s = "";
        String s2 = "";
        for (int i = name.toString().length(); i < spaceOffset1; i++)
            s += " ";
        for (int i = value.toString().length(); i < spaceOffset2; i++)
            s2 += " ";
        return "< " + s + name + "  | " + value + s2 + " >";
    }
}
