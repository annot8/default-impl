/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.defaultimpl.factories;

import java.util.Objects;

import io.annot8.common.implementations.factories.ItemCreator;
import io.annot8.common.implementations.registries.ContentBuilderFactoryRegistry;
import io.annot8.core.data.Item;
import io.annot8.core.data.ItemFactory;
import io.annot8.defaultimpl.data.DefaultItem;

public class DefaultItemCreator implements ItemCreator {

  private final ContentBuilderFactoryRegistry contentBuilderFactoryRegistry;

  public DefaultItemCreator(ContentBuilderFactoryRegistry contentBuilderFactoryRegistry) {
    this.contentBuilderFactoryRegistry = contentBuilderFactoryRegistry;
  }

  @Override
  public Item create(ItemFactory factory) {
    return new DefaultItem(factory, contentBuilderFactoryRegistry);
  }

  @Override
  public Item create(ItemFactory factory, Item parent) {
    Objects.requireNonNull(parent);
    return new DefaultItem(factory, parent.getId(), contentBuilderFactoryRegistry);
  }
}