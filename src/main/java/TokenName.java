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

    TILDE,           // Операция ~
    AMPER,           // Операция &
    AMPEREQUAL,      // Операция &=
    VBAR,            // Операция |
    VBAREQUAL,       // Операция |=
    CIRCUMFLEX,      // Операция ^
    CIRCUMFLEXEQUAL, // Операция ^=

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

    DOT,       // Оператор .
    COMMA,     // Оператор ,
    LPAR,      // Скобка (
    RPAR,      // Скобка )
    LSQB,      // Фигурная скобка {
    RSQB,      // Фигурная скобка }
    LBRACE,    // Квадратная скобка [
    RBRACE,    // Квадратная скобка ]
    BACKQUOTE, // Кавычка "

    NAME,   // Индетификатор
    NUMBER, // Чисельный тип
    STRING, // Строчный тип

    // Ваще хз что это
    ENDMARKER,  //
    COLON,      //
    SEMI,       //
    EQEQUAL,    //
    AT,               //
    OP,               //
    ERRORTOKEN,       //
    N_TOKENS,         //
    NT_OFFSET;        //
}
