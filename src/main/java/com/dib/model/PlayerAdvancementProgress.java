package com.dib.model;

import java.util.Date;

public record PlayerAdvancementProgress(String key, AdvancementMetadata metadata, boolean awarded,
                                        Date awardedDate) {
}
