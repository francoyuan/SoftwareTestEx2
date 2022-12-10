import org.example.YuanLai;
import org.junit.Test;

import java.io.IOException;

public class MyTest {
    @Test
    public void test1() throws IOException {
        String inJavaFile = "testdata/ArrayList.java";
        String methodNameUnion = "remove";
        YuanLai yuanLai = new YuanLai();
        yuanLai.transaction(inJavaFile, methodNameUnion);
    }

    @Test
    public void test2() throws IOException {
        String inJavaFile = "testdata/ArrayList.java";
        String methodNameUnion = "toArray";
        YuanLai yuanLai =new YuanLai();
        yuanLai.transaction(inJavaFile,methodNameUnion);
    }

    @Test
    public void test3() throws IOException {
        String inJavaFile = "testdata/WhileExample.java";
        String methodNameUnion = "whileOne";
        YuanLai yuanLai =new YuanLai();
        yuanLai.transaction(inJavaFile,methodNameUnion);
    }

    @Test
    public void test4() throws IOException {
        String inJavaFile = "testdata/WhileExample.java";
        String methodNameUnion = "whileIfOne";
        YuanLai yuanLai =new YuanLai();
        yuanLai.transaction(inJavaFile,methodNameUnion);
    }

    @Test
    public void test5() throws IOException {
        String inJavaFile = "testdata/Nested.java";
        String methodNameUnion = "ifOne";
        YuanLai yuanLai =new YuanLai();
        yuanLai.transaction(inJavaFile,methodNameUnion);
    }

}
