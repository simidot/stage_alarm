package com.example.stagealarm.show.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.query.SortDirection;

@AllArgsConstructor(staticName = "of")
@Getter
public class SortOption {
  private String name;
  private SortDirection sortDirection;

  public static List<SortOption> of(Sortable sortable) {
    if (sortable == null || sortable.getSort() == null) return null;
    List<SortOption> sortOptions = new ArrayList<>();
    for (int i = 0 ; i < sortable.getSort().length; i++ ) {
      sortOptions.add(SortOption.of(
          sortable.getSort()[i],
          SortDirection.interpret(sortable.getOrder()[i])
      ));
    }
    return sortOptions;
  }


}
