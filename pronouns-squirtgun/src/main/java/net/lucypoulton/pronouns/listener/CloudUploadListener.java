package net.lucypoulton.pronouns.listener;

import com.google.gson.Gson;
import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.api.SetPronounsEvent;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.provider.CloudPronounProvider;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.EventListener;
import net.lucypoulton.squirtgun.platform.scheduler.Task;

import java.util.List;
import java.util.Set;

public class CloudUploadListener implements EventListener {

    private final ProNouns plugin;
    private final CloudPronounProvider cloudPronounProvider;

    public CloudUploadListener(ProNouns plugin, CloudPronounProvider cloudPronounProvider) {
        this.plugin = plugin;
        this.cloudPronounProvider = cloudPronounProvider;
    }

    private void onSet(SetPronounsEvent e) {
        Set<PronounSet> all = plugin.getPronounHandler().getAllPronouns();
        for (PronounSet set : e.getSet()) {
            if (!all.contains(set)) {
                plugin.getPlatform().getTaskScheduler().start(
                    Task.builder().async().action(() -> cloudPronounProvider.submit(set)).build());
            }
        }
    }

    private final List<EventHandler<?>> handlers = List.of(
        EventHandler.builder(SetPronounsEvent.class).executeOnCancel().handle(this::onSet).build()
    );

    @Override
    public List<EventHandler<?>> handlers() {
        return handlers;
    }
}
