package com.example.osuinfo.api.osu

enum class Mods(val value: Int, val acronym: String) {
    NO_MOD(0, "NM"),
    NO_FAIL(1, "NF"),
    EASY(2, "EZ"),
    TOUCH_DEVICE(4, "TD"),
    HIDDEN(8, "HD"),
    HARD_ROCK(16, "HR"),
    SUDDEN_DEATH(32, "SD"),
    DOUBLE_TIME(64, "DT"),
    RELAX(128, "RL"),
    HALF_TIME(256, "HT"),
    NIGHT_CORE(512, "NC"),
    FLASH_LIGHT(1024, "FL"),
    AUTO(2048, "AT"),
    SPUN_OUT(4096, "SO"),
    AUTO_PILOT(8192, "AP"),
    PERFECT(16384, "PF"),
    KEY_4(32768, "4K"),
    KEY_5(65536, "5K"),
    KEY_6(131072, "6K"),
    KEY_7(262144, "7K"),
    KEY_8(524288, "8K"),
    FADE_IN(1048576, "FI"),
    RANDOM(2097152, "RD"),
    CINEMA(4194304, "CN"),
    TARGET(8388608, "TP"),
    KEY_9(16777216, "9K"),
    KEY_COOP(33554432, "2P"),
    KEY_1(67108864, "1K"),
    KEY_3(134217728, "3K"),
    KEY_2(268435456, "2K"),
    SCORE_V2(536870912, "V2"),
    MIRROR(1073741824, "MR")
}