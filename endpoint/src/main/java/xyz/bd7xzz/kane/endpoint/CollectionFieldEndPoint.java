package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.bd7xzz.kane.configmanager.CollectionFieldFacade;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.CollectionFieldVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

import java.util.List;

@RestController
@RequestMapping("/data_source/field")
public class CollectionFieldEndPoint {
    private final CollectionFieldFacade collectionFieldFacade;

    @Autowired
    public CollectionFieldEndPoint(CollectionFieldFacade collectionFieldFacade) {
        this.collectionFieldFacade = collectionFieldFacade;
    }

    @PutMapping("/")
    public ResponseVO addCollectionField(@RequestBody String requestJSON) {
        List<CollectionFieldVO> collectionFields = JSONUtil.parseArray(requestJSON, CollectionFieldVO.class);
        return collectionFieldFacade.addCollectionField(collectionFields);
    }

    @PostMapping("/{id}")
    public ResponseVO updateCollectionField(@PathVariable("id") long id, @RequestBody String requestJSON) {
        CollectionFieldVO collectionFieldVO = JSONUtil.parseObject(requestJSON, CollectionFieldVO.class);
        collectionFieldVO.setId(id);
        return collectionFieldFacade.updateCollectionField(collectionFieldVO);
    }

    @GetMapping("/{id}")
    public ResponseVO getCollectionField(@PathVariable("id") long id) {
        return collectionFieldFacade.getCollectionField(id);
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteCollectionField(@PathVariable("id") long id) {
        return collectionFieldFacade.deleteCollectionField(id);
    }

    @GetMapping("/all/{dataSourceId}")
    public ResponseVO getCollectionFieldByDataSourceId(@PathVariable("dataSourceId") long dataSourceId) {
        return collectionFieldFacade.getCollectionFieldByDataSourceId(dataSourceId);
    }
}
