package by.andd3dfx.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateTimeHelperTest {

    @Test
    public void convertInstantToLocalDateTime() {
        Instant instant = Instant.parse("2011-11-03T06:57:20.852Z");

        LocalDateTime localDateTime = DateTimeHelper.convertInstantToLocalDateTime(instant);

        assertThat(localDateTime.toString(), is("2011-11-03T06:57:20.852"));
    }

    @Test
    public void convertZStringToLocalDateTime() {
        LocalDateTime localDateTime = DateTimeHelper.convertZStringToLocalDateTime("2011-11-03T06:57:20.852Z");

        assertThat(localDateTime.toString(), is("2011-11-03T06:57:20.852"));
    }
}
