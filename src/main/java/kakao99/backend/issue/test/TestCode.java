package kakao99.backend.issue.test;


import io.netty.util.internal.StringUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;

public class TestCode {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void 컬렉션_예외_테스트_ExpectedException(){

        // then
        expectedException.expect(ConcurrentModificationException.class);

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


}
