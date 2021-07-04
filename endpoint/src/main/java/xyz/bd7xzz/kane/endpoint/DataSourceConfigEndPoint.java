package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigFacade;
import xyz.bd7xzz.kane.util.JSONUtil;
import xyz.bd7xzz.kane.vo.DataSourceConfigVO;
import xyz.bd7xzz.kane.vo.ResponseVO;

@RestController
@RequestMapping("/data_source/config")
public class DataSourceConfigEndPoint {

    private final DataSourceConfigFacade dataSourceConfigFacade;

    @Autowired
    public DataSourceConfigEndPoint(DataSourceConfigFacade dataSourceConfigFacade) {
        this.dataSourceConfigFacade = dataSourceConfigFacade;
    }


    @PutMapping("/")
    public ResponseVO createDataSource(@RequestBody String requestJSON) {
        DataSourceConfigVO dataSourceConfigVO = JSONUtil.parseObject(requestJSON, DataSourceConfigVO.class);
        return dataSourceConfigFacade.createDataSource(dataSourceConfigVO);
    }

    @PostMapping("/{id}")
    public ResponseVO updateDataSource(@PathVariable long id, @RequestBody String requestJSON) {
        DataSourceConfigVO dataSourceConfigVO = JSONUtil.parseObject(requestJSON, DataSourceConfigVO.class);
        dataSourceConfigVO.setId(id);
        return dataSourceConfigFacade.updateDataSource(dataSourceConfigVO);
    }

    @GetMapping("/{id}")
    public ResponseVO getDataSource(@PathVariable long id) {
        return dataSourceConfigFacade.getDataSource(id);
    }

    @DeleteMapping("/{id}")
    public ResponseVO deleteDataSource(@PathVariable long id) {
        return dataSourceConfigFacade.deleteDataSource(id);
    }

    @GetMapping("/")
    public ResponseVO listDataSource() {
        return dataSourceConfigFacade.listDataSource();
    }
}
