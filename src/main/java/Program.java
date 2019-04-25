public class Program {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("input1.py");
        lexer.Parse();
        lexer.SaveResult();
    }
}
