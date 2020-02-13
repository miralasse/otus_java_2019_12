package ru.otus.l03;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * DIYArrayListTest.
 *
 * @author Evgeniya_Yanchenko
 */
public class DIYArrayListTest {

    private DIYArrayList<Integer> myArrayList;
    private ArrayList<Integer> benchmarkList;

    @BeforeEach
    public void setUp() {
        myArrayList = new DIYArrayList<>();
        benchmarkList = IntStream.range(0, 50).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    @Test
    public void testAddAll() {
        myArrayList.addAll(benchmarkList);
        assertThat(myArrayList).hasSize(benchmarkList.size());
        assertThat(myArrayList).containsExactly(benchmarkList.toArray(new Integer[0]));
    }

    @Test
    public void testCollectionsAddAll() {
        Collections.addAll(myArrayList, benchmarkList.toArray(new Integer[0]));
        assertThat(myArrayList).hasSize(benchmarkList.size());
        assertThat(myArrayList).containsExactly(benchmarkList.toArray(new Integer[0]));
    }

    @Test
    public void testCopyToMyArrayList() {
        myArrayList.addAll(Stream.generate(() -> 1).limit(benchmarkList.size()).collect(toList()));
        Collections.copy(myArrayList, benchmarkList);
        assertThat(myArrayList).hasSize(benchmarkList.size());
        assertThat(myArrayList).containsExactly(benchmarkList.toArray(new Integer[0]));
    }

    @Test
    public void testCopyFromMyArrayList() {
        myArrayList.addAll(Stream.generate(() -> 2).limit(benchmarkList.size()).collect(toList()));
        Collections.copy(benchmarkList, myArrayList);
        assertThat(benchmarkList).hasSize(myArrayList.size());
        assertThat(myArrayList).containsExactly(benchmarkList.toArray(new Integer[0]));
    }

    @Test
    public void testSort() {
        myArrayList.addAll(Stream.generate(() -> (int)(Math.random()*100))
                .limit(benchmarkList.size())
                .collect(toList()));
        Collections.copy(benchmarkList, myArrayList);
        Collections.sort(benchmarkList);
        Collections.sort(myArrayList);
        assertThat(myArrayList).containsExactly(benchmarkList.toArray(new Integer[0]));
    }



}
