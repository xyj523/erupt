package xyz.erupt.upms.handler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.fun.ChoiceFetchHandler;
import xyz.erupt.annotation.fun.VLModel;
import xyz.erupt.upms.cache.EruptCacheFactory;
import xyz.erupt.upms.cache.IEruptCache;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author YuePeng
 * date 2021/01/03 18:00
 */
@Component
public class SqlChoiceFetchHandler implements ChoiceFetchHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final String CACHE_SPACE = SqlChoiceFetchHandler.class.getName() + ":";
    @Resource
    private EruptCacheFactory eruptCacheFactory;
    private IEruptCache<List<VLModel>> dictCache;

    @PostConstruct
    public void init() {
        dictCache = eruptCacheFactory.getInstance(5_000);
    }

    @Override
    public List<VLModel> fetch(String[] params) {
        if (null == params || params.length == 0) {
            throw new RuntimeException("SqlChoiceFetchHandler → params not found");
        }
        return dictCache.get(CACHE_SPACE + params[0], (key) -> {
            return jdbcTemplate.query(params[0], (rs, i) -> {
                if (rs.getMetaData().getColumnCount() == 1) {
                    return new VLModel(rs.getString(1), rs.getString(1));
                } else {
                    return new VLModel(rs.getString(1), rs.getString(2));
                }
            });
        });
    }

}
