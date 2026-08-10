/*
 * Copyright (c) 2026, JimothyPShrum <https://github.com/JimothyPShrum>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jimothypshrum;

import net.runelite.api.gameval.VarbitID;
import net.runelite.api.gameval.VarPlayerID;
import com.google.common.collect.ImmutableMap;

public class VarInfo
{
    static final ImmutableMap<Integer, String> VARBIT_IDS = new ImmutableMap.Builder<Integer, String>()
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_A, "CAMDOZAAL_VAULT_BARRIER_A")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_B, "CAMDOZAAL_VAULT_BARRIER_B")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_C, "CAMDOZAAL_VAULT_BARRIER_C")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_D, "CAMDOZAAL_VAULT_BARRIER_D")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_E, "CAMDOZAAL_VAULT_BARRIER_E")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_F, "CAMDOZAAL_VAULT_BARRIER_F")
            .put(VarbitID.CAMDOZAAL_VAULT_BARRIER_G, "CAMDOZAAL_VAULT_BARRIER_G")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_1, "CAMDOZAAL_VAULT_CHEST_SMALL_1")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_2, "CAMDOZAAL_VAULT_CHEST_SMALL_2")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_3, "CAMDOZAAL_VAULT_CHEST_SMALL_3")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_4, "CAMDOZAAL_VAULT_CHEST_SMALL_4")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_5, "CAMDOZAAL_VAULT_CHEST_SMALL_5")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_SMALL_6, "CAMDOZAAL_VAULT_CHEST_SMALL_6")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_MEDIUM_1, "CAMDOZAAL_VAULT_CHEST_MEDIUM_1")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_MEDIUM_2, "CAMDOZAAL_VAULT_CHEST_MEDIUM_2")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_MEDIUM_3, "CAMDOZAAL_VAULT_CHEST_MEDIUM_3")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_MEDIUM_4, "CAMDOZAAL_VAULT_CHEST_MEDIUM_4")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_LARGE_1, "CAMDOZAAL_VAULT_CHEST_LARGE_1")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_LARGE_2, "CAMDOZAAL_VAULT_CHEST_LARGE_2")
            .put(VarbitID.CAMDOZAAL_VAULT_CHEST_LARGE_3, "CAMDOZAAL_VAULT_CHEST_LARGE_3")
            .build();

    static final int TIME_REMAINING_VARBIT_ID = VarbitID.CAMDOZAAL_VAULT_TIME_REMAINING;
    static final int VARPLAYER_ID = VarPlayerID.CAMDOZAAL_STORED_BARRONITE;

}
