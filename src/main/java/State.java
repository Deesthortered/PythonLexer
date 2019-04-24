public enum State {

    StartState_EmptyString(0),
    StartState_NotEmptyString(1),
    Comment1(2),
    Comment2(9),
    ExtendString1(3),
    ExtendString2(4),
    NestingDepth(5),

    WaitingEqual(6),
    NeedEqual(7),
    Multiply1(8),
    Slash1(10),
    Greater1(12),
    Less1(14),
    WaitingEqual2(15),

    DotIsFirst(16),
    DotIsNotFirst(17),
    DecimalNumber(18),
    Number2x8x16x(19),
    BinInt(20),
    OctInt(21),
    HexInt(22),
    NextBinDigit(23),
    NextOctDigit(24),
    NextHexDigit(25),
    FractionDigit(26),
    ExponentSymbol(27),
    SignSymbol(29),
    ExponentDigit(30),
    ImaginaryLong(31),

    DoubleQuote1(32),
    DoubleQuote2(33),
    DoubleQuote3(34),
    MultiQuoteTwoOne(35),

    OneQuote1(36),
    OneQuote2(37),
    OneQuote3(38),
    MultiQuoteOneTwo(39),

    FirstPrefixLetter(40),
    SecondPrefixLetter(41),

    IdentifierReading(42),

    StringLetterQuoteOne(43),

    StringLetterQuoteOneOneOne(44),
    FinishStringOneOneOne1(45),
    FinishStringOneOneOne2(46),

    StringLetterQuoteOneTwo(47),
    FinishStringOneTwo(48),

    StringLetterQuoteTwo(49),

    StringLetterQuoteTwoTwoTwo(50),
    FinishStringTwoTwoTwo1(51),
    FinishStringTwoTwoTwo2(52),

    StringLetterQuoteTwoOne(53),
    FinishStringTwoOne(54),

    EscapeSequence(55),
    Reading(56),

    UnicodeName(57),

    N1(58),
    E1(59),
    W(60),
    L(61),
    I(62),
    N2(63),

    _32_bitHex1(64),
    _32_bitHex2(65),
    _32_bitHex3(66),
    _32_bitHex4(67),
    _32_bitHex5(68),
    _32_bitHex6(69),
    _32_bitHex7(70),
    _32_bitHex8(71),

    _16_bitHex1(72),
    _16_bitHex2(73),
    _16_bitHex3(74),
    _16_bitHex4(75),

    HexValue1(76),
    HexValue2(77),
    HexValue3(78),

    OctValue1(79),
    OctValue2(80),
    OctValue3(81);

    private final int value;

    State(final int newValue) {
        value = newValue;
    }
    public int number() { return value; }
}
