package nardiff.mt

import edu.msu.mi.gwurk.Task
import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ResourceLoader

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NarrativeController implements org.springframework.context.ResourceLoaderAware {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", demographics: "POST"]

    ResourceLoader resourceLoader

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Narrative.list(params), model: [narrativeInstanceCount: Narrative.count()]
    }

    def show(Narrative narrativeInstance) {
        respond narrativeInstance
    }

    def create() {
        respond new Narrative(params)
    }

    @Transactional
    def complete() {
        NardiffStuff n = new NardiffStuff();
        n.doInsert(params);
    }


    def preview() {

    }

    def storyImage() {
        println("Got request in ${params}")
        NarrativeRequest request = NarrativeRequest.get(params.narrativeRequestId)
        BufferedImage image = Text2PNG.getImage(request.parent_narrative.text)
        response.setContentType("image/png")
        OutputStream os = response.getOutputStream()
        ImageIO.write(image,"png",os)
        os.close()

    }

    @Transactional
    def demographics() {
        println "$params"
        Turker t = Turker.findByMturk_id(params.workerid)
        if (!t) {
            t = new Turker()
            t.mturk_id = params.workerid
        }
        t.age = params.age as Integer
        t.gender = params.gender
        t.education = params.education as Integer
        t.save()

        response.status = 200
        render "OK"
    }

    @Transactional
    def start() {
        //  [taskrun: params.task, workerId: params.workerId, action: run.taskProperties.action,controller: run.taskProperties.controller, submiturl: run.submitUrl, assignmentId: params.assignmentId]

        Integer taskID;
        String assignmentID;
        String workerID;
        String rootNarrativeIDString;

        try {
            taskID = Integer.parseInt(params.get("task"))
        } catch (Exception e) {
            log.info("start() hit with no taskID, rendering preview");
            render(view: "preview.gsp");
            return;
        }

        assignmentID = params.get("assignmentId");
        if (assignmentID == null) {
            log.info("start() hit without assignmentID = \"" + assignmentID + "\", rendering preview");
            render(view: "preview.gsp");
            return;
        }

        workerID = params.get("workerId");
        if (workerID == null) {
            log.info("start() hit without workerID, rendering preview");
            render(view: "preview.gsp");
            return;
        }

        // Now that we have a taskID, assignmentID, and workerID, we can make
        // the assignment and have the user start working on it.
        log.info("start() called with assignmentID = \"" + assignmentID + "\"")

        Long rootNarrativeID = null;
        try {
            rootNarrativeID = Long.parseLong(params.get("rootNarrativeId"));
        } catch (Exception e) {
            rootNarrativeID = null;
        }

        Task t = null;
        if (rootNarrativeID == null) {
            t = Task.get(taskID);
            rootNarrativeID = Long.parseLong((t.getProperties().get("taskProperties")).getAt("parameter"));
        }

        NarrativeRequest nr = NardiffStuff.findRequest(rootNarrativeID);
        boolean askForDemographics = NardiffStuff.assignRequestToTurker(nr, workerID, assignmentID);
        params.setProperty("askForDemographics", askForDemographics);
        params.setProperty("narrativeRequest", nr);
        params.setProperty("task", t);

        // Generate the image
        nr.attach();
        Narrative parentNarrative = nr.getParent_narrative();

        String narrativeImagePath = servletContext.getRealPath("/images/narratives/");

        Text2PNG.writeImageFile(parentNarrative.text, parentNarrative.id, narrativeImagePath);

//        NarrativeRequest nr = NardiffStuff.findRequest(params.get("narrative_id"));
//        boolean askForDemographics = NardiffStuff.assignRequestToTurker(workerId);
//        int beginStage = 2;
//        if (askForDemographics == false)
//            beginStage = 3;

        log.info(params);
    }

    @Transactional
    def save(Narrative narrativeInstance) {
        if (narrativeInstance == null) {
            notFound()
            return
        }

        if (narrativeInstance.hasErrors()) {
            respond narrativeInstance.errors, view: 'create'
            return
        }

        narrativeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'narrative.label', default: 'Narrative'), narrativeInstance.id])
                redirect narrativeInstance
            }
            '*' { respond narrativeInstance, [status: CREATED] }
        }
    }

    def edit(Narrative narrativeInstance) {
        respond narrativeInstance
    }

    @Transactional
    def update(Narrative narrativeInstance) {
        if (narrativeInstance == null) {
            notFound()
            return
        }

        if (narrativeInstance.hasErrors()) {
            respond narrativeInstance.errors, view: 'edit'
            return
        }

        narrativeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Narrative.label', default: 'Narrative'), narrativeInstance.id])
                redirect narrativeInstance
            }
            '*' { respond narrativeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Narrative narrativeInstance) {

        if (narrativeInstance == null) {
            notFound()
            return
        }

        narrativeInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Narrative.label', default: 'Narrative'), narrativeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'narrative.label', default: 'Narrative'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

}
