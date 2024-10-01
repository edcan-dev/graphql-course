package com.course.graphql.component.fake;

import ch.qos.logback.core.util.StringUtil;
import com.course.graphql.datasource.fake.FakeBookDataSource;
import com.course.graphql.datasource.fake.FakeHelloDataSource;
import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.Book;
import com.course.graphql.generated.types.Hello;
import com.course.graphql.generated.types.ReleaseHistory;
import com.course.graphql.generated.types.ReleaseHistoryInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@DgsComponent
public class FakeBookDataResolver {

  @DgsData(parentType = "Query", field = "books")
  public List<Book> booksWrittenBy(
    @InputArgument(name = "author") Optional<String> authorName
  ) {
    if(authorName.isEmpty() || authorName.get().isEmpty()) {
      return FakeBookDataSource.BOOK_LIST;
    }
    return FakeBookDataSource.BOOK_LIST.stream()
      .filter(book -> StringUtils.containsIgnoreCase(book.getAuthor().getName(), authorName.get()))
      .toList();
  }


  @DgsQuery(field = "booksByReleased")
  public List<Book> getBooksByReleased(DataFetchingEnvironment dataFetchingEnvironment) {

    var releaseMap = (Map<String,Object>) dataFetchingEnvironment.getArgument("releasedInput");
    var releasedInput = ReleaseHistoryInput.newBuilder()
      .printedEdition((boolean) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.PrintedEdition))
      .year((int) releaseMap.get(DgsConstants.RELEASEHISTORYINPUT.Year))
      .build();

    return FakeBookDataSource.BOOK_LIST.stream().filter(
      book -> this.matchReleaseHistory(releasedInput, book.getReleased())
    ).toList();

  }


  private boolean matchReleaseHistory(ReleaseHistoryInput input, ReleaseHistory element) {
    return input.getPrintedEdition().equals(element.getPrintedEdition())
      && input.getYear().equals(element.getYear());
  }

}
