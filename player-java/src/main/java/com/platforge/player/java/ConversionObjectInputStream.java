package com.platforge.player.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.platforge.data.Color;
import com.platforge.data.EditorData;
import com.platforge.data.Vector;

public class ConversionObjectInputStream extends ObjectInputStream {

    public static Map<String, Class<?>> classNameMapping = initclassNameMapping(); 

    private static Map<String, Class<?>> initclassNameMapping(){
        Map<String, Class<?>> res = new HashMap<String, Class<?>>();
        res.put("edu.elon.honors.price.physics.Vector", Vector.class);
        res.put("edu.elon.honors.price.game.Color", Color.class);
        res.put("edu.elon.honors.price.maker.MapEditorView$EditorData", EditorData.class);
        return Collections.unmodifiableMap(res);
    }

    public ConversionObjectInputStream(InputStream in) throws IOException {
        super(in);
    }


    protected ConversionObjectInputStream() throws IOException, SecurityException {
        super();
    }

    @Override
    protected java.io.ObjectStreamClass readClassDescriptor() 
            throws IOException, ClassNotFoundException {
        ObjectStreamClass desc = super.readClassDescriptor();
        String name = desc.getName();
    	System.out.println(name);
        if (classNameMapping.containsKey(name)) {
        	Class<?> clazz = classNameMapping.get(name);
        	return clazz == null ? desc : ObjectStreamClass.lookup(clazz);
        } else if (name.contains("edu.elon.honors.price")) {
        	name = name.replace("edu.elon.honors.price", "com.platforge");
        	Class<?> clazz = Class.forName(name);
        	return ObjectStreamClass.lookup(clazz);
        }
        return desc;
    }

}