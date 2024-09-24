import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @org.junit.jupiter.api.Test
    void getGpa(){
        Student student = new Student();
        assertEquals(student.getGpa(), 0);
    }

    @org.junit.jupiter.api.Test
    void getName(){
        Student student = new Student();
        assertEquals(student.getGpa(), "");
    }
}