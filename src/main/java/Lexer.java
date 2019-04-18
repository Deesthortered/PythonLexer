import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;

class Lexer {
    private String source_code;
    private String result_code;

    private static final char EOF = '$';
    private LinkedList<Integer> depth_stack = new LinkedList<>();
    private int counter = 0;
    private int state = 0;
    private String buffer1 = "";
    private String buffer2 = "";
    private int quote_type = -1;

    private ArrayList<Token> tokenList = new ArrayList<>();

    Lexer(String source_code_file){
        File input_file = new File(source_code_file);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(input_file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.source_code = new String(bytes, StandardCharsets.UTF_8);
        this.source_code += EOF;
        depth_stack.addFirst(0);
    }
    void SaveResult(){
        for (Token token : tokenList) {
            System.out.println(token);
        }
    }
    void Parse() {
        for (int i = 0; i < source_code.length(); i++) {
            char c = source_code.charAt(i);
            switch (state) {

                case 0 : StartState_EmptyString(c); break;
                case 1 : StartState_NotEmptyString(c); break;
                case 2 : Comment1(c); break;
                case 9 : Comment2(c); break;
                case 3 : ExtendString1(c); break;
                case 4 : ExtendString2(c); break;
                case 5 : NestingDepth(c); break;

                case 6 : WaitingEqual(c); break;
                case 7 : NeedEqual(c); break;
                case 8 : Multiply1(c); break;
                case 10: Slash1(c); break;
                case 12: Greater1(c); break;
                case 14: Less1(c); break;
                case 15: WaitingEqual2(c); break;

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
                case 29: SignSymbol(c); break;
                case 30: ExponentDigit(c); break;
                case 31: ImaginaryLong(c); break;

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
        while (depth_stack.getFirst() != 0) {
            tokenList.add(new Token(TokenName.DEDENT, ""));
            depth_stack.pollFirst();
        }
        tokenList.add(new Token(TokenName.ENDMARKER, ""));
    }
    private void goBackToStringLetterAnyQuote(String error_pos) {
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

    private void StartState_EmptyString(char input) {
        counter = 0;
        if (input == EOF) {

        } else if (input == ' ') {
            state = 5;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NL, ""));
            state = 0;
        } else if (input == '#') {
            buffer1 += input;
            state = 9;
        } else if (input == '\\') {
            state = 4;
        }

        else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.OP, "" + input));
            state = 1;
        } else if (input == '!') {
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            buffer1 += input;
            state = 12;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            buffer1 += input;
            state = 6;
        }

