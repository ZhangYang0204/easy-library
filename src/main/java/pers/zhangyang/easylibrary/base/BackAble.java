package pers.zhangyang.easylibrary.base;

/**
 * 一旦实现这个类，该类如果是GuiPage的子类并且点击返回槽位的时候，该方法back则会被调用
 */
public interface BackAble {
    void back();

    int getBackSlot();
}
