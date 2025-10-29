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

package com.ai.poc.agent.jira.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    @SneakyThrows
    public static String readSystemResource(final String location) {
        return IOUtils.toString(resourceLoader.getResource(location).getInputStream(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static List<String> readSystemResourceToList(final String location) {
        return IOUtils.readLines(resourceLoader.getResource(location).getInputStream(), StandardCharsets.UTF_8);
    }
}
