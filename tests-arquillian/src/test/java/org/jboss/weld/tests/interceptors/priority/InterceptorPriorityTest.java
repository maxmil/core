/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.weld.tests.interceptors.priority;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.tests.category.Integration;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@Category(Integration.class)
public class InterceptorPriorityTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(BeanArchive.class).addPackage(InterceptorPriorityTest.class.getPackage());
    }

    @Test
    public void testPriorityIgnoredWithInterceptorsAnnotation(InterceptedEjb interceptedEjb) {
        InterceptAudit.reset();
        interceptedEjb.callInterceptedMethod();
        assertEquals(2, InterceptAudit.getExecutions().size());
        assertEquals(HighPriorityInterceptor.class, InterceptAudit.get(0));
        assertEquals(LowPriorityInterceptor.class, InterceptAudit.get(1));
    }

    @Test
    public void testPriorityNotIgnoredForCdiInterceptor(InterceptedCdi interceptedCdi) {
        InterceptAudit.reset();
        interceptedCdi.callInterceptedMethod();
        assertEquals(2, InterceptAudit.getExecutions().size());
        assertEquals(LowPriorityInterceptor.class, InterceptAudit.get(0));
        assertEquals(HighPriorityInterceptor.class, InterceptAudit.get(1));
    }

}
