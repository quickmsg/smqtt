<template>
  <context-menu class="contextMenu">
    <node-menu>
      <menu-li command="copy" />
      <menu-li command="delete" />
    </node-menu>
    <edge-menu>
      <menu-li command="delete" />
    </edge-menu>
    <group-menu>
      <menu-li command="copy" />
      <menu-li command="delete" />
      <menu-li command="unGroup" icon="ungroup" text="Ungroup" />
    </group-menu>
    <multi-menu>
      <menu-li command="copy" />
      <menu-li command="paste" />
      <menu-li command="addGroup" icon="group" text="Add Group" />
      <menu-li command="delete" />
    </multi-menu>
    <canvas-menu>
      <menu-li command="undo" />
      <menu-li command="redo" />
      <menu-li command="pasteHere" icon="paste" text="Paste Here" />
    </canvas-menu>
  </context-menu>
</template>

<script>
import { NodeMenu, EdgeMenu, GroupMenu, MultiMenu, CanvasMenu, ContextMenu } from 'vg-editor'
import MenuLi from './MenuItem'

export default {
  name: 'FlowContextMenu',
  components: {
    NodeMenu,
    EdgeMenu,
    GroupMenu,
    MultiMenu,
    CanvasMenu,
    ContextMenu,
    MenuLi
  }
}
</script>

<style lang="less">
.contextMenu {
  display: none;
  overflow: hidden;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  .command.disable {
    .item {
      color: rgba(0, 0, 0, 0.25);
      cursor: auto;
      &:hover {
        background: #fff;
      }
    }
  }
  .item {
    display: flex;
    align-items: center;
    padding: 5px 12px;
    cursor: pointer;
    transition: all 0.3s;
    user-select: none;
    &:hover {
      background: #e6f7ff;
    }
    i {
      margin-right: 8px;
    }
  }
}
</style>
