package com.dib.model.web;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

public class HtmlTemplates {
    public static class PageWrapper {
        public static String getHeader() {
            return """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Minecraft Server Advancements</title>
                        <style>
                            @font-face {
                                font-family: 'Minecraft Regular';
                                font-style: normal;
                                font-weight: normal;
                                src: url('font/1_MinecraftRegular1.woff') format('woff');
                            }
                    
                            @font-face {
                                font-family: 'Minecraft Italic';
                                font-style: normal;
                                font-weight: normal;
                                src: url('font/2_MinecraftItalic1.woff') format('woff');
                            }
                    
                            @font-face {
                                font-family: 'Minecraft Bold';
                                font-style: normal;
                                font-weight: normal;
                                src: url('font/3_MinecraftBold1.woff') format('woff');
                            }
                    
                            @font-face {
                                font-family: 'Minecraft Bold Italic';
                                font-style: normal;
                                font-weight: normal;
                                src: url('font/4_MinecraftBoldItalic1.woff') format('woff');
                            }
                    
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }
                    
                            body {
                                font-family: 'Minecraft Regular', 'Courier New', monospace;
                                background: linear-gradient(to bottom, #2b2b2b 0%, #1a1a1a 100%);
                                background-attachment: fixed;
                                color: #fff;
                                padding: 20px;
                                min-height: 100vh;
                            }
                    
                            .container {
                                max-width: 1400px;
                                margin: 0 auto;
                            }
                    
                            .header {
                                text-align: center;
                                margin-bottom: 40px;
                                padding: 20px;
                                background: rgba(0, 0, 0, 0.5);
                                border: 2px solid #000000;
                                box-shadow:
                                    inset 0 3px rgb(219, 162, 19),
                                    inset 0 -3px rgb(73, 54, 6),
                                    inset 3px 0 rgb(219, 162, 19),
                                    inset -3px 0 rgb(73, 54, 6);
                            }
                    
                            .header h1 {
                                font-size: 42px;
                                text-shadow: 3px 3px 0px #3f3f3f;
                                color: #ffff55;
                                margin-bottom: 10px;
                            }
                    
                            .players-grid {
                                display: grid;
                                grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
                                gap: 25px;
                                margin-bottom: 30px;
                            }
                        </style>
                    </head>
                    """;
        }

        public static String getFooter() {
            return """
                            </div>
                        </div>
                    </body>
                    </html>
                    """;
        }
    }

    public static class PlayerCard {
        public static String getStyle() {
            return """
                    <style>
                        .player-card {
                            background: linear-gradient(135deg, #3a3a3a 0%, #2a2a2a 100%);
                            border: 2px solid #000000;
                            box-shadow:
                                inset 0 6px rgb(58, 58, 58),
                                inset 0 -6px rgb(42, 42, 42),
                                inset 6px 0 rgb(58, 58, 58),
                                inset -6px 0 rgb(42, 42, 42);
                            padding: 20px;
                            border-radius: 8px;
                            transition: transform 0.2s, box-shadow 0.2s;
                        }
                    
                        .player-header {
                            display: flex;
                            align-items: center;
                            margin-bottom: 15px;
                            padding-bottom: 15px;
                            border-bottom: 2px solid #5a5a5a;
                        }
                    
                        .player-avatar {
                            width: 64px;
                            height: 64px;
                            margin-right: 15px;
                            image-rendering: pixelated;
                            border: 2px solid #000;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
                        }
                    
                        .player-info {
                            flex: 1;
                        }
                    
                        .player-name {
                            font-size: 24px;
                            color: #55ff55;
                            text-shadow: 2px 2px 0px #000000;
                            margin-bottom: 5px;
                        }
                    
                        .player-stats {
                            display: flex;
                            gap: 15px;
                            font-size: 14px;
                        }
                    
                        .stat {
                            color: #aaaaaa;
                            text-shadow: 1px 1px 0px #000;
                        }
                    
                        .stat-value {
                            color: #ffff55;
                        }
                    
                        .progress-bar-container {
                            background: #1a1a1a;
                            border: 2px solid #000;
                            height: 24px;
                            margin-top: 10px;
                            position: relative;
                            overflow: hidden;
                        }
                    
                        .progress-bar {
                            height: 100%;
                            background: linear-gradient(to right, #55ff55 0%, #33dd33 100%);
                            transition: width 0.3s ease;
                            box-shadow: inset 0 2px 4px rgba(255, 255, 255, 0.3);
                        }
                    
                        .progress-text {
                            position: absolute;
                            top: 50%;
                            left: 50%;
                            transform: translate(-50%, -50%);
                            color: #fff;
                            text-shadow: 1px 1px 0px #000000;
                            font-weight: bold;
                        }
                    
                        .advancements-list {
                            max-height: 400px;
                            overflow-y: auto;
                            margin-top: 15px;
                            scrollbar-width: thin;
                            scrollbar-color: #888 #f1f1f1;
                        }
                    
                        .advancements-list::-webkit-scrollbar {
                            width: 12px;
                        }
                    
                        .advancements-list::-webkit-scrollbar-track {
                            background: #1a1a1a;
                            border: 1px solid #000;
                        }
                    
                        .advancements-list::-webkit-scrollbar-thumb {
                            background: #5a5a5a;
                            border: 1px solid #000;
                        }
                    
                        .advancements-list::-webkit-scrollbar-thumb:hover {
                            background: #6a6a6a;
                        }
                    </style>
                    """;
        }

