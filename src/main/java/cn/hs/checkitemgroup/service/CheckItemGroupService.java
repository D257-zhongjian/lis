package cn.hs.checkitemgroup.service;

import cn.hs.checkitem.pojo.CheckItem;
import cn.hs.checkitemgroup.mapper.CheckItemGroupDetailMapper;
import cn.hs.checkitemgroup.mapper.CheckItemGroupMapper;
import cn.hs.checkitemgroup.pojo.CheckItemGroup;
import cn.hs.publicclass.method.BusinessException;
import cn.hs.publicclass.method.GetCookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CheckItemGroupService {

    @Autowired
    private CheckItemGroupMapper checkItemGroupMapper;

    @Autowired
    private CheckItemGroupDetailMapper checkItemGroupDetailMapper;

    @Autowired
    private GetCookieService getCookie;

    //查找检验项目组合
    public List<CheckItemGroup> getcheckItemGroups(String equipmentId, String checkItemGroup) {
        List<CheckItemGroup> checkItemGroupList = checkItemGroupMapper.getcheckItemGroups(getCookie.getHosNum(), equipmentId, checkItemGroup);
        return checkItemGroupList;
    }

    //查找检验项目组合对应的检验项目
    public List<CheckItem> getcheckItemNameList(String checkItemGroupId) {
        return checkItemGroupMapper.getcheckItemNameList(getCookie.getHosNum(),checkItemGroupId);
    }

    public int deleteCheckItemGroups(List<String> itemIdList) {
        if (itemIdList.isEmpty()){
            return 0;
        }
        checkItemGroupDetailMapper.deleteByGroupId(getCookie.getHosNum(),itemIdList);
        return checkItemGroupMapper.deleteCheckItemGroups(getCookie.getHosNum(),itemIdList);
    }

    //保存检验项目组合
    public void savecheckItemGroup(CheckItemGroup checkItemGroup) {
        String hosNum = getCookie.getHosNum();
        checkItemGroup.setHosnum(hosNum);
        if (StringUtils.isEmpty(checkItemGroup.getGroupId())){  //新增
            if (this.isRepeat(hosNum,checkItemGroup.getGroupName())){
                throw new BusinessException("检验项目组合名称不可重复");
            }
            checkItemGroup.setGroupId(checkItemGroupMapper.getMaxId(hosNum));
            checkItemGroupMapper.insertSelective(checkItemGroup);
        }else {  //更新
            checkItemGroupMapper.updateByPrimaryKeySelective(checkItemGroup);
        }
    }

    //判断检验项目名称是否重复
    private boolean isRepeat(String hosNum, String checkItemGroupName) {
        List<String> list =  checkItemGroupMapper.getAllcheckItemGroupName(hosNum);
        for (int i = 0; i < list.size(); i++) {
            if (checkItemGroupName.equals(list.get(i))){
                return true;
            }
        }
        return false;
    }

    //查找所有样品类型
    public List<String> getAllSampleType() {
        return checkItemGroupMapper.getAllSampleType(getCookie.getHosNum());
    }
}
