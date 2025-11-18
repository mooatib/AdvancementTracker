package com.dib.model.web;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

import java.util.Map;
import java.util.UUID;

public class Homepage {
    public static byte[] renderPage(Map<UUID, PlayerAdvancement> playerAdvancements, int playerCount, int advancementCount) {
        StringBuilder html = new StringBuilder();

        html.append(HtmlTemplates.PageWrapper.getHeader());
        html.append(String.format(
                """
                            <body>
                                <div class="container">
                                    <div class="header">
                                        <h1>🏆 Advancements leaderboard 🏆</h1>
                                        <h3>Server: mc.araux.net | Players Online: %d</h3>
                                    </div>
                                <div class="players-grid">
                        """
                , playerCount));
        html.append(HtmlTemplates.PlayerCard.getStyle());
        html.append(HtmlTemplates.AdvancementCard.getStyle());

        int playerRank = 1;
        for (PlayerAdvancement player : playerAdvancements.values()) {
            html.append(HtmlTemplates.PlayerCard.render(player, playerRank, advancementCount));

            for (PlayerAdvancementProgress advancement : player.getAdvancements().values()) {
                if (advancement.awarded()) {
                    html.append(HtmlTemplates.AdvancementCard.render(advancement));
                }
            }

            html.append(HtmlTemplates.PlayerCard.renderClose());
            playerRank++;
        }

        html.append(HtmlTemplates.PageWrapper.getFooter());

        return html.toString().getBytes();
    }
}
