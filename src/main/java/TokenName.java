
public enum TokenName {
    // Перечислены все токены.
    // Источник: https://docs.python.org/2/library/token.html
    // Описание: https://www.asmeurer.com/brown-water-python/tokens.html

    NEWLINE,    // Токен новой логической строки
    INDENT,     // Токен повышения уровня вложености
    DEDENT,     // Токен понижения уровня вложености
    ENDMARKER,  // Токен конца исходного кода
    ERRORTOKEN, // Нераспознаный токен (если считаный токен нераспознан - он становится ERRORTOKEN)

    NAME,   // Индетификатор
    NUMBER, // Чисельный тип
    STRING, // Строчный тип

    OP, // Токен оператора, используется в спец.режиме, когда не используеются токены под конуретный оператор
        // В нашем случае - бесполезный

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
    EQUAL,        // Оператор =
    EQEQUAL,      // Оператор ==
    NOTEQUAL,     // Оператор !=

    DOT,       // Оператор .
    COMMA,     // Оператор ,
    LPAR,      // Скобка (
    RPAR,      // Скобка )
    LSQB,      // Квадратная скобка [
    RSQB,      // Квадратная скобка ]
    LBRACE,    // Фигурная скобка {
    RBRACE,    // Фигурная скобка }
    BACKQUOTE, // Кавычка "
    COLON,     // Двоеточие
    SEMI,      // Точка с запятой

    AT,               // Походу ничего не значит - инфы ноль
    N_TOKENS,         // Показывает количество типов токенов - просто константа в библиотеке token.py
    NT_OFFSET;        // Показывает максимальное количество завершающих (terminal) токенов. Тоже просто константа библиотеки.
}