package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.bd7xzz.kane.configmanager.LabelConfigFacade;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.LabelConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

@RestController
@RequestMapping("/label/config")
public class LabelConfigEndPoint {

    private final LabelConfigFacade labelConfigFacade;

    @Autowired
    public LabelConfigEndPoint(LabelConfigFacade labelConfigFacade) {
        this.labelConfigFacade = labelConfigFacade;
    }

    @PutMapping("/")
    public ResponseVO createLabelConfig(@RequestBody String requestJSON) {
        LabelConfigVO labelConfigVO = JSONUtil.parseObject(requestJSON, LabelConfigVO.class);
        return labelConfigFacade.createLabelConfig(labelConfigVO);
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteLabelConfig(@PathVariable("id") long id) {
        return labelConfigFacade.deleteLabelConfig(id);
    }

    @GetMapping("/{id}")
    public ResponseVO getLabelConfig(@PathVariable("id") long id) {
        return labelConfigFacade.getLabelConfig(id);
    }

    @PostMapping("/{id}")
    public ResponseVO updateLabelConfig(@PathVariable("id") long id, @RequestBody String requestJSON) {
        LabelConfigVO labelConfigVO = JSONUtil.parseObject(requestJSON, LabelConfigVO.class);
        labelConfigVO.setId(id);
        return labelConfigFacade.updateLabelConfig(labelConfigVO);
    }

    @GetMapping("/")
    public ResponseVO listLabelConfig() {
        return labelConfigFacade.listLabelConfig();
    }
}
