package org.launchcode.models.data;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Adam on 4/10/2017.
 */
@Repository
@Transactional
public interface MenuDao extends CrudRepository <Menu, Integer>{
    List<Menu> findByCheeses_Id(int cheeseId);
}
