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

package org.apache.datasketches.theta;

import static org.apache.datasketches.Util.DEFAULT_UPDATE_SEED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.apache.datasketches.memory.DefaultMemoryRequestServer;
import org.apache.datasketches.memory.Memory;
import org.apache.datasketches.memory.MemoryRequestServer;
import org.apache.datasketches.memory.WritableMemory;
import org.apache.datasketches.tuple.Util;
import org.testng.annotations.Test;

import jdk.incubator.foreign.ResourceScope;

public class HeapifyWrapSerVer1and2Test {
  private static final short defaultSeedHash = Util.computeSeedHash(DEFAULT_UPDATE_SEED);
  private static MemoryRequestServer memReqSvr = new DefaultMemoryRequestServer();
  //private static ResourceScope scope = ResourceScope.newImplicitScope();

  @Test
  public void checkHeapifyCompactSketchAssumedDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = Sketches.heapifyCompactSketch(sv3cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = Sketches.heapifyCompactSketch(sv2cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = Sketches.heapifyCompactSketch(sv1cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkHeapifyCompactSketchAssumedDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = Sketches.heapifyCompactSketch(sv3cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = Sketches.heapifyCompactSketch(sv2cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = Sketches.heapifyCompactSketch(sv1cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), defaultSeedHash);
  }

  @Test
  public void checkHeapifyCompactSketchGivenDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = Sketches.heapifyCompactSketch(sv3cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = Sketches.heapifyCompactSketch(sv2cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = Sketches.heapifyCompactSketch(sv1cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkHeapifyCompactSketchGivenDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = Sketches.heapifyCompactSketch(sv3cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = Sketches.heapifyCompactSketch(sv2cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = Sketches.heapifyCompactSketch(sv1cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkHeapifySketchAssumedDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv3cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv2cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv1cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkHeapifySketchAssumedDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv3cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv2cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv1cskMem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), defaultSeedHash);
  }

  @Test
  public void checkHeapifySketchGivenDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv3cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv2cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv1cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkHeapifySketchGivenDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }

    CompactSketch sv3csk = sv3usk.compact();
    Memory sv3cskMem = Memory.wrap(sv3csk.toByteArray());
    CompactSketch sv3cskResult;

    //SV3 test
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv3cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV2 test
    Memory sv2cskMem = BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv2cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);

    //SV1 test
    Memory sv1cskMem = BackwardConversions.convertSerVer3toSerVer1(sv3csk);
    sv3cskResult = (CompactSketch) Sketches.heapifySketch(sv1cskMem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
  }

  @Test
  public void checkWrapCompactSketchAssumedDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    CompactSketch sv3csk = sv3usk.compact();
    WritableMemory wmem;

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapCompactSketchAssumedDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = Sketches.wrapCompactSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), defaultSeedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapCompactSketchGivenDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {/* ignore */}
  }

  @Test
  public void checkWrapCompactSketchGivenDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = Sketches.wrapCompactSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapSketchAssumedDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapSketchAssumedDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), defaultSeedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapSketchGivenDefaultSeed() {
    final int k = 64;
    final long seed = DEFAULT_UPDATE_SEED;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  @Test
  public void checkWrapSketchGivenDifferentSeed() {
    final int k = 64;
    final long seed = 128L;
    final short seedHash = Util.computeSeedHash(seed);
    UpdateSketch sv3usk = UpdateSketch.builder().setNominalEntries(k).setSeed(seed).build();
    for (int i=0; i<k; i++) { sv3usk.update(i); }
    CompactSketch sv3cskResult;
    WritableMemory wmem;
    CompactSketch sv3csk = sv3usk.compact();

    //SV3 test
    wmem = putOffHeap(Memory.wrap(sv3csk.toByteArray()));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertTrue(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV2 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer2(sv3csk, seed));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}

    //SV1 test
    wmem = putOffHeap(BackwardConversions.convertSerVer3toSerVer1(sv3csk));
    sv3cskResult = (CompactSketch) Sketches.wrapSketch(wmem, seed);
    assertEquals(sv3cskResult.getEstimate(), sv3usk.getEstimate());
    assertEquals(sv3cskResult.getSeedHash(), seedHash);
    assertFalse(sv3cskResult.isDirect());
    try { wmem.close(); } catch (Exception e) {}
  }

  private static WritableMemory putOffHeap(Memory heapMem) {
    final long cap = heapMem.getCapacity();
    ResourceScope scope = ResourceScope.newConfinedScope();
    WritableMemory wmem = WritableMemory.allocateDirect(cap, scope, memReqSvr);
    heapMem.copyTo(0, wmem, 0, cap);
    return wmem;
  }

  @Test
  public void printlnTest() {
    println("PRINTING: "+this.getClass().getName());
  }

  /**
   * @param s value to print
   */
  static void println(String s) {
    //System.out.println(s); //disable here
  }
}

