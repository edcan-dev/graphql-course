package com.course.graphql;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.course.graphql.generated.client.BooksByReleasedGraphQLQuery;
import com.course.graphql.generated.client.BooksGraphQLQuery;
import com.course.graphql.generated.client.BooksProjectionRoot;
import com.course.graphql.generated.types.Author;
import com.course.graphql.generated.types.Book;
import com.course.graphql.generated.types.ReleaseHistoryInput;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FakeBookDataResolverTests {

  @Autowired
  DgsQueryExecutor dgsQueryExecutor;
  @Autowired
  Faker faker;

  @Test
  void testBooksWrittenBy() {
    var graphQLQuery = new BooksGraphQLQuery.Builder().build();
    var projectionRoot = new BooksProjectionRoot<>()
            .title()
            .author()
              .name()
              .originCountry().getRoot()
            .released().
              year();

    var graphQlQueryRequest = new GraphQLQueryRequest(graphQLQuery, projectionRoot).serialize();

    List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
            graphQlQueryRequest,
            "data.books[*].title"
    );

    assertNotNull(titles);
    assertFalse(titles.isEmpty());

    List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            graphQlQueryRequest,
            "data.books[*].author",
            new TypeRef<List<Author>>() {
            }
    );

    assertNotNull(authors);
    assertFalse(authors.isEmpty());

    List<Integer> releasedYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            graphQlQueryRequest,
            "data.books[*].released.year",
            new TypeRef<List<Integer>>() {
            }
    );

    assertNotNull(releasedYears);
    assertFalse(releasedYears.isEmpty());

  }

  @Test
  void testBooksWrittenBy_givenAuthorName() {

    int expectedYear = faker.number().numberBetween(2019, 2021);
    boolean expectedPrintedEdition = faker.bool().bool();

    var releasedHistoryInput = ReleaseHistoryInput.newBuilder()
            .year(expectedYear)
            .printedEdition(expectedPrintedEdition)
            .build();

    var graphQLQuery = BooksByReleasedGraphQLQuery.newRequest()
            .releasedInput(releasedHistoryInput)
            .build();

    var projectionRoot = new BooksProjectionRoot()
            .released()
            .year()
            .printedEdition();

    var graphQLQueryRequest = new GraphQLQueryRequest(
            graphQLQuery, projectionRoot
    ).serialize();

    List<Integer> releasedYears = dgsQueryExecutor.executeAndExtractJsonPath(
            graphQLQueryRequest,
            "data.booksByReleased[*].released.year"
    );

    Set<Integer> uniqueReleaseYears = new HashSet<>(releasedYears);

    assertNotNull(uniqueReleaseYears);
    assertFalse(uniqueReleaseYears.isEmpty());

    List<Boolean> releasePrintedEditions = dgsQueryExecutor.executeAndExtractJsonPath(
            graphQLQueryRequest,
            "data.bookByReleased[*].released.printedEdition"
    );

    assertNotNull(releasePrintedEditions);
    assertFalse(releasePrintedEditions.isEmpty());

  }
}
