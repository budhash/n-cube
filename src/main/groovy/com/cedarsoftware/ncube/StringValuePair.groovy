/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package com.cedarsoftware.ncube

import com.cedarsoftware.util.StringUtilities
import groovy.transform.CompileStatic

/**
 * Class used to carry the NCube meta-information
 * to the client.
 *
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License")
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
@CompileStatic
class StringValuePair<V> implements Map.Entry<String,V> {

    private String key
    private V value

    StringValuePair(String key, V value) {
        this.key = key
        this.value = value
    }

    boolean equals(Object that)
    {
        if (this.is(that))
        {
            return true
        }
        if (that instanceof String)
        {
            return StringUtilities.equalsIgnoreCase(key, (String)that)
        }
        if (that instanceof StringValuePair)
        {
            return StringUtilities.equalsIgnoreCase(key, ((StringValuePair) that).key)
        }
        return false
    }

    int hashCode() {
        return hash(key)
    }

    String toString() {
        return key + ':' + value
    }

    String getKey() {
        return key
    }

    void setKey(String key) {
        this.key = key
    }

    V getValue() {
        return value
    }

    V setValue(V value) {
        this.value = value
        return value
    }

    /**
     * Helper method to handle object hashes for possibly null values
     */
    protected static int hash(Object object) {
        return (object == null) ? 0xbabe : object.hashCode()
    }
}
