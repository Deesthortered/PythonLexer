class Token {
    TokenName name;
    String value;

    Token(TokenName name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "< " + name + " | " + value + " >";
    }
}
