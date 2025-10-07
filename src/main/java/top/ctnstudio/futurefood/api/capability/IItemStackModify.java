package top.ctnstudio.futurefood.api.capability;

public interface IItemStackModify {
  void onItemChanged(int slot);

  void onItemLoad();
}
