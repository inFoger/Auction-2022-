package net.thumbtack.school.fifteenthExercise;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LambdaTest {
    @Test
    void splitAndCountTest  () {
        String str = "C EE FFF GGGG AAAAA BBBBBB";
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "C", "EE", "FFF","GGGG", "AAAAA", "BBBBBB");
        assertEquals(list, Lambda.split.apply(str));
        assertEquals(6, Lambda.count.apply(list));
        assertEquals(6, Lambda.splitAndCount1.apply(str));
        assertEquals(6, Lambda.splitAndCount2.apply(str));

        assertEquals(list, MyLambda.split.apply(str));
        assertEquals(6, MyLambda.count.apply(list));
    }

    @Test
    void createTest() {
        assertEquals("Frodo", Lambda.create1.apply("Frodo").getName());
        assertEquals("Frodo", Lambda.create2.apply("Frodo").getName());
        assertEquals("Frodo", MyLambda.create2.apply("Frodo").getName());
    }

    @Test
    void maxTest() {
        assertEquals(3, Lambda.max.apply(1,3));
        assertEquals(-2, Lambda.max.apply(-5,-2));
    }

    @Test
    void getCurrentDateTest() {
        assertEquals(new Date().toString(), Lambda.getCurrentDate.get().toString());
    }

    @Test
    void isEvenTest() {
        assertTrue(Lambda.isEven.test(2));
        assertTrue(Lambda.isEven.test(-2));
        assertFalse(Lambda.isEven.test(1));
    }

    @Test
    void areEqualTest() {
        assertTrue(Lambda.areEqual.test(1,1));
        assertTrue(Lambda.areEqual.test(-1,-1));
        assertFalse(Lambda.areEqual.test(1,-1));
    }

}