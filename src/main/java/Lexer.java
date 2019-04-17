import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

class Lexer {
    String source_code;
    String result_code;

    static final char EOF = '$';
    int state = 0;
    String buffer1 = "";
    String buffer2 = "";
    int quote_type = -1;

    ArrayList<Token> tokenList = new ArrayList<>();

    Lexer(String source_code_file) throws IOException {
        File input_file = new File(source_code_file);
        byte[] bytes = Files.readAllBytes(input_file.toPath());
        this.source_code = new String(bytes, StandardCharsets.UTF_8);
        this.source_code += EOF;
    }
    void SaveResult() throws IOException {
        FileWriter fileWriter = new FileWriter("result.html");
        fileWriter.write(result_code);
        fileWriter.close();
    }
    void Parse() {
        for (int i = 0; i < source_code.length(); i++) {
            char c = source_code.charAt(i);
            switch (state) {

                case 0 : StartState_EmptyString(c); break;
                case 1 : StartState_NotEmptyString(c); break;
                case 2 : Comment(c); break;
                case 3 : ExtendString1(c); break;
                case 4 : ExtendString2(c); break;
                case 5 : NestingDepth(c); break;

                case 6 : WaitingEqual(c); break;
                case 7 : NeedEqual(c); break;
                case 8 : Multiply1(c); break;
                case 9 : Multiply2(c); break;
                case 10: Slash1(c); break;
                case 11: Slash2(c); break;
                case 12: Greater1(c); break;
                case 13: Greater2(c); break;
                case 14: Less1(c); break;
                case 15: Less2(c); break;

                case 16: DotIsFirst(c); break;
                case 17: DotIsNotFirst(c); break;
                case 18: DecimalNumber(c); break;
                case 19: Number2x8x16x(c); break;
                case 20: BinInt(c); break;
                case 21: OctInt(c); break;
                case 22: HexInt(c); break;
                case 23: NextBinDigit(c); break;
                case 24: NextOctDigit(c); break;
                case 25: NextHexDigit(c); break;
                case 26: FractionDigit(c); break;
                case 27: ExponentSymbol(c); break;
                case 28: Long(c); break;
                case 29: SignSymbol(c); break;
                case 30: ExponentDigit(c); break;
                case 31: Imaginary(c); break;

                case 32: DoubleQuote1(c); break;
                case 33: DoubleQuote2(c); break;
                case 34: DoubleQuote3(c); break;
                case 35: MultiQuoteTwoOne(c); break;

                case 36: OneQuote1(c); break;
                case 37: OneQuote2(c); break;
                case 38: OneQuote3(c); break;
                case 39: MultiQuoteOneTwo(c); break;

                case 40: FirstPrefixLetter(c); break;
                case 41: SecondPrefixLetter(c); break;

                case 42: IdentifierReading(c); break;


                case 43: StringLetterQuoteOne(c); break;

                case 44: StringLetterQuoteOneOneOne(c); break;
                case 45: FinishStringOneOneOne1(c); break;
                case 46: FinishStringOneOneOne2(c); break;

                case 47: StringLetterQuoteOneTwo(c); break;
                case 48: FinishStringOneTwo(c); break;

                case 49: StringLetterQuoteTwo(c); break;

                case 50: StringLetterQuoteTwoTwoTwo(c); break;
                case 51: FinishStringTwoTwoTwo1(c); break;
                case 52: FinishStringTwoTwoTwo2(c); break;

                case 53: StringLetterQuoteTwoOne(c); break;
                case 54: FinishStringTwoOne(c); break;

                case 55: EscapeSequence(c); break;
                case 56: Reading(c); break;

                case 57: UnicodeName(c); break;

                case 58: N1(c); break;
                case 59: E1(c); break;
                case 60: W(c); break;
                case 61: L(c); break;
                case 62: I(c); break;
                case 63: N2(c); break;

                case 64: _32_bitHex1(c); break;
                case 65: _32_bitHex2(c); break;
                case 66: _32_bitHex3(c); break;
                case 67: _32_bitHex4(c); break;
                case 68: _32_bitHex5(c); break;
                case 69: _32_bitHex6(c); break;
                case 70: _32_bitHex7(c); break;
                case 71: _32_bitHex8(c); break;

                case 72: _16_bitHex1(c); break;
                case 73: _16_bitHex2(c); break;
                case 74: _16_bitHex3(c); break;
                case 75: _16_bitHex4(c); break;

                case 76: HexValue1(c); break;
                case 77: HexValue2(c); break;
                case 78: HexValue3(c); break;

                case 79: OctValue1(c); break;
                case 80: OctValue2(c); break;
                case 81: OctValue3(c); break;
                default:
                    System.out.println("ERROR: unknown state");
                    return;
            }
        }
    }
    void goBackToStringLetterAnyQuote(String error_pos) {
        switch (quote_type) {
            case 1: state = 43; break;
            case 2: state = 44; break;
            case 3: state = 47; break;
            case 4: state = 49; break;
            case 5: state = 50; break;
            case 6: state = 53; break;
            default:
                System.out.println("ERROR " + error_pos);
                state = -1;
        }
    }