        else if (input == '\"') {
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            state = 36;
        } else if (input == 'R' || input == 'r') {
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            buffer1 += input;
            state = 40;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
            state = 42;
        } else if (input == '.') {
            buffer1 += input;
            state = 16;
        } else if (input == '0') {
            buffer1 += input;
            state = 19;
        } else if ('1' <= input && input <= '9') {
            buffer1 += input;
            state = 18;
        } else {
            System.out.println("ERROR 0");
            state = -1;
        }
    }    // 0
    private void StartState_NotEmptyString(char input) { // 1
        if (input == EOF) {

        } else if (input == ' ') {
            // nothing
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            state = 0;
        } else if (input == '#') {
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            state = 3;
        }

        else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.OP, "" + input));
        } else if (input == '!') {
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            buffer1 += input;
            state = 12;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            buffer1 += input;
            state = 6;
        }

        else if (input == '\"') {
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            buffer1 += input;
            state = 36;
        } else if (input == 'R' || input == 'r') {
            buffer1 += input;
            state = 41;
        } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
            buffer1 += input;
            state = 40;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
            state = 42;
        } else if (input == '.') {
            buffer1 += input;
            state = 16;
        } else if (input == '0') {
            buffer1 += input;
            state = 19;
        } else if ('1' <= input && input <= '9') {
            buffer1 += input;
            state = 18;
        } else {
            System.out.println("ERROR 1");
            state = -1;
        }
    } // 1
    private void Comment1(char input) { // 2
        switch (input) {
            case '\r':
            case '\n':
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                tokenList.add(new Token(TokenName.NEWLINE, ""));
                buffer1 = "";
                state = 0;
                break;
            case EOF:
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                break;
            default:
                buffer1 += input;
        }
    }       // 2
    private void Comment2(char input) {
        switch (input) {
            case '\r':
            case '\n':
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                buffer1 = "";
                state = 0;
                break;
            case EOF:
                tokenList.add(new Token(TokenName.COMMENT, buffer1));
                break;
            default:
                buffer1 += input;
        }
    }       // 9
    private void ExtendString1(char input) {
        if (input == EOF) {

        } else if (input == '\n' || input == '\r') {
            state = 1;
        } else {
            System.out.println("ERROR 3");
            state = -1;
        }
    }  // 3
    private void ExtendString2(char input) {
        if (input == EOF) {

        } else if (input == '\n' || input == '\r') {
            state = 0;
        } else {
            System.out.println("ERROR 4");
            state = -1;
        }
    }  // 4
    private void NestingDepth(char input) {
        counter++;
        if (input == ' ') {
            // nothing
        } else if (input == '#') {
            buffer1 += input;
            state = 9;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.DEDENT, ""));
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NL, ""));
            state = 0;
        } else {
            int prev_depth = depth_stack.getFirst();
            if (prev_depth < counter) {
                tokenList.add(new Token(TokenName.INDENT, ""));
                depth_stack.addFirst(counter);
            } else if (prev_depth > counter) {
                depth_stack.pollFirst();
                if (depth_stack.getFirst() == counter)
                    tokenList.add(new Token(TokenName.DEDENT, ""));
                else
                    tokenList.add(new Token(TokenName.ERRORTOKEN, ""));
            }

            if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
                tokenList.add(new Token(TokenName.OP, "" + input));
                state = 1;
            } else if (input == '!') {
                buffer1 += input;
                state = 7;
            } else if (input == '*') {
                buffer1 += input;
                state = 8;
            } else if (input == '/') {
                buffer1 += input;
                state = 10;
            } else if (input == '<') {
                buffer1 += input;
                state = 14;
            } else if (input == '>') {
                buffer1 += input;
                state = 12;
            } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
                buffer1 += input;
                state = 6;
            }

            else if (input == '\"') {
                buffer1 += input;
                state = 32;
            } else if (input == '\'') {
                buffer1 += input;
                state = 36;
            } else if (input == 'R' || input == 'r') {
                buffer1 += input;
                state = 41;
            } else if (input == 'U' || input == 'u' || input == 'B' || input == 'b') {
                buffer1 += input;
                state = 40;
            } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
                buffer1 += input;
                state = 42;
            } else if (input == '.') {
                buffer1 += input;
                state = 16;
            } else if (input == '0') {
                buffer1 += input;
                state = 19;
            } else if ('1' <= input && input <= '9') {
                buffer1 += input;
                state = 18;
            } else {
                System.out.println("ERROR 5");
                state = -1;
            }
        }
    }   // 5

    private void WaitingEqual(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 6");
            state = -1;
        }
    }   // 6
    private void NeedEqual(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        } else {
            System.out.println("ERROR 7");
            state = -1;
        }
    }      // 7
    private void Multiply1(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '*') {
            buffer1 += input;
            state = 15;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 8");
            state = -1;
        }
    }      // 8
    private void Slash1(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '/') {
            buffer1 += input;
            state = 15;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 10");
            state = -1;
        }
    }         // 10
    private void Greater1(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '>') {
            buffer1 += input;
            state = 15;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 12");
            state = -1;
        }
    }       // 12
    private void Less1(char input) {
        if (input == '=' || input == '>') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '<') {
            buffer1 += input;
            state = 15;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 14");
            state = -1;
        }
    }          // 14
    private void WaitingEqual2(char input) {
        if (input == '=') {
            buffer1 += input;
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            state = 1;
        }

        else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
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
        } else if (input == '.') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 16;
        } else if ('1' <= input && input <= '9') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 18;
        } else if (input == '0') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 19;
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '\\') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 3;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 15");
            state = -1;
        }
    }  // 15

    private void DotIsFirst(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 26;
        } else if (input == '\"') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '\n' || input == '\r') {
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
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.OP, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.OP, buffer1));
        } else {
            System.out.println("ERROR 16");
            state = -1;
        }
    }  // 16
    private void DotIsNotFirst(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 26;
        } else if (input == '\"') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 36;
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, buffer1));
            buffer1 += input;
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
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 42;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else {
            System.out.println("ERROR 17");
            state = -1;
        }
    }  // 17
    private void DecimalNumber(char input) {
        if (input == 'E' || input == 'e') {
            buffer1 += input;
            state = 27;
        } else if (input == 'L' || input == 'l') {
            buffer1 += input;
            state = 31;
        } else if (input == 'J' || input == 'j') {
            buffer1 += input;
            state = 31;
        } else if ('0' <= input && input <= '9') {
            buffer1 += input;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 18");
            state = -1;
        }
    }  // 18
    private void Number2x8x16x(char input) {
        if (input == 'B' || input == 'b') {
            buffer1 += input;
            state = 20;
        } else if (input == 'O' || input == 'o') {
            buffer1 += input;
            state = 21;
        } else if (input == 'X' || input == 'x') {
            buffer1 += input;
            state = 22;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 19");
            state = -1;
        }
    }  // 19
    private void BinInt(char input) {
        if ('0' <= input && input <= '1') {
            buffer1 += input;
            state = 23;
        } else {
            System.out.println("ERROR 20");
            state = -1;
        }
    }         // 20
    private void OctInt(char input) {
        if ('0' <= input && input <= '7') {
            buffer1 += input;
            state = 24;
        } else {
            System.out.println("ERROR 21");
            state = -1;
        }
    }         // 21
    private void HexInt(char input) {
        if (('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
            state = 25;
        } else {
            System.out.println("ERROR 22");
            state = -1;
        }
    }         // 22
    private void NextBinDigit(char input) {
        if ('0' <= input && input <= '1') {
            buffer1 += input;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 23");
            state = -1;
        }
    }   // 23
    private void NextOctDigit(char input) {
        if ('0' <= input && input <= '7') {
            buffer1 += input;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 24");
            state = -1;
        }
    }   // 24
    private void NextHexDigit(char input) {
        if (('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 25");
            state = -1;
        }
    }   // 25
    private void FractionDigit(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
        } else if (input == 'E' || input == 'e') {
            buffer1 += input;
            state = 27;
        } else if (input == 'J' || input == 'j') {
            buffer1 += input;
            state = 31;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 26");
            state = -1;
        }
    }  // 26
    private void ExponentSymbol(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 30;
        } else if (input == '+' || input == '-') {
            buffer1 += input;
            state = 29;
        } else {
            System.out.println("ERROR 27");
            state = -1;
        }
    } // 27
    private void SignSymbol(char input) {
        if ('0' <= input && input <= '9') {
            buffer1 += input;
            state = 30;
        } else {
            System.out.println("ERROR 29");
            state = -1;
        }
    }     // 29
    private void ExponentDigit(char input){
        if ('0' <= input && input <= '9') {
            buffer1 += input;
        }

        else if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 30");
            state = -1;
        }
    }   // 30
    private void ImaginaryLong(char input) {
        if (input == '\\') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
        } else if (input == '#') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 2;
        } else if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
        } else if (input == '!') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 7;
        } else if (input == '*') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 8;
        } else if (input == '/') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 10;
        } else if (input == '<') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 14;
        } else if (input == '>') {
            tokenList.add(new Token(TokenName.NUMBER, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 12;
        } else {
            System.out.println("ERROR 31");
            state = -1;
        }
    }   // 31

    private void DoubleQuote1(char input) {
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
                quote_type = 4;
                buffer1 += input;
                goBackToStringLetterAnyQuote("32");
        }
    }       // 32
    private void DoubleQuote2(char input) {
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
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
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
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == EOF) {

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
    private void DoubleQuote3(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 51;
        } else {
            buffer1 += input;
            state = 50;
        }
    }       // 34
    private void MultiQuoteTwoOne(char input) {
        if (input == '\'') {
            buffer1 += input;
            state = 54;
        } else {
            buffer1 += input;
            state = 53;
        }
    }   // 35

    private void OneQuote1(char input) {
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
                quote_type = 1;
                buffer1 += input;
                goBackToStringLetterAnyQuote("36");
        }
    }          // 36
    private void OneQuote2(char input) {
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
        } else if (('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
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
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.STRING, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            buffer1 += input;
            state = 0;
        } else if (input == EOF) {

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
    private void OneQuote3(char input) {
        if (input == '\'') {
            buffer1 += input;
            state = 45;
        } else {
            buffer1 += input;
            state = 44;
        }
    }          // 38
    private void MultiQuoteOneTwo(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 48;
        } else {
            buffer1 += input;
            state = 47;
        }
    }   // 39

    private void FirstPrefixLetter(char input) {
        if (input == 'R' || input == 'r') {
            buffer1 += input;
            state = 41;
        } else if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
            state = 42;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
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
        } else if (input == '\n' || input == '\r') {
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
            tokenList.add(new Token(TokenName.NAME, buffer1));
        } else {
            System.out.println("ERROR 40");
            state = -1;
        }
    }  // 40
    private void SecondPrefixLetter(char input) {
        if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
            state = 42;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
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
        } else if (input == '\n' || input == '\r') {
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
            tokenList.add(new Token(TokenName.NAME, buffer1));
        } else {
            System.out.println("ERROR 41");
            state = -1;
        }
    } // 41

    private void IdentifierReading(char input) {
        if (input == ',' || input == '(' || input == ')' || input == '[' || input == ']' || input == '{' || input == '}' || input == '`' || input == ':' || input == ';' || input == '~') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.OP, "" + input));
            buffer1 = "";
            state = 1;
        } else if (input == '\n' || input == '\r') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            tokenList.add(new Token(TokenName.NEWLINE, ""));
            buffer1 = "";
            state = 0;
        } else if (input == ' ') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            state = 1;
        } else if (input == EOF) {
            tokenList.add(new Token(TokenName.NAME, buffer1));
        } else if ( ('0' <= input && input <= '9') || ('A' <= input && input <= 'Z') || ('a' <= input && input <= 'z') || input == '_') {
            buffer1 += input;
        } else if (input == '+' || input == '-' || input == '%' || input == '&' || input == '|' || input == '^' || input == '=') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 6;
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
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
            state = 32;
        } else if (input == '\'') {
            tokenList.add(new Token(TokenName.NAME, buffer1));
            buffer1 = "";
            buffer1 += input;
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


    private void StringLetterQuoteOne(char input) {
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

    private void StringLetterQuoteOneOneOne(char input) {
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
    private void FinishStringOneOneOne1(char input){
        if (input == '\'') {
            buffer1 += input;
            state = 46;
        } else {
            System.out.println("ERROR 45");
            state = -1;
        }
    }      // 45
    private void FinishStringOneOneOne2(char input){
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

    private void StringLetterQuoteOneTwo(char input) {
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
    private void FinishStringOneTwo(char input) {
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

    private void StringLetterQuoteTwo(char input) {
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

    private void StringLetterQuoteTwoTwoTwo(char input) {
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
    private void FinishStringTwoTwoTwo1(char input) {
        if (input == '\"') {
            buffer1 += input;
            state = 52;
        } else {
            System.out.println("ERROR 51");
            state = -1;
        }
    }     // 51
    private void FinishStringTwoTwoTwo2(char input) {
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

    private void StringLetterQuoteTwoOne(char input) {
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
    private void FinishStringTwoOne(char input) {
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


    private void EscapeSequence(char input) {
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
    private void Reading(char input) {
        if (input == '}') {
            boolean is_exist = true; // Let it be
            buffer1 += '?'; // just for example
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

    private void UnicodeName(char input) {
        if (input == '{') {
            state = 56;
        } else {
            System.out.println("ERROR 57");
            state = -1;
        }
    }    // 57

    private void N1(char input) {
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
    private void E1(char input) {
        if (input == 'w') {
            state = 60;
        } else {
            System.out.println("ERROR 59");
            state = -1;
        }
    } // 59
    private void W(char input) {
        if (input == 'l') {
            state = 61;
        } else {
            System.out.println("ERROR 60");
            state = -1;
        }
    }  // 60
    private void L(char input) {
        if (input == 'i') {
            state = 62;
        } else {
            System.out.println("ERROR 61");
            state = -1;
        }
    }  // 61
    private void I(char input) {
        if (input == 'n') {
            state = 63;
        } else {
            System.out.println("ERROR 62");
            state = -1;
        }
    }  // 62
    private void N2(char input) {
        if (input == 'e') {
            goBackToStringLetterAnyQuote("63 1");
        } else {
            System.out.println("ERROR 63 2");
            state = -1;
        }
    } // 63

    private void _32_bitHex1(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 65;
        } else {
            System.out.println("ERROR 64");
        }
    } // 64
    private void _32_bitHex2(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 66;
        } else {
            System.out.println("ERROR 65");
        }
    } // 65
    private void _32_bitHex3(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 67;
        } else {
            System.out.println("ERROR 66");
        }
    } // 66
    private void _32_bitHex4(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 68;
        } else {
            System.out.println("ERROR 67");
        }
    } // 67
    private void _32_bitHex5(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 69;
        } else {
            System.out.println("ERROR 68");
        }
    } // 68
    private void _32_bitHex6(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 70;
        } else {
            System.out.println("ERROR 69");
        }
    } // 69
    private void _32_bitHex7(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 71;
        } else {
            System.out.println("ERROR 70");
        }
    } // 70
    private void _32_bitHex8(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("71 1");
        } else {
            System.out.println("ERROR 71 2");
        }
    } // 71

    private void _16_bitHex1(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 73;
        } else {
            System.out.println("ERROR 72");
        }
    } // 72
    private void _16_bitHex2(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 74;
        } else {
            System.out.println("ERROR 73");
        }
    } // 73
    private void _16_bitHex3(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 75;
        } else {
            System.out.println("ERROR 74");
        }
    } // 74
    private void _16_bitHex4(char input){
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("75 1");
        } else {
            System.out.println("ERROR 75 2");
        }
    } // 75

    private void HexValue1(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 77;
        } else {
            System.out.println("ERROR 76");
        }
    }  // 76
    private void HexValue2(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            state = 78;
        } else {
            System.out.println("ERROR 77");
        }
    }  // 77
    private void HexValue3(char input) {
        if ( ('0' <= input && input <= '9') || (('A' <= input && input <= 'F')) || (('a' <= input && input <= 'f'))) {
            buffer2 += input;
            buffer1 += (char)Integer.parseInt(buffer2, 16);
            goBackToStringLetterAnyQuote("78 1");
        } else {
            System.out.println("ERROR 78 2");
        }
    }  // 78

    private void OctValue1(char input) {
        if ('0' <= input && input <= '7') {
            buffer2 += input;
            state = 80;
        } else {
            System.out.println("ERROR 79");
            state = -1;
        }
    }  // 79
    private void OctValue2(char input) {
        if ('0' <= input && input <= '7') {
            buffer2 += input;
            state = 81;
        } else {
            System.out.println("ERROR 80");
            state = -1;
        }
    }  // 80
    private void OctValue3(char input) {
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