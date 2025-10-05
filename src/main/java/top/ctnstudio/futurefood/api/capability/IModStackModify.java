package top.ctnstudio.futurefood.api.capability;

public interface IModStackModify {
  void onStackContentsChanged(int slot);

  void onStackLoad();
}
