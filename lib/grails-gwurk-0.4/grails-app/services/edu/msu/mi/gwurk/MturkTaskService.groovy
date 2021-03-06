package edu.msu.mi.gwurk

import grails.transaction.Transactional
import groovy.util.logging.Log4j


@Transactional
@Log4j
class MturkTaskService {

    def assignmentFx = [:]
    def hitFx = [:]
    def taskStartingFx = [:]
    def taskCompleteFx = [:]
    def workflowFx = [:]
    def taskConditional = [:]



    def onAssignment(TaskRun taskRun, AssignmentView assignmentView) {
        if (assignmentFx[taskRun.task.name]) {
            assignmentFx[taskRun.task.name](new GwurkEvent(taskRun,assignmentView))
            log.info("On Assignment: ${taskRun.task.name}")
        }
    }

    def onHit(TaskRun taskRun, HitView hitView) {
        log.info("On Hit: ${taskRun.task.name}")
        hitFx[taskRun.task.name](new GwurkEvent(taskRun,hitView))
    }

    def onTaskStarting(TaskRun taskRun) {
        log.info("On Task Starting: ${taskRun.task.name}")
        log.info("Dumping taskStartingFX: " + taskStartingFx.toString());
        taskStartingFx[taskRun.task.name](new GwurkEvent(taskRun))
    }

    def onTaskComplete(TaskRun taskRun) {
        log.info("On Task Complete: ${taskRun.task.name}")
        taskCompleteFx[taskRun.task.name](new GwurkEvent(taskRun))
    }

    def onWorkflow(WorkflowRun workflowRun) {
        log.info("On Workflowt: ${workflowRun.workflow.name}")
        workflowFx[workflowRun.workflow.name](new GwurkEvent(workflowRun))
    }

    boolean advanceToTask(TaskRun from, Task to) {
        log.info("Attempting to run conditional ${from.task.name} -> ${to.name}")
        if (taskConditional["${from.task.name}_${to.name}"]) {
            taskConditional["${from.task.name}_${to.name}"](from)
        } else {
            true
        }
    }


    def installConditional(Task from, Task to, Closure c) {
        taskConditional["${from.name}_${to.name}"]=c
    }



    def installTask(Task t, Closure c) {
        if (c) {
            taskStartingFx+=[ (t.name) : {evt -> c(GwurkEvent.Type.TASK_STARTING,evt)}]
            taskCompleteFx+=[ (t.name) : {evt -> c(GwurkEvent.Type.TASK_COMPLETE,evt)}]
            assignmentFx+=[ (t.name) :{evt -> c(GwurkEvent.Type.ASSIGNMENT_COMPLETE,evt)}]
            hitFx+=[ (t.name) :{evt -> c(GwurkEvent.Type.HIT_COMPLETE,evt)}]

        }
    }

    def installWorkflow(Workflow w, Closure closure) {
       if (closure) {
           workflowFx+=[ (w.name) :{evt -> closure(GwurkEvent.Type.WORKFLOW_COMPLETE,evt)}]
       }
    }
}
