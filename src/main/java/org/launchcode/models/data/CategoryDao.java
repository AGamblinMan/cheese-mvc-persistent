package org.launchcode.models.data;

import org.launchcode.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Adam on 4/6/2017.
 */
@Repository
public interface CategoryDao extends CrudRepository<Category, Integer>{
}
