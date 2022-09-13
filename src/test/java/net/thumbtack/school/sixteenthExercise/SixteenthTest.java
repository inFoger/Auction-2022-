package net.thumbtack.school.sixteenthExercise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SixteenthTest {

    @Test
    void first() {
        assertEquals("Thread[main,5,main]", FirthSecondAndThird.first());
    }

    @Test
    void second() {
        assertEquals(2, FirthSecondAndThird.second());
    }

    @Test
    void third() {
        assertEquals(4, FirthSecondAndThird.third());
    }

    @Test
    void fourth() {
        Fourth.fourth();
    }

    @Test
    void fifth() {
        Fifth.fifth();
    }

    @Test
    void sixth() {
        Sixth.sixth();
    }

    @Test
    void seventh(){
        // бесконечный цикл, поэтому закомментировано. Работает
        // Seventh.seventh();
    }

    @Test
    void eighth() {
        Eighth.eighth();
    }

    @Test
    void tenth() {
        Tenth.tenth();
    }

    @Test
    void eleventh() {
        // бесконечный цикл, поэтому закомментировано. Работает
        //Eleventh.eleventh();
    }

    @Test
    void thirteenth() {
        Thirteenth.thirteenth();
    }

    @Test
    void fifteenth() {

    }
}