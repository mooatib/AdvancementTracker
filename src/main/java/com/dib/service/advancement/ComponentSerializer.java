package com.dib.service.advancement;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public interface ComponentSerializer {
    default String serializeComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);

    }

    default String formatAdvancementTitle(Component component) {
        String serialized = serializeComponent(component);
        return serialized.substring(1, serialized.length() - 1);
    }
}
