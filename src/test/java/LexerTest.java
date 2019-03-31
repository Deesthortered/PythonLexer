import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

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
    void parse1() {
        TestLexer("input1.py");
    }
}