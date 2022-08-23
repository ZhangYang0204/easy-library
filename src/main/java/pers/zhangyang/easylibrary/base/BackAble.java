package pers.zhangyang.easylibrary.base;

/**
 * 一旦继承了这个方法，该类如果是GuiPage的子类并且点击的槽位是49的时候，该方法back则会被调用
 */
public interface BackAble {
    void back();
    int getBackSlot();
}
