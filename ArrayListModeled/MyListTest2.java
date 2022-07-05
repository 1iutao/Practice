import java.util.Arrays;
import java.util.List;

public class MyListTest2 {
    public static void main(String[] args) {
        List list = new MyArrayList();

        list.add("h");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");

        List list2 = Arrays.asList("e", "l", "l", "z");

        //list.removeAll(list2);
        list.retainAll(list2);

        System.out.println(list);
    }
}
