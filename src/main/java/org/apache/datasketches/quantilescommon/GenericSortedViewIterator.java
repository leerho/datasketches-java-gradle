/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.datasketches.quantilescommon;

import static org.apache.datasketches.quantilescommon.QuantileSearchCriteria.INCLUSIVE;

/**
 * Iterator over quantile sketches of generic type.
 * @param <T> The generic item class type
 */
public class GenericSortedViewIterator<T> extends SortedViewIterator {
  private final T[] quantiles;

  /**
   * Constructor
   * @param quantiles the given array of quantiles
   * @param cumWeights the array of cumulative weights, corresponding to the array of quantiles,
   * starting with the value one and the end value must equal N, the total number of items input to the sketch.
   */
  public GenericSortedViewIterator(final T[] quantiles, final long[] cumWeights) {
    super(cumWeights);
    this.quantiles = quantiles; //SpotBugs EI_EXPOSE_REP2 suppressed by FindBugsExcludeFilter
  }

  /**
   * Gets the quantile at the current index
   * This is equivalent to <i>getQuantile(INCLUSIVE)</i>.
   *
   * <p>Don't call this before calling next() for the first time or after getting false from next().</p>
   *
   * @return the quantile at the current index.
   */
  public T getQuantile() {
    return quantiles[index];
  }

  /**
   * Gets the quantile at the current index (or previous index)
   * based on the chosen search criterion.
   *
   * <p>Don't call this before calling next() for the first time or after getting false from next().</p>
   *
   * @param searchCrit if INCLUSIVE, includes the quantile at the current index.
   * Otherwise, returns the quantile of the previous index.
   *
   * @return the quantile at the current index (or previous index)
   * based on the chosen search criterion. If the chosen search criterion is <i>EXCLUSIVE</i> and
   * the current index is at zero, this will return <i>null</i>.
   */
  public T getQuantile(final QuantileSearchCriteria searchCrit) {
    if (searchCrit == INCLUSIVE) { return quantiles[index]; }
    return (index == 0) ? null : quantiles[index - 1];
  }
}
