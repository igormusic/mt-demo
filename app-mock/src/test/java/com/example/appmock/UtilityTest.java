package com.example.appmock;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    public void testGetRandomRecord() {
        var array = new String[] {"a", "b", "c"};
        var result = Utility.getRandomRecord((List.of(array)));
        assertNotNull(result);
        assertTrue(result.equals("a") || result.equals("b") || result.equals("c"));
    }

    @Test void test32BitMaxBase62() {
        var num = 4294967295L;
        var str = Utility.toBase62(num);
        var result = Utility.fromBase62(str);
        assertEquals(num, result);
    }
    @Test void testMinBase62() {
        var num = 0L;
        var str = Utility.toBase62(num);
        var result = Utility.fromBase62(str);
        assertEquals(num, result);
    }

    @Test void testMaxBase62() {

        LocalDateTime dateTime = LocalDateTime.of(2100,1,1,0,0);
        long millisSinceEpoch = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        var str = Utility.toBase62(millisSinceEpoch);
        var result = Utility.fromBase62(str);
        assertEquals(millisSinceEpoch, result);
    }

    @Test void testGenerateReferenceNumber() {
        var result = Utility.generateReferenceNumber();
        assertNotNull(result);
        assertTrue(result.length() == 16);
    }

}