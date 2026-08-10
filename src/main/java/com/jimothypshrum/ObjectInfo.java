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

import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.InterfaceID;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableBiMap;


class ObjectInfo
{
    static final ImmutableBiMap<Integer, String> OBJECT_ID_NICKNAME = new ImmutableBiMap.Builder<Integer, String>()
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_1, "B01")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_2, "B02")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_3, "B03")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_4, "B04")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_5, "B05")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_6, "B06")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_7, "B07")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_8, "B08")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_9, "B09")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_10, "B10")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_11, "B11")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_12, "B12")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_13, "B13")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_15, "B14")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_18, "B15")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_17, "B16")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_19, "B17")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_1, "P01")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_2, "P02")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_3, "P03")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_4, "P04")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_5, "P05")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_6, "P06")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_1, "P07")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_2, "P08")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_3, "P09")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_4, "P10")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_LARGE_1, "P11")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_LARGE_2, "P12")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_LARGE_3, "P13")
            .put(ObjectID.CAMDOZAAL_VAULT_DOOR, "EXIT")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_CLOSED, "CLOSED")
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_OPEN, "OPEN")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_AVAILABLE, "SIMPLE")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_AVAILABLE, "ELABORATE")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_LARGE_AVAILABLE, "ORNATE")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_EMPTY, "EMPTY")
            .build();

    static final ImmutableBiMap<String, Integer> NICKNAME_OBJECT_ID = OBJECT_ID_NICKNAME.inverse();


    static final ImmutableMap<Integer, String> IMPOSTOR_ID_NICKNAME_ACTIVE = new ImmutableMap.Builder<Integer, String>()
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_OPEN, "OPEN")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_SMALL_AVAILABLE, "SIMPLE")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_MEDIUM_AVAILABLE, "ELABORATE")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_LARGE_AVAILABLE, "ORNATE")
            .build();

    static final ImmutableMap<Integer, String> IMPOSTOR_ID_NICKNAME_INACTIVE = new ImmutableMap.Builder<Integer, String>()
            .put(ObjectID.CAMDOZAAL_VAULT_BARRIER_CLOSED, "CLOSED")
            .put(ObjectID.CAMDOZAAL_VAULT_CHEST_EMPTY, "EMPTY")
            .build();

    static final ImmutableMap<String, String> ROUTE_OBJECT_NUMBERS = new ImmutableMap.Builder<String, String>()
            .put("10","B01")
            .put("11","B02")
            .put("12","B03")
            .put("13","B04")
            .put("14","B05")
            .put("15","B06")
            .put("16","B07")
            .put("17","B08")
            .put("18","B09")
            .put("19","B10")
            .put("20","B11")
            .put("21","B12")
            .put("22","B13")
            .put("23","B14")
            .put("24","B15")
            .put("25","B16")
            .put("26","B17")
            .put("27","P01")
            .put("28","P02")
            .put("29","P03")
            .put("30","P04")
            .put("31","P05")
            .put("32","P06")
            .put("33","P07")
            .put("34","P08")
            .put("35","P09")
            .put("36","P10")
            .put("37","P11")
            .put("38","P12")
            .put("39","P13")
            .put("40","EXIT")
            .build();

    static final ImmutableMap<String, String> STATE_OBJECT_CONVERSIONS = new ImmutableMap.Builder<String, String>()
            .put("B02","1")
            .put("B04","2")
            .put("B06","3")
            .put("B07","4")
            .put("B09","5")
            .put("B10","6")
            .put("B12","7")
            .put("B14","8")
            .put("B15","9")
            .put("B17","0")
            .put("P01","1")
            .put("P02","2")
            .put("P03","3")
            .put("P04","4")
            .put("P05","5")
            .put("P06","6")
            .put("P07","7")
            .put("P08","8")
            .put("P09","9")
            .put("P10","0")
            .build();


}






