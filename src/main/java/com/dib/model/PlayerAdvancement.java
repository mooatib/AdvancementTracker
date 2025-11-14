package com.dib.model;

import java.util.Date;
import java.util.Map;

public record PlayerAdvancement(String key, boolean completed, Map<String, Date> criteria) {
}
