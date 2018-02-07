package io.annot8.defaultimpl.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.annot8.core.exceptions.IncompleteException;
import io.annot8.core.properties.ImmutableProperties;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SimpleImmutablePropertiesTest {

  @Test
  public void testImmutableProperties() throws IncompleteException {
    ImmutableProperties props1 = new SimpleImmutableProperties.Builder()
        .withProperty("key1", "Hello World")
        .withProperty("key2", 17)
        .save();
    testMap(props1.getAll());

    ImmutableProperties props2 = new SimpleImmutableProperties.Builder()
        .withProperty("key3",
            false)    //This will be removed as from(...) creates an exact copy of the parameter
        .from(props1)
        .save();
    testMap(props2.getAll());

    ImmutableProperties props3 = new SimpleImmutableProperties.Builder()
        .withProperty("key3",
            false)    //This won't be removed, as withProperties(...) adds to existing properties
        .withProperty("key1", "To be overwritten")
        .withProperties(props1)
        .save();

    Map<String, Object> map3 = props3.getAll();
    assertEquals(3, map3.size());
    assertEquals("Hello World", map3.get("key1"));
    assertEquals(17, map3.get("key2"));
    assertEquals(false, map3.get("key3"));
  }

  private void testMap(Map<String, Object> map) {
    assertEquals(2, map.size());
    assertEquals("Hello World", map.get("key1"));
    assertEquals(17, map.get("key2"));
  }
}
