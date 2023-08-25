package guru.springframework.repositories;

import guru.springframework.bootstrap.RecipeBootstrap;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ReactiveReposIT {
    @Autowired
    UnitOfMeasureReactiveRepository uomReacRepo;
    @Autowired
    CategoryReactiveRepository catReacRepo;
    @Autowired
    RecipeReactiveRepository recipeReacRepo;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
        recipeBootstrap.onApplicationEvent(null);
    }

    @Test
    public void testCounts() {
        assertEquals(Long.valueOf(8), uomReacRepo.count().block());
        assertEquals(Long.valueOf(4), catReacRepo.count().block());
        assertEquals(Long.valueOf(2), recipeReacRepo.count().block());
    }

    @Test
    public void testFindByDescription() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("addedUom");

        uomReacRepo.save(uom1).block(); //publisher must be consumed (block method) for save to occur

        assertNotNull(uomReacRepo.findByDescription("addedUom").block());
    }
}
