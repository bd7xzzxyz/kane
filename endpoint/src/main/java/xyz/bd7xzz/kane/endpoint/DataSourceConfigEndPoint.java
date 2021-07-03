package xyz.bd7xzz.kane.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bd7xzz.kane.configmanager.DataSourceConfigFacade;
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


    @PostMapping("/")
    public ResponseVO createDataSource(DataSourceConfigVO dataSourceConfigVO) {
        return dataSourceConfigFacade.createDataSource(dataSourceConfigVO);
    }
}
