package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import util.ResultEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuHandler {

    @Autowired
    IMenuService menuService;


    @RequestMapping("menu/remove.json")
    public ResultEntity<String> removeMenu(Integer id){

        menuService.removeMenu(id);

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){

        menuService.updateMenu(menu);


        return ResultEntity.successWithoutData();
    }

    @RequestMapping("menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){

        menuService.saveMenu(menu);

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree(){

        // 1 查询全部Menu对象
        List<Menu> menuList = menuService.getAll();

        // 2.声明一个变量用于存储找到的根节点
        Menu root = null;

        // 3.创建Map对象用来存储id和menu对象的对应关系便于查找父节点
        Map<Integer,Menu> menuMap = new HashMap<>();

        // 4.遍历menuList填充menuMap
        for (Menu menu : menuList){

          Integer id = menu.getId();
          menuMap.put(id,menu);

        }
        // 5.再次遍历menuList，查找根节点，组装父节点
        for(Menu menu:menuList){
            // 6.获取当前menu对象pid属性值
            Integer pid= menu.getPid();

            // 7.如果pid为空，判定为根节点
            if(pid == null){
                root = menu;
                continue;
            }
            // 如果pid不为null，说明当前节点有父节点
            Menu father = menuMap.get(pid);
            // 将当前节点存入父节点的children集合
            father.getChildren().add(menu);

        }

        // 结果上面的运算，根节点包含了整个树形结构，返回根节点就是返回整个树

        return ResultEntity.successWithData(root);
    }
}
