/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.data.pipeline.distsql;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.data.pipeline.core.job.progress.config.PipelineProcessConfiguration;
import org.apache.shardingsphere.data.pipeline.core.job.progress.config.PipelineProcessConfigurationUtils;
import org.apache.shardingsphere.data.pipeline.core.context.PipelineContextKey;
import org.apache.shardingsphere.data.pipeline.core.metadata.PipelineProcessConfigurationPersistService;
import org.apache.shardingsphere.infra.instance.metadata.InstanceType;
import org.apache.shardingsphere.infra.merge.result.impl.local.LocalDataQueryResultRow;
import org.apache.shardingsphere.infra.util.json.JsonUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Show transmission rule query result.
 */
@RequiredArgsConstructor
public final class ShowTransmissionRuleQueryResult {
    
    private final String jobType;
    
    private final PipelineProcessConfigurationPersistService persistService = new PipelineProcessConfigurationPersistService();
    
    /**
     * Get rows.
     * 
     * @return query result row
     */
    public Collection<LocalDataQueryResultRow> getRows() {
        PipelineProcessConfiguration processConfig = PipelineProcessConfigurationUtils.convertWithDefaultValue(persistService.load(new PipelineContextKey(InstanceType.PROXY), jobType));
        return Collections.singleton(new LocalDataQueryResultRow(getString(processConfig.getRead()), getString(processConfig.getWrite()), getString(processConfig.getStreamChannel())));
    }
    
    private String getString(final Object obj) {
        return null == obj ? "" : JsonUtils.toJsonString(obj);
    }
    
    /**
     * Get column names.
     * 
     * @return column names
     */
    public Collection<String> getColumnNames() {
        return Arrays.asList("read", "write", "stream_channel");
    }
}
