package com.daqem.jobsplus.command.arguments;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class JobArgument implements ArgumentType<JobInstance> {

    public static JobArgument job() {
        return new JobArgument();
    }

    @Override
    public JobInstance parse(StringReader reader) throws CommandSyntaxException {
        return JobManager.getInstance().getJobs().get(Identifier.read(reader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Collection<Identifier> jobs = JobManager.getInstance().getJobs().keySet();
        return SharedSuggestionProvider.suggestResource(jobs, builder);
    }

    public static JobInstance getJob(CommandContext<?> context, String name) {
        return context.getArgument(name, JobInstance.class);
    }
}