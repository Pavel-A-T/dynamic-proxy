import java.util.ArrayList;
import java.util.List;

public class Example implements Examplable {

    @LogAnnotation
    public void calculate() throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                list.add("" + i + j);
                Thread.sleep(1);
            }
        }
    }


    public void doSome() {
        System.out.println("This is method doSome");
    }

}