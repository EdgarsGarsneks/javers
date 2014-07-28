package org.javers.core.diff.appenders

import org.javers.core.diff.AbstractDiffTest;
import org.javers.core.model.SnapshotEntity
import spock.lang.Ignore
import spock.lang.Unroll;

import static org.javers.core.metamodel.object.InstanceId.InstanceIdDTO.instanceId;

import static org.javers.core.diff.appenders.ContainerChangeAssert.getAssertThat

public class SetReferenceChangeAppenderTest extends AbstractDiffTest {

    def "should append ReferenceAdded in Set of Entities"() {
        given:
        def leftCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 2)])
        def rightCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 2), new SnapshotEntity(id: 5)])

        when:
        def change = setChangeAppender()
                .calculateChanges(realNodePair(leftCdo, rightCdo), getProperty(SnapshotEntity, "setOfEntities"))

        then:
        assertThat(change)
                .hasSize(1)
                .hasReferenceAdded(1, instanceId(5, SnapshotEntity))
    }

    def "should append ReferenceRemoved in Set of Entities"() {
        given:
        def leftCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 2), new SnapshotEntity(id: 5)])
        def rightCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 2)])

        when:
        def change = setChangeAppender()
                .calculateChanges(realNodePair(leftCdo, rightCdo), getProperty(SnapshotEntity, "setOfEntities"))

        then:
        assertThat(change)
                .hasSize(1)
                .hasReferenceRemoved(1, instanceId(5, SnapshotEntity))
    }

    def "should NOT append ElementReferenceChange in Set of Entities"() {
        given:
        def leftCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 2), new SnapshotEntity(id: 3)])
        def rightCdo = new SnapshotEntity(id: 1, setOfEntities: [new SnapshotEntity(id: 3), new SnapshotEntity(id: 2)])

        when:
        def change = setChangeAppender()
                .calculateChanges(realNodePair(leftCdo, rightCdo), getProperty(SnapshotEntity, "setOfEntities"))

        then:
        !change
    }
}


