package com.github.dcysteine.nesql.exporter.plugin.quest.postprocessor;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;

import java.util.List;

public class QuestPostProcessor {
    private final Database database;

    public QuestPostProcessor(Database database) {
        this.database = database;
    }

    public void postProcess() {
        List<DBEntry<IQuest>> questEntries = QuestDatabase.INSTANCE.getEntries();
        int total = questEntries.size();
        Logger.QUEST.info("Post-processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(database);
        int count = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            count++;

            Quest quest = questFactory.findQuest(entry.getID());
            questFactory.setRequiredQuests(quest, entry.getValue().getRequirements());

            if (Logger.intermittentLog(count)) {
                Logger.QUEST.info("Post-processed quest {} of {}", count, total);
                Logger.QUEST.info("Most recent quest: {}", quest.getName());
            }
        }

        Logger.QUEST.info("Finished post-processing quests!");
    }
}
