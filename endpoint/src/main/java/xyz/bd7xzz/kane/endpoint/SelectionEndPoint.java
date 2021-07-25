package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.bd7xzz.kane.selection.SelectionFacade;
import xyz.bd7xzz.kane.vo.ResponseVO;

@RestController
@RequestMapping("/selection")
public class SelectionEndPoint {

    private final SelectionFacade selectionFacade;

    @Autowired
    public SelectionEndPoint(SelectionFacade selectionFacade) {
        this.selectionFacade = selectionFacade;
    }

    @PutMapping("/")
    public ResponseVO createSelection(@RequestBody String requestJSON) {
        return null;
    }

    @PostMapping("/")
    public ResponseVO updateSelection(@RequestBody String requestJSON) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteSelection(@PathVariable long id) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseVO getSelection(@PathVariable long id) {
        return null;
    }

    @PostMapping("/execute")
    public ResponseVO executeSelection(@RequestBody String requestJSON) {
        return null;
    }

    @GetMapping("/task/{subTaskId}")
    public ResponseVO getSubTask(@PathVariable long subTaskId) {
        return null;
    }

    @PostMapping("/task/kill/{subTaskId}")
    public ResponseVO killTask(@PathVariable long subTaskId) {
        return null;
    }

    @PostMapping("/task/cancel/{subTaskId}")
    public ResponseVO subTaskId(@PathVariable long subTaskId) {
        return null;
    }

    @PostMapping("/task/stop/{taskId}")
    public ResponseVO stopTask(@PathVariable long taskId) {
        return null;
    }

    @PostMapping("/task/start/{taskId}")
    public ResponseVO startTask(@PathVariable long taskId) {
        return null;
    }
}
