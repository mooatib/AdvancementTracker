package com.dib.model;

import java.util.Date;
import java.util.Map;

public record PlayerAdvancementProgress(String key, String title, String description, boolean completed,
                                        Map<String, Date> criteria) {
}
