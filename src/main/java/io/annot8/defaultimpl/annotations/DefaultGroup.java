/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.defaultimpl.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.annot8.common.implementations.annotations.AbstractGroup;
import io.annot8.common.implementations.properties.MapImmutableProperties;
import io.annot8.common.implementations.properties.MapMutableProperties;
import io.annot8.common.utils.properties.EmptyImmutableProperties;
import io.annot8.core.annotations.Annotation;
import io.annot8.core.annotations.Group;
import io.annot8.core.data.BaseItem;
import io.annot8.core.exceptions.IncompleteException;
import io.annot8.core.properties.ImmutableProperties;
import io.annot8.core.properties.MutableProperties;
import io.annot8.core.properties.Properties;
import io.annot8.core.references.AnnotationReference;
import io.annot8.defaultimpl.references.DefaultAnnotationReference;

/** Simple implementation of Group interface */
public class DefaultGroup extends AbstractGroup {

  private final BaseItem item;
  private final String id;
  private final String type;
  private final ImmutableProperties properties;

  // TODO: Better stored as (or as well as) a Guava Multimap ?
  private final Map<DefaultAnnotationReference, String> annotations;

  private DefaultGroup(
      final BaseItem item,
      final String id,
      final String type,
      final ImmutableProperties properties,
      final Map<DefaultAnnotationReference, String> annotations) {
    this.item = item;
    this.id = id;
    this.type = type;
    this.properties = properties;
    this.annotations = annotations;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public ImmutableProperties getProperties() {
    return properties;
  }

  @Override
  public Map<String, Stream<AnnotationReference>> getReferences() {
    Map<String, Stream<AnnotationReference>> ret = new HashMap<>();

    annotations.forEach(
        (key, value) -> {
          Stream<AnnotationReference> s = ret.getOrDefault(value, Stream.empty());
          ret.put(value, Stream.concat(s, Stream.of(key)));
        });

    return ret;
  }

  @Override
  public Optional<String> getRole(Annotation annotation) {
    return Optional.ofNullable(annotations.get(DefaultAnnotationReference.to(item, annotation)));
  }

  @Override
  public boolean containsAnnotation(Annotation annotation) {
    return annotations.containsKey(DefaultAnnotationReference.to(item, annotation));
  }

  /**
   * AbstractContentBuilder class for DefaultGroup, using UUID for generating new IDs and
   * InMemoryImmutableProperties or EmptyImmutableProperties for the properties.
   */
  public static class Builder implements Group.Builder {

    private final BaseItem item;

    private String id;
    private String type = null;
    private MutableProperties properties = new MapMutableProperties();
    private Map<Annotation, String> annotations = new HashMap<>();

    public Builder(BaseItem item) {
      this(item, null);
    }

    public Builder(BaseItem item, String id) {
      this.item = item;
      this.id = id;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder withId(String id) {
      this.id = id;
      return this;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder withType(String type) {
      this.type = type;
      return this;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder withProperty(String key, Object value) {
      properties.set(key, value);
      return this;
    }

    @Override
    public Group.Builder withPropertyIfPresent(String key, Optional<?> value) {
      value.ifPresent(o -> properties.set(key, o));
      return this;
    }

    @Override
    public Group.Builder withoutProperty(String key, Object value) {
      Optional<Object> val = properties.get(key);
      if (val.isPresent() && val.get().equals(value)) {
        properties.remove(key);
      }

      return this;
    }

    @Override
    public Group.Builder withoutProperty(String key) {
      properties.remove(key);
      return this;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder withProperties(Properties properties) {
      this.properties = new MapMutableProperties(properties);
      return this;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder newId() {
      this.id = null;
      return this;
    }

    @Override
    public io.annot8.core.annotations.Group.Builder from(Group from) {
      this.id = from.getId();
      this.type = from.getType();
      this.properties = new MapMutableProperties(from.getProperties());

      this.annotations = new HashMap<>();
      from.getAnnotations()
          .forEach((key, value) -> value.forEach(a -> this.annotations.put(a, key)));

      return this;
    }

    @Override
    public Group save() throws IncompleteException {

      if (id == null) {
        id = UUID.randomUUID().toString();
      }

      if (type == null) {
        throw new IncompleteException("Type has not been set");
      }

      ImmutableProperties immutableProperties;
      if (properties.getAll().isEmpty()) {
        immutableProperties = EmptyImmutableProperties.getInstance();
      } else {
        immutableProperties = new MapImmutableProperties.Builder().from(properties).save();
      }

      Map<DefaultAnnotationReference, String> references =
          annotations
              .entrySet()
              .stream()
              .collect(
                  Collectors.toMap(
                      e -> DefaultAnnotationReference.to(item, e.getKey()), Entry::getValue));

      return new DefaultGroup(item, id, type, immutableProperties, references);
    }

    @Override
    public io.annot8.core.annotations.Group.Builder withAnnotation(
        String role, Annotation annotation) {
      annotations.put(annotation, role);

      return this;
    }
  }
}
