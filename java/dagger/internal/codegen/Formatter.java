/*
 * Copyright (C) 2014 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import static com.google.common.base.Preconditions.checkElementIndex;
import static dagger.internal.codegen.ErrorMessages.INDENT;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * A formatter which transforms an instance of a particular type into a string
 * representation.
 *
 * @param <T> the type of the object to be transformed.
 * @author Christian Gruber
 * @since 2.0
 */
abstract class Formatter<T> implements Function<T, String> {

  /**
   * Performs the transformation of an object into a string representation.
   */
  public abstract String format(T object);

  /**
   * Performs the transformation of an object into a string representation in conformity with the
   * {@link Function}{@code <T, String>} contract, delegating to {@link #format(Object)}.
   *
   * @deprecated Call {@link #format(Object)} instead. This method exists to make formatters easy to
   *     use when functions are required, but shouldn't be called directly.
   */
  @SuppressWarnings("javadoc")
  @Deprecated
  @Override
  public final String apply(T object) {
    return format(object);
  }

  /**
   * Formats {@code items}, one per line. Stops after {@code limit} items.
   */
  public void formatIndentedList(
      StringBuilder builder, Iterable<? extends T> items, int indentLevel, int limit) {
    formatIndentedList(
        builder, indentLevel, Iterables.limit(items, limit), Iterables.skip(items, limit));
  }

  private void formatIndentedList(
      StringBuilder builder,
      int indentLevel,
      Iterable<? extends T> firstItems,
      Iterable<? extends T> restOfItems) {
    for (T item : firstItems) {
      builder.append('\n');
      appendIndent(builder, indentLevel);
      builder.append(format(item));
    }
    int numberOfOtherItems = Iterables.size(restOfItems);
    if (numberOfOtherItems > 0) {
      builder.append('\n');
      appendIndent(builder, indentLevel);
      builder.append("and ").append(numberOfOtherItems).append(" other");
    }
    if (numberOfOtherItems > 1) {
      builder.append('s');
    }
  }

  private void appendIndent(StringBuilder builder, int indentLevel) {
    for (int i = 0; i < indentLevel; i++) {
      builder.append(INDENT);
    }
  }

  protected String formatArgumentInList(int index, int size, CharSequence name) {
    checkElementIndex(index, size);
    StringBuilder builder = new StringBuilder();
    if (index > 0) {
      builder.append("…, ");
    }
    builder.append(name);
    if (index < size - 1) {
      builder.append(", …");
    }
    return builder.toString();
  }
}
