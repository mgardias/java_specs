package com.atlassian.bamboo;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.notification.CommittersRecipient;
import com.atlassian.bamboo.specs.builders.notification.EmailRecipient;
import com.atlassian.bamboo.specs.builders.notification.PlanFailedNotification;
import com.atlassian.bamboo.specs.builders.repository.git.GitRepository;
import com.atlassian.bamboo.specs.builders.task.DumpVariablesTask;
import com.atlassian.bamboo.specs.builders.trigger.ScheduledTrigger;
import com.atlassian.bamboo.specs.util.BambooServer;
import org.apache.commons.lang3.time.StopWatch;

import java.time.LocalTime;

// this is comment. Changing anything here does not affect any plan, duh. adsdsf
///move asd no change dfd fd fdsdfsf dsf fsd asd sdf sdf s
@BambooSpec
public class PlanSpec {
    public Plan plan(int projectIndex, int planIndex) {
        final Plan plan = new Plan(new Project()
                .key(new BambooKey("MANY" + projectIndex))
                .name("Many Plans" + projectIndex),
                "hbk specs test number " + planIndex,
                new BambooKey("HST" + planIndex))
                .linkedRepositories("github java specs")
                .planBranchManagement(new PlanBranchManagement().createManually().triggerBuildsManually())
                .notifications(new Notification().type(new PlanFailedNotification()).recipients(new EmailRecipient("bamboo@examp3l2e2.com")))
                .stages(createStage())
                .ignoreHungBuilds();
        return plan;
    }

    private static Stage createStage() {
        final Stage stage = new Stage("Default Stage");
        for (int i = 0; i < 10; i++) {
            stage.jobs(new Job("Default Job" + i,
                    new BambooKey("JOB" + i))
                    .tasks(new DumpVariablesTask()));
        }
        return stage;
    }

    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://localhost:8085/bamboo");
        final PlanSpec planSpec = new PlanSpec();

        for (int projectIdx = 0; projectIdx < 1; projectIdx++) {
            for (int i = 0; i < 1; i++) {
                final Plan plan = planSpec.plan(projectIdx, i);
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                bambooServer.publish(plan);
                System.out.println(stopWatch);
            }
        }
    }
}