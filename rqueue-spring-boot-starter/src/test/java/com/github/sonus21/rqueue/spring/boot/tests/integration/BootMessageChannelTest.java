/*
 * Copyright 2020 Sonu Kumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sonus21.rqueue.spring.boot.tests.integration;

import com.github.sonus21.rqueue.core.RqueueMessage;
import com.github.sonus21.rqueue.exception.TimedOutException;
import com.github.sonus21.rqueue.spring.boot.application.ApplicationListenerDisabled;
import com.github.sonus21.rqueue.test.tests.MessageChannelTest;
import com.github.sonus21.test.RqueueSpringTestRunner;
import com.github.sonus21.test.RunTestUntilFail;
import java.util.List;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@RunWith(RqueueSpringTestRunner.class)
@ContextConfiguration(classes = ApplicationListenerDisabled.class)
@TestPropertySource(
    properties = {
      "rqueue.scheduler.auto.start=false",
      "spring.redis.port=8002",
      "mysql.db.name=test2",
      "max.workers.count=120",
      "use.system.redis=false"

    })
@SpringBootTest
@Slf4j
public class BootMessageChannelTest extends MessageChannelTest {

  @Rule
  public RunTestUntilFail retry =
      new RunTestUntilFail(
          log,
          () -> {
            for (Entry<String, List<RqueueMessage>> entry : getMessageMap(jobQueue).entrySet()) {
              log.error("FAILING Queue {}", entry.getKey());
              for (RqueueMessage message : entry.getValue()) {
                log.error("FAILING Queue {} Msg {}", entry.getKey(), message);
              }
            }
          });

  @Test
  public void publishMessageIsTriggeredOnMessageAddition() throws TimedOutException {
    verifyPublishMessageIsTriggeredOnMessageAddition();
  }

  @Test
  public void publishMessageIsTriggeredOnMessageRemoval() throws TimedOutException {
    verifyPublishMessageIsTriggeredOnMessageRemoval();
  }
}
