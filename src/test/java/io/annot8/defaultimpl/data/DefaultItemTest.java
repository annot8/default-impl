package io.annot8.defaultimpl.data;

import io.annot8.core.data.Item;
import io.annot8.defaultimpl.factories.DefaultContentBuilderFactoryRegistry;
import io.annot8.defaultimpl.factories.DefaultItemFactory;
import io.annot8.testing.tck.impl.AbstractItemTest;

public class DefaultItemTest extends AbstractItemTest {

    @Override
    protected Item getItem() {
        DefaultContentBuilderFactoryRegistry registry = new DefaultContentBuilderFactoryRegistry(true);
        return new DefaultItem(new DefaultItemFactory(registry), registry);
    }
}