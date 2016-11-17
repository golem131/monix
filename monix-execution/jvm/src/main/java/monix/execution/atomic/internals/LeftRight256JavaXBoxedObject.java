/*
 * Copyright (c) 2014-2016 by its authors. Some rights reserved.
 * See the project homepage at: https://monix.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package monix.execution.atomic.internals;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

final class LeftRight256JavaXBoxedObject extends LeftRight256JavaXBoxedObjectImpl {
  public volatile long r1, r2, r3, r4, r5, r6, r7, r8 = 7;
  public volatile long r9, r10, r11, r12, r13, r14, r15 = 7;
  public long sum() {
    return p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 +
      p9 + p10 + p11 + p12 + p13 + p14 + p15 +
      r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 +
      r9 + r10 + r11 + r12 + r13 + r14 + r15;
  }

  LeftRight256JavaXBoxedObject(Object initialValue) {
    super(initialValue);
  }
}

abstract class LeftRight256JavaXBoxedObjectImpl extends LeftPadding120
  implements monix.execution.atomic.internals.BoxedObject {

  public volatile Object value;

  private static final AtomicReferenceFieldUpdater<LeftRight256JavaXBoxedObjectImpl, Object> UPDATER =
    AtomicReferenceFieldUpdater.newUpdater(LeftRight256JavaXBoxedObjectImpl.class, Object.class, "value");

  LeftRight256JavaXBoxedObjectImpl(Object initialValue) {
    this.value = initialValue;
  }

  public Object volatileGet() {
    return value;
  }

  public void volatileSet(Object update) {
    value = update;
  }

  public void lazySet(Object update) {
    UPDATER.lazySet(this, update);
  }

  public boolean compareAndSet(Object current, Object update) {
    return UPDATER.compareAndSet(this, current, update);
  }

  public Object getAndSet(Object update) {
    return UPDATER.getAndSet(this, update);
  }
}