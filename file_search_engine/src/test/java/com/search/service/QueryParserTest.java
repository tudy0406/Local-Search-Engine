package com.search.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {

    private final QueryParser parser = new QueryParser();

    @Test
    void shouldParseSimpleWords() {
        String result = parser.parse("hello world");

        assertEquals("(hello AND world)", result);
    }

    @Test
    void shouldHandleSingleWord() {
        String result = parser.parse("hello");

        assertEquals("hello", result);
    }

    @Test
    void shouldParseQualifiedWord() {
        String result = parser.parse("path:docs");

        assertEquals("path:docs", result);
    }

    @Test
    void shouldParseMultipleQualifiedWords() {
        String result = parser.parse("path:docs path:report");

        assertEquals("(path:docs AND path:report)", result);
    }

    @Test
    void shouldParseMixedQuery() {
        String result = parser.parse("hello path:docs");

        assertEquals("hello AND path:docs", result);
    }

    @Test
    void shouldNormalizeCase() {
        String result = parser.parse("HELLO Path:DOCS");

        assertEquals("hello AND path:docs", result);
    }

    @Test
    void shouldHandleExtraSpaces() {
        String result = parser.parse("   hello    world   ");

        assertEquals("(hello AND world)", result);
    }

    @Test
    void shouldRemoveSpecialCharacters() {
        String result = parser.parse("hello!!! world???");

        assertEquals("(hello AND world)", result);
    }

    @Test
    void shouldHandleQuotedPhrase() {
        String result = parser.parse("\"machine learning\"");

        assertEquals("(machine AND learning)", result);
    }

    @Test
    void shouldHandleQualifiedQuotedPhrase() {
        String result = parser.parse("path:\"my docs\"");

        assertEquals("(path:my AND path:docs)", result);
    }

    @Test
    void shouldReturnEmptyForBlankInput() {
        String result = parser.parse("   ");

        assertEquals("", result);
    }

    @Test
    void shouldHandleMixedComplexQuery() {
        String result = parser.parse("hello path:\"my docs\" content:java");

        assertEquals("hello AND (path:my AND path:docs) AND content:java", result);
    }
}
