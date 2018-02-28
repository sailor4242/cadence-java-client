/*
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.uber.cadence.internal.dispatcher;

import com.uber.cadence.internal.worker.CheckedExceptionWrapper;
import com.uber.cadence.workflow.Workflow;
import org.junit.Assert;
import org.junit.Test;

public class CheckedExceptionWrapperTest {

    @Test
    public void testUnwrap() {
        try {
            try {
                try {
                    try {
                        try {
                            throw new Exception("1");
                        } catch (Exception e) {
                            throw Workflow.throwWrapped(e);
                        }
                    } catch (Exception e) {
                        throw Workflow.throwWrapped(e);
                    }
                } catch (Exception e) {
                    throw new Exception("2", e);
                }
            } catch (Exception e) {
                throw Workflow.throwWrapped(e);
            }
        } catch (Exception e) {
            Throwable result = CheckedExceptionWrapper.unwrap(e);
            Assert.assertEquals("2", result.getMessage());
            Assert.assertEquals("1", result.getCause().getMessage());
            Assert.assertNull(result.getCause().getCause());
        }
        Throwable e = new Throwable("5");
        Throwable eu = CheckedExceptionWrapper.unwrap(e);
        Assert.assertEquals(e, eu);
    }
}