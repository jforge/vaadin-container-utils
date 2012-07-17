package org.shortbrain.vaadin.container.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.shortbrain.vaadin.container.AbstractContainerUtilsTest;

/**
 * Test class for {@link AttributeReaderAlgorithm}.
 * 
 * @author Vincent Demeester <vincent@demeester.fr>
 * 
 */
@SuppressWarnings("unused")
@RunWith(BlockJUnit4ClassRunner.class)
public class AttributeReaderAlgorithmTest extends AbstractContainerUtilsTest {

	@Test
	public void testGetPropertiesNull() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm();
		try {
			a.getProperties(null);
			fail("should throw a IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertEquals("beanClass cannot be null.", e.getMessage());
		}
	}

	@Test
	public void testGetPropertiesIgnoredNull() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(null);
		List<PropertyMetadata> metadatas = a.getProperties(SuperTestBean.class);
		assertNotNull(metadatas);
		assertEquals(1, metadatas.size());
		assertMetadata("date", Date.class, null, "date", metadatas.get(0));
	}

	@Test
	public void testGetPropertiesDefault() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm();
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(3, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
		assertMetadata("date", Date.class, null, "date", metadatas.get(2));
	}

	@Test
	public void testGetPropertiesWithSuper() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(true);
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(3, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
		assertMetadata("date", Date.class, null, "date", metadatas.get(2));
	}

	@Test
	public void testGetPropertiesWithoutSuper() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(false);
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(2, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
	}

	@Test
	public void testGetPropertiesWithIgnored() {
		AttributeReaderAlgorithm a1 = new AttributeReaderAlgorithm(
				Arrays.asList(new String[] { "date" }));
		List<PropertyMetadata> metadatas1 = a1.getProperties(TestBean.class);
		assertNotNull(metadatas1);
		assertEquals(2, metadatas1.size());
		assertMetadata("string", String.class, null, "string",
				metadatas1.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas1.get(1));
		AttributeReaderAlgorithm a2 = new AttributeReaderAlgorithm(
				Arrays.asList(new String[] { "string" }));
		List<PropertyMetadata> metadatas2 = a2.getProperties(TestBean.class);
		assertNotNull(metadatas2);
		assertEquals(2, metadatas2.size());
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas2.get(0));
		assertMetadata("date", Date.class, null, "date", metadatas2.get(1));
	}

	@Test
	public void testGetPropertiesWithSuperAndIgnored() {
		AttributeReaderAlgorithm a1 = new AttributeReaderAlgorithm(
				Arrays.asList(new String[] { "date" }), true);
		List<PropertyMetadata> metadatas1 = a1.getProperties(TestBean.class);
		assertNotNull(metadatas1);
		assertEquals(2, metadatas1.size());
		assertMetadata("string", String.class, null, "string",
				metadatas1.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas1.get(1));
		AttributeReaderAlgorithm a2 = new AttributeReaderAlgorithm(
				Arrays.asList(new String[] { "string" }), true);
		List<PropertyMetadata> metadatas2 = a2.getProperties(TestBean.class);
		assertNotNull(metadatas2);
		assertEquals(2, metadatas2.size());
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas2.get(0));
		assertMetadata("date", Date.class, null, "date", metadatas2.get(1));
	}

	@Test
	public void testGetPropertiesWithSuperAndWithoutIgnored() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(null, true);
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(3, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
		assertMetadata("date", Date.class, null, "date", metadatas.get(2));
	}

	@Test
	public void testGetPropertiesWithoutSuperAndIgnored() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(
				Arrays.asList(new String[] { "date" }), false);
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(2, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
	}

	@Test
	public void testGetPropertiesWithoutSuperAndWithoutIgnored() {
		AttributeReaderAlgorithm a = new AttributeReaderAlgorithm(null, false);
		List<PropertyMetadata> metadatas = a.getProperties(TestBean.class);
		assertNotNull(metadatas);
		assertEquals(2, metadatas.size());
		assertMetadata("string", String.class, null, "string", metadatas.get(0));
		assertMetadata("integer", Integer.class, null, "integer",
				metadatas.get(1));
	}

	private static class SuperTestBean {
		private Date date;

		public Date getDate() {
			return date;
		}

	}

	private static class TestBean extends SuperTestBean {

		private String string;
		private Integer integer;

		public String getString() {
			return string;
		}

		public Integer getInteger() {
			return integer;
		}

	}
}
