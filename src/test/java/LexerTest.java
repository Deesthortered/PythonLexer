import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class LexerTest {

    void TestLexer(String input) {
        try {
            Lexer lexer = new Lexer(input);
            lexer.Parse();
            lexer.SaveResult();
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
    }

    // На каждый файл делаем свой тест
    @Test
    public void parse1() {
        TestLexer("input1.py");
    }
}