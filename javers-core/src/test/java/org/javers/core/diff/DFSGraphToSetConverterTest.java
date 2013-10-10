package org.javers.core.diff;

import org.javers.core.model.DummyUser;
import org.javers.core.model.DummyUserDetails;
import org.javers.model.mapping.BeanBasedEntityFactory;
import org.javers.model.mapping.EntityManager;
import org.javers.model.mapping.type.TypeMapper;
import org.javers.model.object.graph.ObjectGraphBuilder;
import org.javers.model.object.graph.ObjectNode;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

import static org.javers.test.builder.DummyUserBuilder.dummyUser;
import static org.javers.test.builder.TypeMapperTestBuilder.typeMapper;

public class DFSGraphToSetConverterTest {

    private DFSGraphToSetConverter converter;
    private ObjectGraphBuilder objectGraphBuilder;

    @BeforeMethod
    public void setUp() {
        converter = new DFSGraphToSetConverter();
        TypeMapper mapper = typeMapper().registerAllDummyTypes().build();
        EntityManager entityManager = new EntityManager(new BeanBasedEntityFactory(mapper));
        entityManager.registerEntity(DummyUser.class);
        entityManager.registerEntity(DummyUserDetails.class);
        entityManager.buildManagedClasses();
        objectGraphBuilder = new ObjectGraphBuilder(entityManager);
    }

    @Test
    public void shouldConvertGraphIntoSet() {
        // given:
        DummyUser user = dummyUser().withName("Mad Kaz").build();
        ObjectNode graph = objectGraphBuilder.build(user);

        // when:
        Set<ObjectNode> objectNodes = converter.convertFromGraph(graph);

        // then:
        Assert.assertEquals(1, objectNodes.size());
    }
}
