package io.annot8.defaultimpl.stores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.annot8.core.annotations.Annotation;
import io.annot8.core.annotations.Group;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.core.stores.GroupStore;
import io.annot8.test.TestItem;
import io.annot8.test.content.TestStringContent;
import org.junit.jupiter.api.Test;

public class SimpleGroupStoreTest {

  @Test
  public void testInMemoryAnnotationStore() throws IncompleteException {

    TestItem item = new TestItem();
    GroupStore store = new SimpleGroupStore(item);
    item.setGroups(store);
    TestStringContent content = item.save(new TestStringContent());
    Annotation a1 = content.getAnnotations().create().save();
    Annotation a2 = content.getAnnotations().create().save();

    assertEquals(0, store.getAll().count());

    Group g = store.getBuilder()
        .withType("TEST")
        .withAnnotation("source", a1)
        .withAnnotation("target", a2)
        .save();

    assertEquals(1, store.getAll().count());
    store.getAll().forEach(group -> assertEquals(g, group));
    assertEquals(g, store.getById(g.getId()).get());
    assertFalse(store.getById("TEST").isPresent());

    store.delete(g);
    assertEquals(0, store.getAll().count());
  }
}