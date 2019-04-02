// Тут я просто опишу токены, какие они ваще бывают и через этот класс они будут использоваться
// при парсинге и выводе в HTML.

public enum TokenName {
    // Перечислены все токены. Источник: https://docs.python.org/2/library/token.html

    NEWLINE,    // Токен новой логической строки
    INDENT,     // Токен повышения уровня вложености
    DEDENT,     // Токен понижения уровня вложености

    PLUS,             // Операция +
    MINUS,            // Операция -
    STAR,             // Операция *
    SLASH,            // Операция /
    DOUBLESLASH,      // Операция //
    PERCENT,          // Операция %
    DOUBLESTAR,       // Операция **
    PLUSEQUAL,        // Операция +=
    MINEQUAL,         // Операция -=
    STAREQUAL,        // Операция *=
    SLASHEQUAL,       // Операция /=
    DOUBLESLASHEQUAL, // Операция //=
    PERCENTEQUAL,     // Операция %=
    DOUBLESTAREQUAL,  // Операция **=

    LEFTSHIFT,       // Операция <<
    RIGHTSHIFT,      // Операция >>
    LEFTSHIFTEQUAL,  // Операция <<=
    RIGHTSHIFTEQUAL, // Операция >>=

    LESS,         // Оператор <
    GREATER,      // Оператор >
    LESSEQUAL,    // Оператор <=
    GREATEREQUAL, // Оператор >=
    EQUAL,        // Оператор ==
    NOTEQUAL,     // Оператор !=

    DOT,    // Оператор .
    COMMA,  // Оператор ,
    LPAR,   // Скобка (
    RPAR,   // Скобка )
    LSQB,   // Фигурная скобка {
    RSQB,   // Фигурная скобка }
    LBRACE, // Квадратная скобка [
    RBRACE, // Квадратная скобка ]

    NUMBER,     // Чисельный тип
    STRING,     // Строчный тип

    ENDMARKER,  //
    NAME,       //
    COLON,      //
    SEMI,       //
    VBAR,       //
    AMPER,      //
    BACKQUOTE,  //
    EQEQUAL,    //
    TILDE,        //
    CIRCUMFLEX,   //
    AMPEREQUAL,   //
    VBAREQUAL,    //
    CIRCUMFLEXEQUAL,  //
    AT,               //
    OP,               //
    ERRORTOKEN,       //
    N_TOKENS,         //
    NT_OFFSET;        //
}
