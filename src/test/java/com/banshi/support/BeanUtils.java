package com.banshi.support;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.Resolver;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

public class BeanUtils extends BeanUtilsBean {

    public BeanUtils(ConvertUtilsBean convertUtilsBean) {
        super(convertUtilsBean, new PropertyUtilsBean());
    }

    public void populateExt(Object bean, Map properties)
            throws IllegalAccessException, InvocationTargetException {

        // Do nothing unless both arguments have been specified
        if ((bean == null) || (properties == null)) {
            return;
        }

        // Loop through the property name/value pairs to be set
        Iterator entries = properties.entrySet().iterator();
        while (entries.hasNext()) {

            // Identify the property name and value(s) to be assigned
            Map.Entry entry = (Map.Entry) entries.next();
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }

            // Perform the assignment for this property
            setPropertyExt(bean, name, entry.getValue());
        }

    }

    public void setPropertyExt(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {

        // Resolve any nested expression to get the actual target bean
        Object target = bean;
        Resolver resolver = getPropertyUtils().getResolver();
        while (resolver.hasNested(name)) {
            try {
                target = getPropertyUtils().getProperty(target, resolver.next(name));
                name = resolver.remove(name);
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
        }

        // Declare local variables we will require
        String propName = resolver.getProperty(name); // Simple name of target property
        Class type = null;                            // Java type of target property
        int index = resolver.getIndex(name);         // Indexed subscript value (if any)

        // Calculate the property type
        if (target instanceof DynaBean) {
            DynaClass dynaClass = ((DynaBean) target).getDynaClass();
            DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
            if (dynaProperty == null) {
                return; // Skip this property setter
            }
            type = dynaProperty.getType();
        } else if (target instanceof Map) {
            type = Object.class;
        } else if (target != null && target.getClass().isArray() && index >= 0) {
            type = Array.get(target, index).getClass();
        } else {
            try {
                type = getPropertyUtils().getPropertyType(target, name);
                if (type == null) {
                    return; // Skip this property
                }
            } catch (Exception e) {
                return; // Skip this property
            }
        }

        // Convert the specified value to the required type
        Object newValue = null;
        if (type.isArray() && (index < 0)) { // Scalar value into array
            if (value == null) {
                String[] values = new String[1];
                values[0] = null;
                newValue = getConvertUtils().convert(values, type);
            } else if (value instanceof String) {
                newValue = getConvertUtils().convert(value, type);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert((String[]) value, type);
            } else {
                newValue = convert(value, type);
            }
        } else if (type.isArray()) {         // Indexed value into array
            if (value instanceof String || value == null) {
                newValue = getConvertUtils().convert((String) value, type.getComponentType());
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0], type.getComponentType());
            } else {
                newValue = convert(value, type.getComponentType());
            }
        } else {                             // Value into scalar
            if (value instanceof String) {
                newValue = getConvertUtils().convert((String) value, type);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0], type);
            } else {
                try {
                    newValue = convert(value, type);
                } catch (Exception e) {
                    return;  //Skip this property
                }
            }
        }

        // Invoke the reflection
        try {
            ReflectionTestUtils.setField(target, name, newValue, type);
        } catch (Exception e) {
            throw new InvocationTargetException(e, "Cannot set " + propName);
        }

    }
}
