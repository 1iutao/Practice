import java.util.List;

public class MyArrayListTest {

    public static void main(String[] args) {
        List list = new MyArrayList();

        list.add("h");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");
//        System.out.println(list);

        list.clear();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");

        for (int i = 0; i < 100; i++) {
            list.add("hello");
        }

        System.out.println(list.size());
    }
}
