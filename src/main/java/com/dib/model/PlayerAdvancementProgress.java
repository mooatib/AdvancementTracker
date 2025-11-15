package com.dib.model;

import net.kyori.adventure.text.Component;

import java.util.Date;
import java.util.Map;

public record PlayerAdvancementProgress(String key, Component title, Component description, boolean completed,
                                        Map<String, Date> criteria) {
}
