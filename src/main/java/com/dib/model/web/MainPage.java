package com.dib.model.web;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

import java.util.Map;
import java.util.UUID;

public class MainPage {
    private static String head() {
        return """
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Minecraft Server - Player Advancements</title>
                </head>
                """;
    }

    private static String getStyle() {
        return """
                        <style>
                                * {
                                    margin: 0;
                                    padding: 0;
                                    box-sizing: border-box;
                                }
                                body {
                                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
                                    color: #fff;
                                    min-height: 100vh;
                                    padding: 20px;
                                }
                
                                .container {
                                    max-width: 1400px;
                                    margin: 0 auto;
                                }
                
                                header {
                                    text-align: center;
                                    padding: 40px 20px;
                                    background: rgba(0, 0, 0, 0.3);
                                    border-radius: 15px;
                                    margin-bottom: 40px;
                                    border: 2px solid rgba(255, 215, 0, 0.3);
                                }
                
                                h1 {
                                    font-size: 3em;
                                    color: #ffd700;
                                    text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.5);
                                    margin-bottom: 10px;
                                }
                
                                .server-info {
                                    font-size: 1.2em;
                                    color: #aaa;
                                }
                
                                .filters {
                                    display: flex;
                                    gap: 15px;
                                    margin-bottom: 30px;
                                    flex-wrap: wrap;
                                    justify-content: center;
                                }
                
                                .filter-btn {
                                    padding: 12px 24px;
                                    background: rgba(255, 255, 255, 0.1);
                                    border: 2px solid rgba(255, 215, 0, 0.3);
                                    color: #fff;
                                    border-radius: 8px;
                                    cursor: pointer;
                                    transition: all 0.3s ease;
                                    font-size: 1em;
                                }
                
                                .filter-btn:hover {
                                    background: rgba(255, 215, 0, 0.2);
                                    border-color: #ffd700;
                                    transform: translateY(-2px);
                                }
                
                                .filter-btn.active {
                                    background: rgba(255, 215, 0, 0.3);
                                    border-color: #ffd700;
                                }
                
                                .players-grid {
                                    display: grid;
                                    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
                                    gap: 25px;
                                    margin-bottom: 40px;
                                }
                
                                .playerAdvancement-card {
                                    background: rgba(0, 0, 0, 0.4);
                                    border-radius: 12px;
                                    padding: 25px;
                                    border: 2px solid rgba(255, 215, 0, 0.2);
                                    transition: all 0.3s ease;
                                    backdrop-filter: blur(10px);
                                }
                
                                .playerAdvancement-card:hover {
                                    transform: translateY(-5px);
                                    border-color: #ffd700;
                                    box-shadow: 0 10px 30px rgba(255, 215, 0, 0.2);
                                }
                
                                .playerAdvancement-header {
                                    display: flex;
                                    align-items: center;
                                    gap: 15px;
                                    margin-bottom: 20px;
                                }
                
                                .playerAdvancement-avatar {
                                    width: 64px;
                                    height: 64px;
                                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                    border-radius: 8px;
                                    display: flex;
                                    align-items: center;
                                    justify-content: center;
                                    font-size: 32px;
                                    border: 3px solid rgba(255, 215, 0, 0.3);
                                }
                
                                .playerAdvancement-info {
                                    flex: 1;
                                }
                
                                .playerAdvancement-name {
                                    font-size: 1.5em;
                                    font-weight: bold;
                                    color: #ffd700;
                                    margin-bottom: 5px;
                                }
                
                                .playerAdvancement-stats {
                                    color: #aaa;
                                    font-size: 0.9em;
                                }
                
                                .progress-section {
                                    margin-bottom: 20px;
                                }
                
                                .progress-header {
                                    display: flex;
                                    justify-content: space-between;
                                    margin-bottom: 10px;
                                    font-size: 0.95em;
                                }
                
                                .progress-label {
                                    color: #ddd;
                                }
                
                                .progress-value {
                                    color: #ffd700;
                                    font-weight: bold;
                                }
                
                                .progress-bar {
                                    width: 100%;
                                    height: 25px;
                                    background: rgba(255, 255, 255, 0.1);
                                    border-radius: 12px;
                                    overflow: hidden;
                                    position: relative;
                                    border: 1px solid rgba(255, 215, 0, 0.2);
                                }
                
                                .progress-fill {
                                    height: 100%;
                                    background: linear-gradient(90deg, #4ade80 0%, #22c55e 100%);
                                    transition: width 0.5s ease;
                                    display: flex;
                                    align-items: center;
                                    justify-content: center;
                                    font-size: 0.85em;
                                    font-weight: bold;
                                    color: #fff;
                                    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
                                }
                
                                .advancements-list {
                                    margin-top: 15px;
                                }
                
                                .advancement-category {
                                    margin-bottom: 15px;
                                }
                
                                .category-title {
                                    font-size: 1.1em;
                                    color: #ffd700;
                                    margin-bottom: 8px;
                                    display: flex;
                                    align-items: center;
                                    gap: 8px;
                                }
                
                                .category-icon {
                                    font-size: 1.2em;
                                }
                
                                .advancement-items {
                                    display: flex;
                                    flex-wrap: wrap;
                                    gap: 8px;
                                }
                
                                .advancement-badge {
                                    padding: 6px 12px;
                                    background: rgba(34, 197, 94, 0.2);
                                    border: 1px solid rgba(34, 197, 94, 0.5);
                                    border-radius: 6px;
                                    font-size: 0.85em;
                                    color: #4ade80;
                                    transition: all 0.2s ease;
                                    position: relative;
                                    cursor: pointer;
                                }
                
                                .advancement-badge:hover {
                                    background: rgba(34, 197, 94, 0.3);
                                    transform: scale(1.05);
                                }
                
                                .advancement-badge .tooltip {
                                    visibility: hidden;
                                    opacity: 0;
                                    position: absolute;
                                    bottom: 100%;
                                    left: 50%;
                                    transform: translateX(-50%);
                                    margin-bottom: 8px;
                                    padding: 8px 12px;
                                    background: rgba(0, 0, 0, 0.95);
                                    color: #fff;
                                    border: 1px solid rgba(255, 215, 0, 0.5);
                                    border-radius: 6px;
                                    font-size: 0.9em;
                                    white-space: nowrap;
                                    z-index: 1000;
                                    pointer-events: none;
                                    transition: opacity 0.3s ease, visibility 0.3s ease;
                                }
                
                                .advancement-badge:hover .tooltip {
                                    visibility: visible;
                                    opacity: 1;
                                }
                
                                .advancement-badge.locked {
                                    background: rgba(255, 255, 255, 0.05);
                                    border-color: rgba(255, 255, 255, 0.2);
                                    color: #666;
                                }
                
                                .rank-badge {
                                    display: inline-block;
                                    padding: 4px 10px;
                                    background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
                                    color: #1a1a2e;
                                    border-radius: 5px;
                                    font-size: 0.8em;
                                    font-weight: bold;
                                    margin-left: 10px;
                                }
                
                                .rank-badge.gold { background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); }
                                .rank-badge.silver { background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%); }
                                .rank-badge.bronze { background: linear-gradient(135deg, #cd7f32 0%, #d4a574 100%); }
                
                                @media (max-width: 768px) {
                                    h1 {
                                        font-size: 2em;
                                    }
                
                                    .players-grid {
                                        grid-template-columns: 1fr;
                                    }
                
                                    .filters {
                                        flex-direction: column;
                                    }
                
                                    .filter-btn {
                                        width: 100%;
                                    }
                                }
                        </style>
                """;
    }

