package org.shortbrain.vaadin.container.annotation.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Generic annotation reader.
 * 
 * This currently only work on annotation for Type.
 * 
 * @author Vincent Demeester <vincent@demeester.fr>
 * @param <T>
 *            type of the Annotation.
 * 
 */
public abstract class BeanAbstractAnnotationReader<T> {

	private final Class<?> originalBeanClass;
	private final Class<?> beanClass;
	private T metadata;

	/**
	 * Creates a BeanAbstractAnnotationReader.
	 * 
	 * @param originalBeanClass
	 *            the original bean type.
	 * @param beanClass
	 *            the <em>annotated</em> bean type (might be a parent).
	 * @throws IllegalArgumentException
	 *             if the originalBeanClass or beanClass is null.
	 */
	public BeanAbstractAnnotationReader(Class<?> originalBeanClass,
			Class<?> beanClass) {
		if (originalBeanClass == null || beanClass == null) {
			throw new IllegalArgumentException(
					"originalBeanClass and beanClass cannot be null.");
		}
		this.originalBeanClass = originalBeanClass;
		this.beanClass = beanClass;
	}

	/**
	 * @return the originalEntityClass
	 */
	protected Class<?> getOriginalBeanClass() {
		return originalBeanClass;
	}

	/**
	 * @return the entityClass
	 */
	protected Class<?> getBeanClass() {
		return beanClass;
	}

	public void setMetadatas(T metadata) {
		this.metadata = metadata;
	}

	public T getMetadatas() {
		return metadata;
	}

	/**
	 * Get the type of the given propertyAttribute
	 * 
	 * @param propertyAttribute
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 *             if the field is not found or if propertyAttribute is null.
	 */
	protected Class<?> getPropertyType(String propertyAttribute)
			throws SecurityException, NoSuchFieldException {
		if (propertyAttribute == null) {
			throw new NoSuchFieldException("no null field.");
		}
		Class<?> ret = null;
		if (propertyAttribute.contains(".")) {
			String fieldName = propertyAttribute.split("\\.")[0];
			String subFieldName = propertyAttribute.split("\\.")[1];
			Field field = getField(getOriginalBeanClass(), fieldName);
			if (field == null) {
				throw new NoSuchFieldException("No field " + fieldName
						+ " for class " + getOriginalBeanClass().getName()
						+ ".");
			}
			Class<?> fieldClass = field.getType();
			ret = fieldClass.getDeclaredField(subFieldName).getType();
		} else {
			Field field = getField(getOriginalBeanClass(), propertyAttribute);
			if (field == null) {
				throw new NoSuchFieldException("No field " + propertyAttribute
						+ " for class " + getOriginalBeanClass().getName()
						+ ".");
			}
			ret = field.getType();
		}
		return ret;
	}

	/**
	 * Get the Field for the given class and parents.
	 * 
	 * @param klass
	 *            the class where we will look for the field.
	 * @param fieldName
	 *            the name of the field.
	 * @return the field or null if we cannot find the field.
	 * @throws IllegalArgumentException
	 *             if klass or fieldName are null.
	 */
	protected Field getField(Class<?> klass, String fieldName) {
		if (klass == null || fieldName == null) {
			throw new IllegalArgumentException(
					"klass or fieldName cannot be null.");
		}
		Field field = null;
		if (klass != Object.class) {
			try {
				field = klass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				field = getField(klass.getSuperclass(), fieldName);
			}
		}
		return field;
	}

	/**
	 * Get the annotated type by walking in superclass to find the annotation
	 * 
	 * @param beanClass
	 *            The type that should be annotated
	 * @param annotationClass
	 *            the type of the annotation
	 * @return the "real" type annotated, either entityClass or a parent.
	 * @throws IllegalArgumentException
	 *             if entityClass or annotationClass are null or if entityClass
	 *             (and its parent) is not annotated with annotationClass.
	 */
	public static Class<?> getAnnotatedClass(Class<?> beanClass,
			Class<? extends Annotation> annotationClass) {
		if (beanClass == null || annotationClass == null) {
			throw new IllegalArgumentException(
					"beanClass and annotationClass cannot be null.");
		}
		Class<?> ret = null;
		if (!beanClass.isAnnotationPresent(annotationClass)) {
			// On remonte dans la hierarchie jusqu'à Object
			if (beanClass.getSuperclass() != Object.class) {
				ret = getAnnotatedClass(beanClass.getSuperclass(),
						annotationClass);
			}
			if (ret == null) {
				throw new IllegalArgumentException(
						"beanClass and its super classes are not annotated with "
								+ annotationClass.getSimpleName() + ".");
			}
		} else {
			ret = beanClass;
		}
		return ret;
	}

}
