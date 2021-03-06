package ru.otus.l15;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.l15.DiyGson.NULL_TEXT;

import com.google.gson.Gson;
import examples.BagOfPrimitives;
import examples.ComplexObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DiyGsonTest.
 *
 * @author Evgeniya_Yanchenko
 */
public class DiyGsonTest {

    private BagOfPrimitives bagOfPrimitives;
    private ComplexObject complexObject;
    private Set<String> stringSet;
    private int[] intArray;
    private List<BagOfPrimitives> list;

    DiyGson diyGson;
    Gson gson;

    @BeforeEach
    void setUp() {
        bagOfPrimitives = new BagOfPrimitives(15, "text", 32);
        complexObject = new ComplexObject(45, "usual string", bagOfPrimitives,
                Map.of(1L, "first", 2L, "second"));
        stringSet = Set.of("one", "two", "three");
        intArray = new int[]{10, 20, 30, 40, 50};
        list = List.of(
                new BagOfPrimitives(bagOfPrimitives).setStringValue("first element"),
                new BagOfPrimitives(bagOfPrimitives).setStringValue("second element"),
                new BagOfPrimitives(bagOfPrimitives).setStringValue("third element")
        );

        diyGson = new DiyGson();
        gson = new Gson();
    }

    @Test
    void testBagOfPrimitiveSerialization() {
        String json = diyGson.toJson(bagOfPrimitives);
        BagOfPrimitives deserialized = gson.fromJson(json, BagOfPrimitives.class);
        assertThat(deserialized).isEqualTo(bagOfPrimitives);
    }

    @Test
    void testComplexObjectSerialization() {
        String json = diyGson.toJson(complexObject);
        ComplexObject deserialized = gson.fromJson(json, ComplexObject.class);
        assertThat(deserialized).isNotEqualTo(complexObject);

        deserialized.setTransientInt(complexObject.getTransientInt());
        assertThat(deserialized).isEqualTo(complexObject);
    }

    @Test
    void testSimpleSetSerialization() {
        String json = diyGson.toJson(stringSet);
        Set<String> deserialized = gson.fromJson(json, HashSet.class);
        assertThat(deserialized).isEqualTo(stringSet);
    }

    @Test
    void testSimpleArraySerialization() {
        String json = diyGson.toJson(intArray);
        int[] deserialized = gson.fromJson(json, int[].class);
        assertThat(deserialized).isEqualTo(intArray);
    }

    @Test
    void testListOfObjectsSerialization() {
        String json = diyGson.toJson(list);
        String gsonJson = gson.toJson(list);
        assertThat(json).isEqualTo(gsonJson);

        ArrayList<BagOfPrimitives> fromJson = gson.fromJson(json, ArrayList.class);
        ArrayList<BagOfPrimitives> fromGsonJson = gson.fromJson(gsonJson, ArrayList.class);
        assertThat(fromJson).isEqualTo(fromGsonJson);
    }

    @Test
    void testNull() {
        String json = diyGson.toJson(null);
        assertThat(json).isEqualTo(NULL_TEXT);
    }

    @Test
    public void test() {
        DiyGson serializer = new DiyGson();
        assertEquals(gson.toJson(null), serializer.toJson(null));
        assertEquals(gson.toJson((byte) 1), serializer.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), serializer.toJson((short) 1f));
        assertEquals(gson.toJson(1), serializer.toJson(1));
        assertEquals(gson.toJson(1L), serializer.toJson(1L));
        assertEquals(gson.toJson(1f), serializer.toJson(1f));
        assertEquals(gson.toJson(1d), serializer.toJson(1d));
        assertEquals(gson.toJson("aaa"), serializer.toJson("aaa"));
        assertEquals(gson.toJson('a'), serializer.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), serializer.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), serializer.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), serializer.toJson(Collections.singletonList(1)));
    }
}
