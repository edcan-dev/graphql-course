package com.course.graphql.component.fake;


import com.course.graphql.datasource.fake.FakePetDataSource;
import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.DgsConstants.QUERY;
import com.course.graphql.generated.types.Cat;
import com.course.graphql.generated.types.Dog;
import com.course.graphql.generated.types.Pet;
import com.course.graphql.generated.types.PetFilter;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

@DgsComponent
public class FakePetsDataResolver {

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = QUERY.Pets
    )
    public List<Pet> getPets(
            @InputArgument(name = "petFilter", collectionType = PetFilter.class) Optional<PetFilter> petFilter
    ) {

        if(petFilter.isEmpty()) {
            return FakePetDataSource.PET_LIST;
        }

        return FakePetDataSource.PET_LIST.stream().filter(
                pet -> this.matchFilter(petFilter.get(), pet)
        ).toList();

    }

    private boolean matchFilter(PetFilter petFilter, Pet pet) {

        if(StringUtils.isBlank(petFilter.getPetType())) {
            return true;
        }

        if(petFilter.getPetType().equalsIgnoreCase(Dog.class.getSimpleName())) {
            return pet instanceof Dog;
        } else if(petFilter.getPetType().equalsIgnoreCase(Cat.class.getSimpleName())) {
            return pet instanceof Cat;
        } else return true;
    }

}
