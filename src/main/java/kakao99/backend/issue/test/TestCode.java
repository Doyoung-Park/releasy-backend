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

    @Test
    public void deleteElement() {

        List<String> list = Arrays.asList("A", "B", "C");

        Stream<String> stream = list.stream();

        stream.filter("A"::equals).forEach(System.out::println);


    }

    enum ExceptionCode{
        NOT_AUTHORIZED(5001, "접근 권한이 없음"),
        NOT_LOGGED_IN(5002, "로그인되지 않음"),
        PARAMETER_MISSED(5003, "필수 파라미터 누락됨");

        final private int statusCode;
        final private String message;

        public String getMessage() {
            return message;
        }

        private ExceptionCode(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
    }

    @Test
    public void Enum_Test() {

        System.out.println(ExceptionCode.NOT_AUTHORIZED);
        System.out.println(ExceptionCode.NOT_AUTHORIZED.getMessage());

        System.out.println(ExceptionCode.NOT_LOGGED_IN);
        System.out.println(ExceptionCode.NOT_LOGGED_IN.getMessage());

        System.out.println(ExceptionCode.PARAMETER_MISSED);
        System.out.println(ExceptionCode.PARAMETER_MISSED.getMessage());

    }
}