        public static String render(PlayerAdvancement player, int advancementCount) {
            String avatarUrl = "https://mc-heads.net/avatar/" + player.getName() + "/64";
            double progressPercentage = computeProgressPercentage(player.getAdvancements().size(), advancementCount);
            return String.format("""
                            <div class="player-card">
                                <div class="player-header">
                                    <img src="%s" alt="%s" class="player-avatar" onerror="this.src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=='">
                                    <div class="player-info">
                                        <div class="player-name">%s</div>
                                        <div class="player-stats">
                                            <span class="stat">
                                                <span class="stat-value">%d</span>/<span class="stat-value">%d</span>
                                                Advancements
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="progress-bar-container">
                                    <div class="progress-bar" style="width: %.1f%%"></div>
                                    <div class="progress-text">%.1f%% Complete</div>
                                </div>
                                <div class="advancements-list">
                            """,
                    avatarUrl,
                    player.getName(),
                    player.getName(),
                    player.completedAdvancements().size(),
                    advancementCount,
                    progressPercentage,
                    progressPercentage
            );
        }

        private static double computeProgressPercentage(int completedAdvancementCount, int advancementCount) {
            return advancementCount > 0
                    ? (completedAdvancementCount * 100.0 / advancementCount)
                    : 0.0;
        }

        public static String renderClose() {
            return """
                        </div>
                    </div>
                    """;
        }
    }

    /**
     * Advancement card component - displays individual advancement
     */
    public static class AdvancementCard {
        public static String getStyle() {
            return """
                    <style>
                        .advancement {
                            background: rgba(0, 0, 0, 0.4);
                            border: 2px solid #000000;
                            padding: 12px;
                            margin-bottom: 8px;
                            border-radius: 4px;
                            display: flex;
                            align-items: center;
                            transition: all 0.2s;
                        }
                    
                        .advancement:hover {
                            background: rgba(0, 0, 0, 0.6);
                            border-color: #ffffff;
                        }
                    
                        .advancement.awarded {
                          background: rgba(185, 143, 44, 1);
                          box-shadow:
                           inset 0 3px rgb(219, 162, 19),
                           inset 0 -3px rgb(73, 54, 6),
                           inset 3px 0 rgb(219, 162, 19),
                           inset -3px 0 rgb(73, 54, 6);
                        }
                    
                        .advancement.challenge {
                            border-color: #ff55ff;
                            background: rgba(255, 85, 255, 0.1);
                        }
                    
                        .advancement.goal {
                            border-color: #55ffff;
                            background: rgba(85, 255, 255, 0.1);
                        }
                    
                        .advancement-icon {
                            width: 32px;
                            height: 32px;
                            margin-right: 12px;
                            image-rendering: pixelated;
                            flex-shrink: 0;
                        }
                    
                        .advancement-content {
                            flex: 1;
                        }
                    
                        .advancement-title {
                            font-size: 16px;
                            margin-bottom: 4px;
                            color: #fff;
                            text-shadow: 1px 1px 0px #3e3e3e;
                        }
                    
                        .advancement.awarded .advancement-title {
                            color: #fff;
                            text-shadow: 1px 1px 0px #000000;
                        }
                    
                        .advancement.challenge .advancement-title {
                            color: #ff55ff;
                            text-shadow: 1px 1px 0px #551555;
                        }
                    
                        .advancement.goal .advancement-title {
                            color: #55ff55;
                            text-shadow: 1px 1px 0px #155555;
                        }
                    
                        .advancement-description {
                            font-size: 12px;
                            color: #55ff55;
                            text-shadow: 1px 1px 0px #000000;
                        }
                    
                        .advancement-status {
                            font-size: 14px;
                            padding: 4px 8px;
                            border-radius: 3px;
                            background: rgba(0, 0, 0, 0.5);
                            flex-shrink: 0;
                        }
                    
                        .advancement.awarded .advancement-status {
                            color: #55ff55;
                            background: rgba(85, 255, 85, 0.2);
                        }
                    
                        .advancement:not(.awarded) .advancement-status {
                            color: #ff5555;
                            background: rgba(255, 85, 85, 0.2);
                        }
                    
                        .advancement-criteria {
                            margin-top: 8px;
                            padding-top: 8px;
                            border-top: 1px solid #3a3a3a;
                            font-size: 11px;
                            color: #888;
                        }
                    
                        .criteria-item {
                            margin: 2px 0;
                        }
                    
                        .criteria-awarded {
                            color: #55ff55;
                        }
                    </style>
                    """;
        }

        public static String render(PlayerAdvancementProgress advancement) {
            String completedClass = advancement.awarded() ? "awarded" : "";
            String icon = "textures/" + advancement.metadata().iconName() + ".png";

            StringBuilder html = new StringBuilder();
            html.append(String.format("""
                            <div class="advancement %s">
                                <img src="%s" class="advancement-icon" alt="icon">
                                <div class="advancement-content">
                                    <div class="advancement-title">%s</div>
                                    <div class="advancement-description">%s</div>
                            """,
                    completedClass,
                    icon,
                    getAdvancementTitle(advancement),
                    getAdvancementDescription(advancement)
            ));

            html.append("""
                        </div>
                    </div>
                    """
            );

            return html.toString();
        }

        private static String getAdvancementTitle(PlayerAdvancementProgress advancement) {
            if (advancement.metadata() != null && advancement.metadata().title() != null) {
                return advancement.metadata().title();
            }
            String[] parts = advancement.key().split("[:/]");
            String name = parts[parts.length - 1];
            return name.substring(0, 1).toUpperCase() + name.substring(1).replace("_", " ");
        }

        private static String getAdvancementDescription(PlayerAdvancementProgress advancement) {
            if (advancement.metadata() != null && advancement.metadata().description() != null) {
                return advancement.metadata().description();
            }
            return "Complete this advancement";
        }
    }
}
