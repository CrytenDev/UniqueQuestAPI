package de.cryten.tool.mysql.common.table.column;

/**
 * A collection of all suitable types.
 *
 */
public enum ColumnType {

    /**
     * A bit-value type. A number from 1 to 64.
     */
    BIT,

    /**
     * A very small integer. The signed range is -128 to 127. The unsigned range is 0 to 255.
     */
    TINYINT,

    /**
     * A small integer. The signed range is -32768 to 32767. The unsigned range is 0 to 65535.
     */
    SMALLINT,

    /**
     * A medium-sized integer. The signed range is -8388608 to 8388607. The unsigned range is 0 to 16777215.
     */
    MEDIUMINT,

    /**
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The unsigned range is 0 to 4294967295.
     */
    INT,

    /**
     * A large integer. The signed range is -9223372036854775808 to 9223372036854775807. The unsigned range is 0 to
     * 18446744073709551615.
     */
    BIGINT,

    /**
     * A small (single-precision) floating-point number. Permissible values are -3.402823466E+38 to -1.175494351E-38, 0,
     * and 1.175494351E-38 to 3.402823466E+38. These are the theoretical limits, based on the IEEE standard. The actual
     * range might be slightly smaller depending on your hardware or operating system.
     */
    FLOAT,

    /**
     * A normal-size (double-precision) floating-point number. Permissible values are -1.7976931348623157E+308 to
     * -2.2250738585072014E-308, 0, and 2.2250738585072014E-308 to 1.7976931348623157E+308. These are the theoretical
     * limits, based on the IEEE standard. The actual range might be slightly smaller depending on your hardware or
     * operating system.
     */
    DOUBLE,

    /**
     * These types are synonyms for <b>TINYINT(1)</b>. A value of zero is considered false.Nonzero values are considered
     * true.
     */
    BOOLEAN,

    /**
     * A date. The supported range is '1000-01-01' to '9999-12-31'. MySQL displays DATE values in 'YYYY-MM-DD' format,
     * but permits assignment of values to DATE columns using either strings or numbers.
     */
    DATE,

    /**
     * A date and time combination. The supported range is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999'.
     * MySQL displays DATETIME values in 'YYYY-MM-DD HH:MM:SS[.fraction]' format, but permits assignment of values to
     * DATETIME columns using either strings or numbers.
     */
    DATETIME,

    /**
     * A timestamp. The range is '1970-01-01 00:00:01.000000' UTC to '2038-01-19 03:14:07.999999' UTC. TIMESTAMP values
     * are stored as the number of seconds since the epoch ('1970-01-01 00:00:00' UTC). A TIMESTAMP cannot represent the
     * value '1970-01-01 00:00:00' because that is equivalent to 0 seconds from the epoch and the value 0 is reserved
     * for representing '0000-00-00 00:00:00', the ���zero��? TIMESTAMP value.
     */
    TIMESTAMP,

    /**
     * A time. The range is '-838:59:59.000000' to '838:59:59.000000'. MySQL displays TIME values in
     * 'HH:MM:SS[.fraction]' format, but permits assignment of values to TIME columns using either strings or numbers.
     */
    TIME,

    /**
     * A fixed-length string that is always right-padded with spaces to the specified length when stored. The range is 0
     * to 255.
     */
    CHAR,

    /**
     * A variable-length string. The range is 0 to 65,535. The effective maximum length of a VARCHAR is subject to the
     * maximum row size (65,535 bytes, which is shared among all columns) and the character set used. For example, utf8
     * characters can require up to three bytes per character, so a VARCHAR column that uses the utf8 character set can
     * be declared to be a maximum of 21,844 characters.
     */
    VARCHAR,

    /**
     * A TEXT column with a maximum length of 255 characters. The effective maximum length is less if the value
     * contains multibyte characters.
     */
    TINYTEXT,

    /**
     * A TEXT column with a maximum length of 65,535 characters. The effective maximum length is less if the
     * value contains multibyte characters.
     */
    TEXT,

    /**
     * A TEXT column with a maximum length of 16,777,215 characters. The effective maximum length is less if
     * the value contains multibyte characters.
     */
    MEDIUMTEXT,

    /**
     * A TEXT column with a maximum length of 4,294,967,295 or 4GB characters. The effective maximum length is
     * less if the value contains multibyte characters. The effective maximum length of LONGTEXT columns also depends on
     * the configured maximum packet size in the client/server protocol and available memory.
     */
    LONGTEXT,

    /**
     * An enumeration. A string object that can have only one value, chosen from the list of values 'value1', 'value2',
     * ..., NULL or the special '' error value. ENUM values are represented internally as integers.
     */
    ENUM;
}