package com.course.graphql;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FakeBookDataResolverTests {

  @Autowired
  DgsQueryExecutor dgsQueryExecutor;

  @Test
  void testBooksWrittenBy() {

  }

}
