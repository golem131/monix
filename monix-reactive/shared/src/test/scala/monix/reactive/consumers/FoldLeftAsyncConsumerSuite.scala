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
 */

package monix.reactive.consumers

import minitest.TestSuite
import monix.eval.Task
import monix.execution.schedulers.TestScheduler
import monix.reactive.exceptions.DummyException
import monix.reactive.{Consumer, Observable}
import scala.util.{Failure, Success}

object FoldLeftAsyncConsumerSuite extends TestSuite[TestScheduler] {
  def setup(): TestScheduler = TestScheduler()
  def tearDown(s: TestScheduler): Unit = {
    assert(s.state.tasks.isEmpty,
      "TestScheduler should have no pending tasks")
  }

  test("should sum a long stream") { implicit s =>
    val count = 10000L
    val obs = Observable.range(0, count)
    val f = obs.consumeWith(Consumer
      .foldLeftAsync(0L)((s,a) => Task(s+a)))
      .runAsync

    s.tick()
    assertEquals(f.value, Some(Success(count * (count - 1) / 2)))
  }

  test("should interrupt with error") { implicit s =>
    val ex = DummyException("dummy")
    val obs = Observable.range(0, 10000).endWithError(ex)
    val f = obs.consumeWith(Consumer
      .foldLeftAsync(0L)((s,a) => Task(s+a)))
      .runAsync

    s.tick()
    assertEquals(f.value, Some(Failure(ex)))
  }

  test("should protect against user simple error") { implicit s =>
    val ex = DummyException("dummy")
    val f = Observable.now(1)
      .consumeWith(Consumer.foldLeftAsync(0L)((s,a) => throw ex))
      .runAsync

    s.tick()
    assertEquals(f.value, Some(Failure(ex)))
  }

  test("should protect against user task error") { implicit s =>
    val ex = DummyException("dummy")
    val f = Observable.now(1)
      .consumeWith(Consumer.foldLeftAsync(0L)((s,a) => Task.raiseError(ex)))
      .runAsync

    s.tick()
    assertEquals(f.value, Some(Failure(ex)))
  }
}