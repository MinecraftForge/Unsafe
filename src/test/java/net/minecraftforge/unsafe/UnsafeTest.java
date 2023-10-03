/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.unsafe;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class UnsafeTest {
    @Test
    public void canGetUnsafe() {
        assertNotNull(UnsafeHacks.theUnsafe());
    }
}
