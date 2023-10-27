import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import uob.oop.NLP;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Tester_NLP {

    @Test
    @Order(1)
    void textCleaning(){
        String strTest = "!\"$%&^%H).,e+ll~'/o/Wor.l,d!";
        assertEquals("helloworld", NLP.textCleaning(strTest));
    }

    @Test
    @Order(2)
    void textLemmatization(){
        String strTest = "apples playing helped bananas test";
        assertEquals("appl play help banana test", NLP.textLemmatization(strTest));
    }

    //my tests
    @Test
    @Order(3)
    void removeStopWords() {
        String strTest = "i went to the shop in tamworth and it is really great";
        String[] stopTest = new String[] {"and", "the", "is", "in"};
        assertEquals("i went to shop tamworth it really great", NLP.removeStopWords(strTest, stopTest));
    }
}
