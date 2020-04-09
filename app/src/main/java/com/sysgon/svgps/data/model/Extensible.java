/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sysgon.svgps.data.model;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Extensible extends Message {
    private String attributes = "{}";

    private Map<String, Object> attributesP = new LinkedHashMap<>();
    public Map<String, Object> getAttributesP() {
        return attributesP;
    }

    public void setAttributesP(Map<String, Object> attributesP) {
        this.attributesP = attributesP;
    }

    public void set(String key, boolean value) {
        attributesP.put(key, value);
    }

    public void set(String key, int value) {
        attributesP.put(key, value);
    }

    public void set(String key, long value) {
        attributesP.put(key, value);
    }

    public void set(String key, double value) {
        attributesP.put(key, value);
    }

    public void set(String key, String value) {
        if (value != null && !value.isEmpty()) {
            attributesP.put(key, value);
        }
    }

    public void add(Map.Entry<String, Object> entry) {
        if (entry != null && entry.getValue() != null) {
            attributesP.put(entry.getKey(), entry.getValue());
        }
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
        try {
            JSONObject attr = new JSONObject(attributes);
            Iterator<String> keys = attr.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if (attr.get(key) instanceof Boolean) {
                    this.set(key, (Boolean) attr.get(key));
                }else if(attr.get(key) instanceof Integer){
                    this.set(key, (Integer) attr.get(key));
                }else if(attr.get(key) instanceof Long){
                    this.set(key, (Long) attr.get(key));
                }else if(attr.get(key) instanceof Double){
                    this.set(key, (Double) attr.get(key));
                }else if(attr.get(key) instanceof String){
                    this.set(key, (String) attr.get(key));
                }
            }
        } catch (JSONException e) {
            Log.e("Extensible.java", e.getMessage());
        }
    }
}
