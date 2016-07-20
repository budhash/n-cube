package com.cedarsoftware.ncube.util;

import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *  @author Ken Partlow (kpartlow@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
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
public class CdnClassLoader extends GroovyClassLoader
{
    private final boolean _preventRemoteBeanInfo;
    private final boolean _preventRemoteCustomizer;
    private final ClassLoader parentClassLoader = super.getParent();

    /**
     * creates a GroovyClassLoader using the given ClassLoader as parent
     */
    public CdnClassLoader(ClassLoader loader, boolean preventRemoteBeanInfo, boolean preventRemoteCustomizer)
    {
        super(loader, null);
        _preventRemoteBeanInfo = preventRemoteBeanInfo;
        _preventRemoteCustomizer = preventRemoteCustomizer;
    }

    public CdnClassLoader(List<String> list)
    {
        this(CdnClassLoader.class.getClassLoader(), true, true);
        addURLs(list);
    }

    protected Class<?> findClass(final String name) throws ClassNotFoundException
    {
        return parentClassLoader.loadClass(name);
    }

    private void addURLs(List<String> list)
    {
        for (String url : list)
        {
            addURL(url);
        }
    }

    public void addURL(String url)
    {
        if (url != null)
        {
            try
            {
                if (!url.endsWith("/"))
                {
                    url += "/";
                }
                addURL(new URL(url));
            }
            catch (MalformedURLException e)
            {
                throw new IllegalArgumentException("added url was malformed: " + url, e);
            }
        }

    }

    /**
     * @param name Name of resource
     * @return true if we should only look locally.
     */
    boolean isLocalOnlyResource(String name)
    {
        if (name.endsWith(".class"))
        {
            return true;
        }

        //  Groovy ASTTransform Service
        if (name.endsWith("org.codehaus.groovy.transform.ASTTransformation"))
        {
            return true;
        }

        if (name.startsWith("META-INF") ||
            name.startsWith("ncube/grv/") ||
            name.startsWith("java/") ||
            name.startsWith("groovy/") ||
            name.startsWith("org/") ||
            name.startsWith("net/") ||
            name.startsWith("com/google/") ||
            name.startsWith("com/cedarsoftware/"))
        {
            if (name.startsWith("ncube/grv/closure/"))
            {
                return false;
            }
            return true;
        }

        if (_preventRemoteBeanInfo)
        {
            if (name.endsWith("BeanInfo.groovy"))
            {
                return true;
            }
        }

        if (_preventRemoteCustomizer)
        {
            if (name.endsWith("Customizer.groovy"))
            {
                return true;
            }
        }

        return false;
    }

    public Enumeration<URL> getResources(String name) throws IOException
    {
        if (isLocalOnlyResource(name))
        {
            return new Enumeration<URL>()
            {
                public boolean hasMoreElements()
                {
                    return false;
                }

                public URL nextElement()
                {
                    throw new NoSuchElementException();
                }
            };
        }
        return super.getResources(name);
    }

    public URL getResource(String name)
    {
        if (isLocalOnlyResource(name))
        {
            return null;
        }
        return super.getResource(name);
    }
}
