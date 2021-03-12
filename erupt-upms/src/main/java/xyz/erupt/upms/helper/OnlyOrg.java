package xyz.erupt.upms.helper;

import org.springframework.stereotype.Service;
import xyz.erupt.annotation.PreDataProxy;
import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.upms.model.EruptUser;
import xyz.erupt.upms.model.base.HyperModel;
import xyz.erupt.upms.service.EruptUserService;

import javax.annotation.Resource;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

/**
 * @author liyuepeng
 * @date 2021/3/10 11:30
 */
@MappedSuperclass
@PreDataProxy(OnlyOrg.class)
@Service
public class OnlyOrg extends HyperModel implements DataProxy<Object> {

    @Resource
    @Transient
    private HttpServletRequest request;

    @Resource
    @Transient
    private EruptUserService eruptUserService;

    @Override
    public String beforeFetch() {
        EruptUser eruptUser = eruptUserService.getCurrentEruptUser();
        if (!eruptUser.getIsAdmin()) {
            if (null == eruptUser.getEruptOrg()) {
                return "1 = 2";
            } else {
                return request.getHeader("erupt") + ".createUser.eruptOrg.id = " + eruptUser.getEruptOrg().getId();
            }
        } else {
            return null;
        }
    }
}