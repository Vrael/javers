package org.javers.core.pico;

import org.javers.core.Javers;
import org.javers.core.MappingStyle;
import org.javers.core.diff.DiffFactory;
import org.javers.model.mapping.*;
import org.javers.model.mapping.type.TypeMapper;
import org.picocontainer.PicoContainer;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author bartosz walacik
 */
public class JaversContainerFactoryTest {

    @Test
    public void shouldCreateMultipleJaversContainers() {
        //when
        PicoContainer container1 = JaversContainerFactory.create(MappingStyle.BEAN);
        PicoContainer container2 = JaversContainerFactory.create(MappingStyle.BEAN);

        //then
        assertThat(container1).isNotSameAs(container2);
        assertThat(container1.getComponent(EntityManager.class))
      .isNotSameAs(container2.getComponent(EntityManager.class));
    }

    @Test
    public void shouldContainRequiredJaversBeans(){
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.BEAN);

        //then
        assertThat(container.getComponent(Javers.class)).isNotNull();
        assertThat(container.getComponent(EntityManager.class)).isNotNull();
        assertThat(container.getComponent(TypeMapper.class)).isNotNull();
        assertThat(container.getComponent(DiffFactory.class)).isNotNull();
    }

    @Test
    public void shouldContainSingletons() {
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.BEAN);

        //when
        Javers javers = container.getComponent(Javers.class);
        Javers javersSecondRef = container.getComponent(Javers.class);

        //then
        assertThat(javers).isSameAs(javersSecondRef);
    }

    @Test
    public void shouldContainFieldBasedPropertyScannerWhenFieldStyle(){
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.FIELD);

        //then
        assertThat(container.getComponent(PropertyScanner.class)).isInstanceOf(FieldBasedPropertyScanner.class);
    }

    @Test
    public void shouldContainBeanBasedPropertyScannerWhenBeanStyle(){
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.BEAN);

        //then
        assertThat(container.getComponent(PropertyScanner.class)).isInstanceOf(BeanBasedPropertyScanner.class);
    }


    @Test
    public void shouldNotContainFieldBasedPropertyScannerWhenBeanStyle(){
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.BEAN);

        //then
        assertThat(container.getComponent(FieldBasedPropertyScanner.class)).isNull();
    }

    @Test
    public void shouldNotContainBeanBasedPropertyScannerWhenFieldStyle(){
        //given
        PicoContainer container = JaversContainerFactory.create(MappingStyle.FIELD);

        //then
        assertThat(container.getComponent(BeanBasedPropertyScanner.class)).isNull();
    }
}
