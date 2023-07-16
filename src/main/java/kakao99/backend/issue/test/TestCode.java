package kakao99.backend.issue.test;


import io.netty.util.internal.StringUtil;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

public class TestCode {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void 컬렉션_예외_테스트_ExpectedException(){

        // then
        expectedException.expect(ConcurrentModificationException.class);

        Matcher<String> nullMatcher = new IsNull<>();  // 예외 메세지가 null임을 테스트하는 코드
        expectedException.expectMessage(nullMatcher);

        // given
        ArrayList<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);

        // when
        for (Integer object : objects) {
            System.out.println("object = " + object);

            if (object.equals(1)) {
                int index = objects.indexOf(object);
                System.out.println("index = " + index);
                Integer remove = objects.remove(objects.indexOf(object));   // ConcurrentModificationException 발생
                System.out.println("remove = " + remove);
            }
        }
    }


    @Test
    public void 컬렉션_예외_테스트_assertThrows() {

        // given
        ArrayList<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);

        // then
        Exception exception = assertThrows(ConcurrentModificationException.class, ()->{
            // when
            for (Integer object : objects) {
                System.out.println("object = " + object);

                if (object.equals(1)) {
                    int index = objects.indexOf(object);
                    System.out.println("index = " + index);
                    Integer remove = objects.remove(objects.indexOf(object));   // ConcurrentModificationException 발생
                    System.out.println("remove = " + remove);
                }
            }
        });

        /* THEN -> EXPECTED EXCEPTION MESSAGE */
//        assertThat(exception.getMessage(), containsString(null));
        assertThat(exception.getMessage(), Matchers.nullValue());   // 예외 메세지가 null임을 테스트하는 코드
    }


}
