package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Menu;

import java.util.List;

public interface IMenuService {
    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenu(Integer id);
}
