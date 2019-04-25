public class Program {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("input3.py");
        lexer.Parse();
        lexer.SaveResult();
    }
}
