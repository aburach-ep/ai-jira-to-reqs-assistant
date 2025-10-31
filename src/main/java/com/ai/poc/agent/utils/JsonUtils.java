/*
 * Copyright (c) 2019 CoreLogic, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of CoreLogic, Inc.
 * It is furnished under license and may only be used or copied in accordance
 * with the terms of such license.
 * This software is subject to change without notice and no information
 * contained in it should be construed as commitment by CoreLogic, Inc.
 * CoreLogic, Inc. cannot accept any responsibility, financial or otherwise, for any
 * consequences arising from the use of this software except as otherwise stated in
 * the terms of the license.
 */

package com.ai.poc.agent.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final ObjectWriter WRITER;
    private static final ObjectReader READER;

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // ignore Unknown Fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // workaround for JsonMappingException: Illegal unquoted character ((CTRL-CHAR, code 13)):
        // has to be escaped using backslash to be included in string value
        mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        WRITER = mapper.writer();
        READER = mapper.reader();
    }

    @SneakyThrows
    public static <T> T read(final String value, final Class<T> clazz) {
        return READER.forType(clazz).readValue(value);
    }

    @SneakyThrows
    public static String write(final Object value) {
        return WRITER.writeValueAsString(value);
    }

    @SneakyThrows
    public static String writePretty(final Object value) {
        return WRITER.withDefaultPrettyPrinter().writeValueAsString(value);
    }

}