    private static String getBody() {
        return """        
                <body>
                    <div class="container">
                        <header>
                            <h1>⛏️ Minecraft Server Advancements</h1>
                            <div class="server-info">Server: mc.araux.net | Players Online: 24</div>
                        </header>
                    </div>
                </body>
                """;
    }

    public static String build(Map<UUID, PlayerAdvancement> playerAdvancements) {
        StringBuilder html = new StringBuilder();
        html.append("""
                <!DOCTYPE html>
                <html>
                """);
        html.append(head());
        html.append(getStyle());
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("""
                    <header>
                        <h1>⛏️ Minecraft Server Advancements</h1>
                        <div class="server-info">Server: mc.araux.net | Players Online: %d</div>
                    </header>
                """.formatted(playerAdvancements.size()));

        // Players grid
        html.append("        <div class=\"players-grid\">\n");

        for (PlayerAdvancement player : playerAdvancements.values()) {
            int completedAdvancements = player.completedAdvancements().size();

            html.append("            <div class=\"playerAdvancement-card\">\n");

            // Player header
            html.append("                <div class=\"playerAdvancement-header\">\n");
            html.append("                    <div class=\"playerAdvancement-avatar\">\n");
            html.append("                        ").append(player.getName().charAt(0)).append("\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"playerAdvancement-info\">\n");
            html.append("                        <div class=\"playerAdvancement-name\">").append(player.getName()).append("</div>\n");
            html.append("                        <div class=\"playerAdvancement-stats\">\n");
            html.append("                            ").append(completedAdvancements).append("/").append(player.getTotalAdvancements()).append(" Advancements\n");
            html.append("                        </div>\n");
            html.append("                    </div>\n");
            html.append("                </div>\n");

            // Progress section
            html.append("                <div class=\"progress-section\">\n");
            html.append("                    <div class=\"progress-header\">\n");
            html.append("                        <span class=\"progress-label\">Overall Progress</span>\n");
            html.append("                        <span class=\"progress-value\">").append(String.format("%.1f%%", player.getProgressPercentage())).append("</span>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"progress-bar\">\n");
            html.append("                        <div class=\"progress-fill\" style=\"width: ").append(String.format("%.1f%%", player.getProgressPercentage())).append("\">\n");
            if (player.getProgressPercentage() > 10) {
                html.append("                            ").append(String.format("%.1f%%", player.getProgressPercentage())).append("\n");
            }
            html.append("                        </div>\n");
            html.append("                    </div>\n");
            html.append("                </div>\n");

            // Advancements list
            html.append("                <div class=\"advancements-list\">\n");
            html.append("                    <div class=\"advancement-items\">\n");

            for (PlayerAdvancementProgress advancement : player.getAdvancements().values()) {
                if (advancement.completed()) {
                    String name = advancement.title();
                    String description = advancement.description();
                    String badgeClass = "advancement-badge";
                    String emoji = "✅ ";

                    html.append("                        <div class=\"").append(badgeClass).append("\">\n");
                    html.append("                            ").append(emoji).append(name).append("\n");
                    html.append("                            <span class=\"tooltip\">").append(description).append("</span>\n");
                    html.append("                        </div>\n");
                }
            }

            html.append("                    </div>\n");
            html.append("                </div>\n");
            html.append("            </div>\n");
        }

        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    public static byte[] renderPage(Map<UUID, PlayerAdvancement> playerAdvancements) {
        return MainPage.build(playerAdvancements).getBytes();
    }
}
