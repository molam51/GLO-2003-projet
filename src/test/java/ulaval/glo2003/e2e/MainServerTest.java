package ulaval.glo2003.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.Main;

public class MainServerTest {

    @AfterEach
    public void teardownAfterEach() {
        Main.shutdownServerNow();
    }

    @Test
    public void givenNoArgs_whenMain_thenShouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> Main.main(new String[0]));
    }
}
