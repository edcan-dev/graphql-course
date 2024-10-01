package com.course.graphql;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeHelloDataResolverTest {

  @Autowired
  DgsQueryExecutor dgsQueryExecutor;

  @Test
  void testOneHello() {

    var graphQuery = """
          {
            oneHello {
              randomNumber
              text
            }
          }
      """;


    String text = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.oneHello.text");
    Integer randomNumber = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.oneHello.randomNumber");

    assertFalse(StringUtils.isBlank(text));
    assertNotNull(randomNumber);

  }

  @Test
  void testallHellos() {

    var graphQuery = """
          {
            allHellos {
              randomNumber
              text
            }
          }
      """;


    List<String> texts = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.allHellos[*].text");
    List<Integer> randomNumbers = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.allHellos[*].randomNumber");

    assertNotNull(texts);
    assertFalse(texts.isEmpty());
    assertNotNull(randomNumbers);
    assertFalse(randomNumbers.isEmpty());
    assertEquals(randomNumbers.size(), texts.size());

  }

}
