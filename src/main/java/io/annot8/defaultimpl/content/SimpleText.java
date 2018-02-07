package io.annot8.defaultimpl.content;

import io.annot8.common.content.Text;
import io.annot8.common.stores.SaveFromBuilder;
import io.annot8.core.data.Content;
import io.annot8.core.data.Item;
import io.annot8.core.data.Tags;
import io.annot8.core.properties.ImmutableProperties;
import io.annot8.core.stores.AnnotationStore;
import io.annot8.defaultimpl.data.AbstractSimpleContent;

public class SimpleText extends AbstractSimpleContent<String> implements Text {

  private SimpleText(AnnotationStore annotations,
      String name, Tags tags,
      ImmutableProperties properties, String data) {
    super(annotations, name, tags, properties, data);
  }

  public static class Builder extends AbstractSimpleContent.Builder<String, SimpleText> {

    public Builder(SaveFromBuilder<SimpleText, SimpleText> saver) {
      super(saver);
    }

    @Override
    protected SimpleText create(AnnotationStore annotations, String name, Tags tags,
        ImmutableProperties properties, String data) {
      return new SimpleText(annotations, name, tags, properties, data);
    }
  }

  public static class BuilderFactory extends
      AbstractSimpleContent.BuilderFactory<String, SimpleText> {

    public BuilderFactory() {
      super(String.class, SimpleText.class);
    }

    @Override
    public Content.Builder<SimpleText, String> create(Item item,
        SaveFromBuilder<SimpleText, SimpleText> saver) {
      return new SimpleText.Builder(saver);
    }

  }

}