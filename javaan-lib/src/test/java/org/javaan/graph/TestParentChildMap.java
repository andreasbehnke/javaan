package org.javaan.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

public class TestParentChildMap {

    @Test
    public void testInvers() {
        ParentChildMap map = new ParentChildMap();
        map.addChild("p1", "c1");
        map.addChild("p1", "c2");
        map.addChild("p2", "c1");
        map.addChild("p2", "c3");

        map = map.invers();
        assertNotNull(map);
        assertEquals(3, map.keySet().size());
        assertTrue(map.keySet().contains("c1"));
        assertTrue(map.keySet().contains("c2"));
        assertTrue(map.keySet().contains("c3"));
        List<String> values = map.get("c1");
        assertNotNull(values);
        assertEquals(2, values.size());
        assertTrue(values.contains("p1"));
        assertTrue(values.contains("p2"));
        values = map.get("c2");
        assertNotNull(values);
        assertEquals(1, values.size());
        assertTrue(values.contains("p1"));
        values = map.get("c3");
        assertNotNull(values);
        assertEquals(1, values.size());
        assertTrue(values.contains("p2"));
    }

}