    void StartState_EmptyString(char input) {
        switch (input) {
            default:
        }
    }    // 0
    void StartState_NotEmptyString(char input) { // 1
        switch (input) {
            default:
        }
    } // 1
    void Comment(char input) { // 2
        switch (input) {
            case '\n':
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                buffer1 = "";
                state = 0;
                break;
            case EOF:
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                tokenList.add(new Token(TokenName.ENDMARKER, ""));
                break;
            default:
                buffer1 += input;
        }
    }     // 2
    void ExtendString1(char input) {

    }  // 3
    void ExtendString2(char input) {

    }  // 4
    void NestingDepth(char input) {

    }   // 5

    void WaitingEqual(char input) {

    }   // 6
    void NeedEqual(char input) {

    }      // 7
    void Multiply1(char input) {

    }      // 8
    void Multiply2(char input) {

    }      // 9
    void Slash1(char input) {

    }         // 10
    void Slash2(char input) {

    }         // 11
    void Greater1(char input) {

    }       // 12
    void Greater2(char input) {

    }       // 13
    void Less1(char input) {

    }          // 14
    void Less2(char input) {

    }          // 15

    void DotIsFirst(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 26;
        } else if (input == '\"') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 36;
        } else if (input == '#') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 2;
        } else if (input == '!') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 7;
        } else if (input == '*') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 8;
        } else if (input == '/') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 10;
        } else if (input == '<') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 14;
        } else if (input == '>') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 12;
        } else if (input == '\n') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, buffer1));
            buffer1 = "";
            state = 12;
        }else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            tokenList.add(new Token(TokenName.OP, "" + input));
            state = 1;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == 'R' || input == 'r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 40;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z')) {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else {
            System.out.println("ERROR 16");
            state = -1;
        }
    }  // 16
    void DotIsNotFirst(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 26;
        } else if (input == '\"') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 36;
        } else if (input == '#') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 2;
        } else if (input == '!') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 7;
        } else if (input == '*') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 8;
        } else if (input == '/') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 10;
        } else if (input == '<') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 14;
        } else if (input == '>') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 12;
        } else if (input == '\n') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, buffer1));
            buffer1 = "";
            state = 12;
        }else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            tokenList.add(new Token(TokenName.NUMBER, "" + input));
            state = 1;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == 'R' || input == 'r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 40;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z')) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else {
            System.out.println("ERROR 16");
            state = -1;
        }
    }  // 17
    void DecimalNumber(char input) {

    }  // 18
    void Number2x8x16x(char input) {

    }  // 19
    void BinInt(char input) {

    }         // 20
    void OctInt(char input) {

    }         // 21
    void HexInt(char input) {

    }         // 22
    void NextBinDigit(char input) {

    }   // 23
    void NextOctDigit(char input) {

    }   // 24
    void NextHexDigit(char input) {

    }   // 25
    void FractionDigit(char input) {

    }  // 26
    void ExponentSymbol(char input) {

    } // 27
    void Long(char input) {

    }           // 28
    void SignSymbol(char input) {

    }     // 29
    void ExponentDigit(char input){

    }   // 30
    void Imaginary(char input) {

    }      // 31

    void DoubleQuote1(char input) {
        switch (input) {
            case '\'':
                buffer1 += input;
                state = 35;
                break;
            case '\"':
                buffer1 += input;
                state = 33;
                break;
            default:
                System.out.println("ERROR 32");
                state = -1;
        }
    }       // 32
    void DoubleQuote2(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 34;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
        } else if (input == 'R' || input == 'r') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 40;
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z')) {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '\n') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            tokenList.add(new Token(TokenName.OP, "" + input));
            state = 1;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else {
            System.out.println("ERROR 33");
            state = -1;
        }
    }       // 33
    void DoubleQuote3(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 51;
        } else {
            buffer1 += input;
            state = 50;
        }
    }       // 34
    void MultiQuoteTwoOne(char input) {
        if (input == '\'') {
            buffer1 += input;
            state = 54;
        } else {
            buffer1 += input;
            state = 53;
        }
    }   // 35

    void OneQuote1(char input) {
        switch (input) {
            case '\'':
                buffer1 += input;
                state = 36;
                break;
            case '\"':
                buffer1 += input;
                state = 39;
                break;
            default:
                System.out.println("ERROR 36");
                state = -1;
        }
    }          // 36
    void OneQuote2(char input) {
        if (input == '\'') {
            buffer1 += input;
            state = 38;
        }  else if (input == '\"') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == 'R' || input == 'r') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 40;
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z')) {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '\n') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            tokenList.add(new Token(TokenName.OP, "" + input));
            state = 1;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else {
            System.out.println("ERROR 37");
            state = -1;
        }
    }          // 37
    void OneQuote3(char input) {
        if (input == '\'') {
            buffer1 += input;
            state = 45;
        } else {
            buffer1 += input;
            state = 44;
        }
    }          // 38
    void MultiQuoteOneTwo(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 48;
        } else {
            buffer1 += input;
            state = 47;
        }
    }   // 39

    void FirstPrefixLetter(char input) {
        if (input == 'R' || input == 'r') {
            buffer1 += input;
            state = 41;
        } else if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') ) {
            buffer1 += input;
            state = 42;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 16;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\"') {
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            state = 36;
        } else if (input == '\n') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else {
            System.out.println("ERROR 40");
            state = -1;
        }
    }  // 40
    void SecondPrefixLetter(char input) {
        if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') ) {
            buffer1 += input;
            state = 42;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\"') {
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            state = 36;
        } else if (input == '\n') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else {
            System.out.println("ERROR 41");
            state = -1;
        }
    } // 41

    void IdentifierReading(char input) {
        if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == '\n') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.ENDMARKER, ""));
        } else if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') ) {
            buffer1 += input;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if (input == '\"') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 36;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else {
            System.out.println("ERROR 42");
            state = -1;
        }
    }  // 42


    void StringLetterQuoteOne(char input) {
        quote_type = 1;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\'':
                buffer1 += input;
                tokenList.add(new Token(TokenName.STRING, buffer1));
                buffer1 = "";
                state = 1;
                break;
            default: {
                buffer1 += input;
            }
        }
    }       // 43 & 1

    void StringLetterQuoteOneOneOne(char input) {
        quote_type = 2;
        quote_type = 6;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\'':
                buffer1 += input;
                state = 45;
                break;
            default: {
                buffer1 += input;
            }
        }

    } // 44 & 2
    void FinishStringOneOneOne1(char input){
        if (input == '\'') {
            buffer1 += input;
            state = 46;
        } else {
            System.out.println("ERROR 45");
            state = -1;
        }
    }      // 45
    void FinishStringOneOneOne2(char input){
        if (input == '\'') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            state = 1;
        } else {
            System.out.println("ERROR 46");
            state = -1;
        }
    }      // 46

    void StringLetterQuoteOneTwo(char input) {
        quote_type = 3;
        quote_type = 6;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\"':
                buffer1 += input;
                state = 48;
                break;
            default: {
                buffer1 += input;
            }
        }
    }    // 47 & 3
    void FinishStringOneTwo(char input) {
        if (input == '\'') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            state = 1;
        } else {
            System.out.println("ERROR 48");
            state = -1;
        }
    }         // 48

    void StringLetterQuoteTwo(char input) {
        quote_type = 4;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\"':
                buffer1 += input;
                tokenList.add(new Token(TokenName.STRING, buffer1));
                buffer1 = "";
                state = 1;
                break;
            default: {
                buffer1 += input;
            }
        }
    }       // 49 & 4

    void StringLetterQuoteTwoTwoTwo(char input) {
        quote_type = 5;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\"':
                buffer1 += input;
                state = 51;
                break;
            default: {
                buffer1 += input;
            }
        }
    } // 50 & 5
    void FinishStringTwoTwoTwo1(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 52;
        } else {
            System.out.println("ERROR 51");
            state = -1;
        }
    }     // 51
    void FinishStringTwoTwoTwo2(char input) {
        if (input == '\"') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            state = 1;
        } else {
            System.out.println("ERROR 52");
            state = -1;
        }
    }     // 52

    void StringLetterQuoteTwoOne(char input) {
        quote_type = 6;
        switch (input) {
            case '\\':
                state = 55;
                break;
            case '\'':
                buffer1 += input;
                state = 54;
                break;
            default: {
                buffer1 += input;
            }
        }
    }    // 53 & 6
    void FinishStringTwoOne(char input) {
        if (input == '\"') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.STRING, buffer1));
            buffer1 = "";
            state = 1;
        } else {
            System.out.println("ERROR 53");
            state = -1;
        }
    }         // 54


    void EscapeSequence(char input) {
        switch (input) {
            case 'U':
                buffer2 = "";
                state = 64;
                break;
            case 'u':
                buffer2 = "";
                state = 72;
                break;
            case 'x':
                buffer2 = "";
                state = 76;
                break;
            case 'N':
                buffer2 = "";
                state = 57;
                break;
            case 'n':
                state = 58;
                break;
            case '\\':
                buffer1 += '\\';
                goBackToStringLetterAnyQuote("55 1");
                break;
            case '\"':
                buffer1 += '\"';
                goBackToStringLetterAnyQuote("55 2");
                break;
            case '\'':
                buffer1 += '\'';
                goBackToStringLetterAnyQuote("55 3");
                break;
            case 'a':
                buffer1 += "\\a"; // There is no character in Java
                goBackToStringLetterAnyQuote("55 4");
                break;
            case 'b':
                buffer1 += "\b";
                goBackToStringLetterAnyQuote("55 5");
                break;
            case 'f':
                buffer1 += "\f";
                goBackToStringLetterAnyQuote("55 6");
                break;
            case 'r':
                buffer1 += "\r";
                goBackToStringLetterAnyQuote("55 7");
            case 't':
                buffer1 += "\t";
                goBackToStringLetterAnyQuote("55 8");
                break;
            case 'v':
                buffer1 += "\\v";  // There is no character in Java
                goBackToStringLetterAnyQuote("55 9");
                break;
            default:
                if ('0' <= input && input <= '3') {
                    buffer2 = "";
                    buffer2 += input;
                    state = 79;
                } else if ('4' <= input && input <= '7') {
                    buffer2 = "";
                    buffer2 += input;
                    state = 80;
                } else {
                    System.out.println("ERROR 55");
                    state = -1;
                }
        }
    } // 55
    void Reading(char input) {
        if (input == '}') {
            boolean is_exist = true; // Let it be
            buffer1 += '۞'; // just for example
            if (is_exist) {
                goBackToStringLetterAnyQuote("56 1");
            } else {
                System.out.println("ERROR 56 2");
                state = -1;
            }
        } else {
            buffer2 += input;
        }
    }        // 56

    void UnicodeName(char input) {
        if (input == '{') {
            state = 56;
        } else {
            System.out.println("ERROR 57");
            state = -1;
        }
    }    // 57

    void N1(char input) {
        if (input == '\\') {
            buffer1 += '\n';
            state = 55;
        } else if (input == 'e') {
            state = 59;
        } else {
            switch (quote_type) {
                case 1:
                    if (input == '\'') {
                        buffer1 += '\n';
                        buffer1 += input;
                        tokenList.add(new Token(TokenName.STRING, buffer1));
                        buffer1 = "";

                        state = 1;
                    } else {
                        buffer1 += '\n';
                        state = 43;
                    }
                    break;
                case 2:
                    if (input == '\'') {
                        buffer1 += '\n';
                        buffer1 += input;
                        state = 45;
                    } else {
                        buffer1 += '\n';
                        state = 44;
                    }
                    break;
                case 3:
                    if (input == '\"') {
                        buffer1 += '\n';
                        buffer1 += input;
                        state = 48;
                    } else {
                        buffer1 += '\n';
                        state = 47;
                    }
                    break;
                case 4:
                    if (input == '\"') {
                        buffer1 += '\n';
                        buffer1 += input;
                        tokenList.add(new Token(TokenName.STRING, buffer1));
                        buffer1 = "";

                        state = 1;
                    } else {
                        buffer1 += '\n';
                        state = 49;
                    }
                    break;
                case 5:
                    if (input == '\"') {
                        buffer1 += '\n';
                        buffer1 += input;
                        state = 51;
                    } else {
                        buffer1 += '\n';
                        state = 50;
                    }
                    break;
                case 6:
                    if (input == '\'') {
                        buffer1 += '\n';
                        buffer1 += input;
                        state = 54;
                    } else {
                        buffer1 += '\n';
                        state = 53;
                    }
                    break;
                default:
                    System.out.println("ERROR 63 7");
                    state = -1;
            }
        }
    } // 58
    void E1(char input) {
        if (input == 'w') {
            state = 60;
        } else {
            System.out.println("ERROR 59");
            state = -1;
        }
    } // 59
    void W(char input) {
        if (input == 'l') {
            state = 61;
        } else {
            System.out.println("ERROR 60");
            state = -1;
        }
    }  // 60
    void L(char input) {
        if (input == 'i') {
            state = 62;
        } else {
            System.out.println("ERROR 61");
            state = -1;
        }
    }  // 61
    void I(char input) {
        if (input == 'n') {
            state = 63;
        } else {
            System.out.println("ERROR 62");
            state = -1;
        }
    }  // 62
    void N2(char input) {
        if (input == 'e') {
            goBackToStringLetterAnyQuote("63 1");
        } else {
            System.out.println("ERROR 63 2");
            state = -1;
        }
    } // 63

    void _32_bitHex1(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 65;
        } else {
            System.out.println("ERROR 64");
        }
    } // 64
    void _32_bitHex2(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 66;
        } else {
            System.out.println("ERROR 65");
        }
    } // 65
    void _32_bitHex3(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 67;
        } else {
            System.out.println("ERROR 66");
        }
    } // 66
    void _32_bitHex4(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 68;
        } else {
            System.out.println("ERROR 67");
        }
    } // 67
    void _32_bitHex5(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 69;
        } else {
            System.out.println("ERROR 68");
        }
    } // 68
    void _32_bitHex6(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 70;
        } else {
            System.out.println("ERROR 69");
        }
    } // 69
    void _32_bitHex7(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 71;
        } else {
            System.out.println("ERROR 70");
        }
    } // 70
    void _32_bitHex8(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("71 1");
        } else {
            System.out.println("ERROR 71 2");
        }
    } // 71

    void _16_bitHex1(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 73;
        } else {
            System.out.println("ERROR 72");
        }
    } // 72
    void _16_bitHex2(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 74;
        } else {
            System.out.println("ERROR 73");
        }
    } // 73
    void _16_bitHex3(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 75;
        } else {
            System.out.println("ERROR 74");
        }
    } // 74
    void _16_bitHex4(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("75 1");
        } else {
            System.out.println("ERROR 75 2");
        }
    } // 75

    void HexValue1(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 77;
        } else {
            System.out.println("ERROR 76");
        }
    }  // 76
    void HexValue2(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 78;
        } else {
            System.out.println("ERROR 77");
        }
    }  // 77
    void HexValue3(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("78 1");
        } else {
            System.out.println("ERROR 78 2");
        }
    }  // 78

    void OctValue1(char input) {
        if ('0' <= input && input <= '7') {
            buffer2 += input;
            state = 80;
        } else {
            System.out.println("ERROR 79");
            state = -1;
        }
    }  // 79
    void OctValue2(char input) {
        if ('0' <= input && input <= '7') {
            buffer2 += input;
            state = 81;
        } else {
            System.out.println("ERROR 80");
            state = -1;
        }
    }  // 80
    void OctValue3(char input) {
        if ('0' <= input && input <= '7') {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 8);
            goBackToStringLetterAnyQuote("81 1");
        } else {
            System.out.println("ERROR 81 2");
            state = -1;
        }
    }  // 81
}