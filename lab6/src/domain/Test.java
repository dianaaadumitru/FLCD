package domain;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private final Parser parser;

    public Test() {
        Grammar grammar = new Grammar("./res/data/g3.txt");
        this.parser = new Parser(grammar);
    }

    private void testClosure() {
        List<Pair<String, List<String>>> closureList = new ArrayList<>();

        closureList.add(new Pair<>("S", List.of("a", "A")));
        closureList.add(new Pair<>("A", List.of("b", "A")));

        var result = parser.closure(closureList);
        System.out.println(result);
    }

    public void runAll() {
        testClosure();
    }
}
