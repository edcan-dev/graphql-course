package com.course.graphql.component.fake;

import com.course.graphql.datasource.fake.FakeHelloDataSource;
import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.DgsConstants.MUTATION;
import com.course.graphql.generated.types.Hello;
import com.course.graphql.generated.types.HelloInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;

@DgsComponent
public class FakeHelloMutation {

    /*@DgsData(
            parentType = MUTATION.TYPE_NAME,
            field = MUTATION.AddHello
    )*/
    @DgsMutation
    public int addHello(
            @InputArgument(name = "helloInput")HelloInput helloInput
    ) {

        Hello hello = Hello.newBuilder()
                .text(helloInput.getText())
                .randomNumber(helloInput.getNumber())
                .build();
        FakeHelloDataSource.HELLO_LIST.add(hello);

        return FakeHelloDataSource.HELLO_LIST.size();

    }

    @DgsMutation
    public List<Hello> replaceHelloText(
            @InputArgument(name = "helloInput")HelloInput helloInput
    ) {
        FakeHelloDataSource.HELLO_LIST.stream().filter(
                hello -> hello.getRandomNumber() == helloInput.getNumber()
        ).forEach(hello -> hello.setText(helloInput.getText()));
        return FakeHelloDataSource.HELLO_LIST;
    }

    @DgsMutation
    public int deleteHello(
            int number
    ) {
        FakeHelloDataSource.HELLO_LIST.removeIf(hello -> hello.getRandomNumber() == number);
        return FakeHelloDataSource.HELLO_LIST.size();
    }

}
