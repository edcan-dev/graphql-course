package com.course.graphql.component.fake;

import com.course.graphql.datasource.fake.FakeBookDataSource;
import com.course.graphql.datasource.fake.FakeHelloDataSource;
import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.SmartSearchResult;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.apache.commons.lang3.StringUtils;

@DgsComponent
public class FakeSmartSearchResolver {


    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.SmartSearch
    )
    public List<SmartSearchResult> getSmartSearch(
            @InputArgument(name = "keyword") Optional<String> keyword
    ) {
        var smartSearchList = new ArrayList<SmartSearchResult>();
        if(keyword.isEmpty()) {
            smartSearchList.addAll(FakeHelloDataSource.HELLO_LIST);
            smartSearchList.addAll(FakeBookDataSource.BOOK_LIST);
        } else {

            FakeHelloDataSource.HELLO_LIST.stream().filter(
                    hello -> StringUtils.containsIgnoreCase(hello.getText(), keyword.get())
            ).forEach(smartSearchList::add);
            FakeBookDataSource.BOOK_LIST.stream().filter(
                    book -> StringUtils.containsIgnoreCase(book.getTitle(), keyword.get())
            ).forEach(smartSearchList::add);
        }
        return smartSearchList;
    }

}
