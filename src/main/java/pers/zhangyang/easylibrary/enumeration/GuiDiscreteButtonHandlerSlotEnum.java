package pers.zhangyang.easylibrary.enumeration;

public enum GuiDiscreteButtonHandlerSlotEnum {
    zero(0),first(1),;

    private final int slot;
    GuiDiscreteButtonHandlerSlotEnum(int i) {
        slot=i;
    }

    public int getSlot() {
        return slot;
    }
}
