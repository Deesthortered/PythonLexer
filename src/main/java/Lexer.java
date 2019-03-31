import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

// Наш главный класс, с ним и работаем
class Lexer {
    String source_code;
    String result_code;

    // Тут ниче не трогай, пускай так и будет - читаем .PY файл, получаем HTML файл.
    Lexer(String source_code_file) throws IOException {
        File input_file = new File(source_code_file);
        byte[] bytes = Files.readAllBytes(input_file.toPath());
        this.source_code = new String(bytes, StandardCharsets.UTF_8);
    }
    void SaveResult() throws IOException {
        FileWriter fileWriter = new FileWriter("result.html");
        fileWriter.write(result_code);
        fileWriter.close();
    }

    // Вот тут и вся магия
    void Parse() {
        // Чисто для прикола, сотрем потом
        result_code = source_code;
    }
}

// Пиши комменты там, где считаешь нужным, чтоб мне потом не обьяснять ничего.
// Ща их нахуячил, шоб все ясно было и наконец-то впервые появился повод их писать))