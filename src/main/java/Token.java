// Тут я просто опишу токены, какие они ваще бывают и через этот класс они будут использоваться
// при парсинге и выводе в HTML.

public enum Token {
    // Перечислены все токены. Источник: https://docs.python.org/2/library/token.html

    ENDMARKER,  //
    NAME,       //
    NUMBER,     //
    STRING,     //
    NEWLINE,    //
    INDENT,     //
    DEDENT,     //
    LPAR,       //
    RPAR,       //
    LSQB,       //
    RSQB,       //
    COLON,      //
    COMMA,      //
    SEMI,       //
    PLUS,       //
    MINUS,      //
    STAR,       //
    SLASH,      //
    VBAR,       //
    AMPER,      //
    LESS,       //
    GREATER,    //
    EQUAL,      //
    DOT,        //
    PERCENT,    //
    BACKQUOTE,  //
    LBRACE,     //
    RBRACE,     //
    EQEQUAL,    //
    NOTEQUAL,   //
    LESSEQUAL,  //
    GREATEREQUAL, //
    TILDE,        //
    CIRCUMFLEX,   //
    LEFTSHIFT,    //
    RIGHTSHIFT,   //
    DOUBLESTAR,   //
    PLUSEQUAL,    //
    MINEQUAL,     //
    STAREQUAL,    //
    SLASHEQUAL,   //
    PERCENTEQUAL, //
    AMPEREQUAL,   //
    VBAREQUAL,    //
    CIRCUMFLEXEQUAL,  //
    LEFTSHIFTEQUAL,   //
    RIGHTSHIFTEQUAL,  //
    DOUBLESTAREQUAL,  //
    DOUBLESLASH,      //
    DOUBLESLASHEQUAL, //
    AT,               //
    OP,               //
    ERRORTOKEN,       //
    N_TOKENS,         //
    NT_OFFSET;        //
}
