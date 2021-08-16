package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.bd7xzz.kane.selection.SelectionFacade;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.ResponseVO;
import xyz.bd7xzz.kane.vo.SelectionConfigVO;

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
        SelectionConfigVO selectionConfig = JSONUtil.parseObject(requestJSON, SelectionConfigVO.class);
        return selectionFacade.createSelection(selectionConfig);
    }

    @PostMapping("/{id}")
    public ResponseVO updateSelection(@PathVariable long id, @RequestBody String requestJSON) {
        SelectionConfigVO selectionConfig = JSONUtil.parseObject(requestJSON, SelectionConfigVO.class);
        selectionConfig.setId(id);
        return selectionFacade.updateSelection(selectionConfig);
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteSelection(@PathVariable long id) {
        return selectionFacade.deleteSelection(id);
    }

    @GetMapping("/{id}")
    public ResponseVO getSelection(@PathVariable long id) {
        return selectionFacade.getSelection(id);
    }

    @PostMapping("/execute/{id}")
    public ResponseVO executeSelection(@PathVariable long id) {
        return selectionFacade.executeSelection(id);
    }

    @PostMapping("/task/stop/{taskId}")
    public ResponseVO stopTask(@PathVariable long taskId) {
        return selectionFacade.stopTask(taskId);
    }

    @PostMapping("/task/pause/{taskId}")
    public ResponseVO pauseTask(@PathVariable long taskId) {
        return selectionFacade.pauseTask(taskId);
    }

    @PostMapping("/task/resume/{taskId}")
    public ResponseVO resumeTask(@PathVariable long taskId) {
        return selectionFacade.resumeTask(taskId);
    }

    @GetMapping("/task/{taskId}")
    public ResponseVO getTask(@PathVariable long taskId) {
        return selectionFacade.getTask(taskId);
    }
}
